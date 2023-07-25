package dev.rgbmc.tabstats.command;

import com.google.common.collect.Ordering;
import dev.rgbmc.tabstats.exceptions.UnknowPlayerException;
import dev.rgbmc.tabstats.stats.HyCache;
import dev.rgbmc.tabstats.stats.HyProfile;
import dev.rgbmc.tabstats.stats.HyUtils;
import dev.rgbmc.tabstats.stats.PackagedProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.weavemc.loader.api.command.Command;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class OverlayCommand extends Command {
    public OverlayCommand() {
        super("overlay", "olay", "ol");
    }

    @Override
    public void handle(String[] args) {
        Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§e正在获取玩家数据..."));
        CompletableFuture.runAsync(() -> {
            Ordering<PackagedProfile> ordering = Ordering.from((o1, o2) -> {
                if (o1.isNicked() && !o2.isNicked()) {
                    return -1;
                } else if (o1.isNicked() && o2.isNicked()) {
                    return 0;
                } else if (!o1.isNicked() && o2.isNicked()) {
                    return 1;
                }
                double o1_fkdr = (double) o1.getProfile().getBw_final_kill_long() / (double) o1.getProfile().getBw_final_death_long();
                double o2_fkdr = (double) o2.getProfile().getBw_final_kill_long() / (double) o2.getProfile().getBw_final_death_long();
                if (o1_fkdr > o2_fkdr) {
                    return -1;
                } else if (o1_fkdr < o2_fkdr) {
                    return 1;
                }
                return 0;
            });
            List<PackagedProfile> profiles = new ArrayList<>();
            List<EntityPlayer> playerInfos = List.copyOf(Minecraft.getMinecraft().theWorld.playerEntities);
            //System.out.println("Info Size: " + playerInfos.size());
            for (EntityPlayer entityPlayer : playerInfos) {
                PackagedProfile packagedProfile = new PackagedProfile(entityPlayer);
                if (HyCache.isCached(packagedProfile.getName())) {
                    packagedProfile.setProfile(HyCache.getByName(packagedProfile.getName()));
                    profiles.add(packagedProfile);
                    continue;
                }
                /*if (entityPlayer != null) {
                    System.out.println("Info: " + gameProfile.getName() + " onGround: " + entityPlayer.onGround + " Invisible: " + entityPlayer.isInvisible() + " ticksExisted: " + entityPlayer.ticksExisted + " Check Status: " + HyUtils.check(entityPlayer.getName()) + " EntityName: " + entityPlayer.getName());
                }*/
                boolean isNick = packagedProfile.getUuid().version() == 1;
                if (entityPlayer != null && !entityPlayer.onGround && HyUtils.check(entityPlayer.getName()) && !isNick) {
                    //System.out.println("Bypassed Bot: " + gameProfile.getName());
                    continue;
                }
                if (isNick) packagedProfile.setNicked(isNick);
                /*long real_start = System.currentTimeMillis();
                if (!HyUtils.isRealPlayer(gameProfile.getName())) {
                    System.out.println("Check Real Player: " + (System.currentTimeMillis() - real_start));
                    System.out.println(gameProfile.getName() + " is nicked");
                    packagedProfile.setNicked(true);
                    profiles.add(packagedProfile);
                    continue;
                }*/
                try {
                    long start = System.currentTimeMillis();
                    HyProfile hyProfile = new HyProfile(packagedProfile.getUuid().toString());
                    //System.out.println("Time Used: " + (System.currentTimeMillis() - start));
                    //System.out.println("玩家 " + gameProfile.getName() + " 的统计数据已获取");
                    packagedProfile.setProfile(hyProfile);
                    profiles.add(packagedProfile);
                    HyCache.cacheProfile(hyProfile, packagedProfile.getName());
                } catch (UnknowPlayerException ignore) {
                    //System.out.println(gameProfile.getName() + " is nicked");
                    packagedProfile.setNicked(true);
                    profiles.add(packagedProfile);
                    continue;
                } catch (Exception e) {
                    System.out.println("获取玩家: " + packagedProfile.getName() + " 的数据时遇到错误");
                    e.printStackTrace(System.out);
                }
            }
            long start = System.currentTimeMillis();
            List<PackagedProfile> sorted_profiles = ordering.sortedCopy(profiles);
            //System.out.println("Sorting Used Time: " + (System.currentTimeMillis() - start));
            //System.out.println("List Size: " + sorted_profiles.size());
            for (PackagedProfile packagedProfile : sorted_profiles) {
                if (!packagedProfile.isNicked()) {
                    HyProfile profile = packagedProfile.getProfile();
                    if (profile == null) {
                        //System.out.println("Bypassed null profile");
                        Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§a玩家: §e" + packagedProfile.getName() + " §e警告: §7[§c数据获取失败§7]"));
                        continue;
                    }
                    double fkdr = (double) profile.getBw_final_kill_long() / (double) profile.getBw_final_death_long();
                    Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§a玩家: §e" + packagedProfile.getName() + " §aBedwars等级: §e" + profile.getLevel() + " §aFKDR: §e" + String.format("%.2f", fkdr) + " §a最终击杀: §e" + profile.getBw_final_killed() + " §a胜场: §e" + profile.getBw_win_games()));
                } else {
                    Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§a玩家: §e" + packagedProfile.getName() + " §e警告: §7[§cNICKED§7]"));
                }
            }
        });
    }
}
