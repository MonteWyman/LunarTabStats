package dev.rgbmc.tabstats.command;

import dev.rgbmc.tabstats.stats.HyCache;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.weavemc.loader.api.command.Command;
import org.jetbrains.annotations.NotNull;

public class CleanCacheCommand extends Command {
    public CleanCacheCommand() {
        super("cleancache", "cache", "cca");
    }

    @Override
    public void handle(@NotNull String[] args) {
        Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§b数据缓存已清理!"));
        HyCache.clear();
    }
}
