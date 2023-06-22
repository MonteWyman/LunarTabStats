package dev.rgbmc.tabstats.stats;

import java.util.HashMap;
import java.util.Map;

public class HyCache {
    private final static Map<String, HyProfile> cache_map = new HashMap<>();

    public static void clear() {
        cache_map.clear();
    }

    public static HyProfile getByName(String name) {
        return cache_map.get(name);
    }

    public static void cacheProfile(HyProfile profile, String name) {
        cache_map.put(name, profile);
    }

    public static boolean isCached(String name) {
        return cache_map.containsKey(name);
    }
}
