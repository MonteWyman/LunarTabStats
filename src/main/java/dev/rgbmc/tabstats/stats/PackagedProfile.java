package dev.rgbmc.tabstats.stats;

import com.mojang.authlib.GameProfile;

public class PackagedProfile {
    private final GameProfile gameProfile;
    private HyProfile profile = null;
    private boolean nicked = false;

    public PackagedProfile(GameProfile gameProfile) {
        this.gameProfile = gameProfile;
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

    public GameProfile getGameProfile() {
        return gameProfile;
    }
}
