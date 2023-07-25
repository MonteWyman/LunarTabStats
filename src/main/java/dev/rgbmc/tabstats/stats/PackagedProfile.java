package dev.rgbmc.tabstats.stats;

import net.minecraft.entity.player.EntityPlayer;

import java.util.UUID;

public class PackagedProfile {
    private final String name;
    private final UUID uuid;
    private HyProfile profile = null;
    private boolean nicked = false;

    public PackagedProfile(EntityPlayer entityPlayer) {
        this.name = entityPlayer.getName();
        this.uuid = entityPlayer.getUniqueID();
    }

    public HyProfile getProfile() {
        return profile;
    }

    public void setProfile(HyProfile profile) {
        this.profile = profile;
    }

    public boolean isNicked() {
        return nicked;
    }

    public void setNicked(boolean nicked) {
        this.nicked = nicked;
    }

    public String getName() {
        return name;
    }

    public UUID getUuid() {
        return uuid;
    }
}
