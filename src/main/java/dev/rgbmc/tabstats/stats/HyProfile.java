package dev.rgbmc.tabstats.stats;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.rgbmc.tabstats.TabStats;

public class HyProfile {
    private final long[] EASY_LEVELS_XP = {500, 1000, 2000, 3500};
    private String rank;
    private String language = "ENGLISH";
    private String bw_final_killed = "无";
    private String bw_final_death = "无";
    private String bw_win_games = "无";
    private String bw_losses_games = "无";
    private long bw_win_long = 0;
    private long bw_losses_long = 0;
    private String level = "无";
    private String network_level = "无";
    private String bw_bed_broken = "无";
    private String bw_bed_lost = "无";
    private long bw_final_kill_long = 0;
    private long bw_final_death_long = 0;
    private long bw_kill_long = 0;
    private long bw_death_long = 0;
    private long bw_winstreak = 0;
    private String bw_kill = "无";
    private String bw_death = "无";
    private String sw_level = "无";
    private long sw_coins = 0;
    private long sw_kills = 0;
    private long sw_deaths = 0;
    private long sw_wins = 0;
    private long sw_losses = 0;
    private long de_losses = 0;
    private long de_wins = 0;
    private long de_kills = 0;
    private String display = "Unknow";
    private String plusColor = "RED";
    private String rankColor = "GOLD";
    private long gifted_rank = 0;
    private long karma = 0;

    public HyProfile(String uuid) throws Exception {
        JsonObject jo = new JsonParser().parse(HyUtils.request("https://api.hypixel.net/player?key=" + TabStats.API_KEY + "&uuid=" + uuid)).getAsJsonObject();
        if (!jo.has("player")) return;
        JsonObject pjo = jo.get("player").getAsJsonObject();
        if (pjo.has("monthlyPackageRank")) {
            rank = pjo.get("monthlyPackageRank").getAsString();
            if (rank.equals("NONE")) {
                if (pjo.has("newPackageRank")) {
                    rank = pjo.get("newPackageRank").getAsString();
                } else {
                    rank = "default";
                }
            }
        } else {
            if (pjo.has("newPackageRank")) {
                rank = pjo.get("newPackageRank").getAsString();
            } else {
                rank = "default";
            }
        }
        if (pjo.has("rank")) {
            if (pjo.get("rank").getAsString().equals("ADMIN") || pjo.get("rank").getAsString().equals("OWNER") || pjo.get("rank").getAsString().equals("HELPER") || pjo.get("rank").getAsString().equals("YOUTUBER")
                    || pjo.get("rank").getAsString().equals("GAME_MASTER") || pjo.get("rank").getAsString().equals("MODERATOR") ||
                    pjo.get("rank").getAsString().equals("JR_HELPER")) {
                rank = pjo.get("rank").getAsString();
            }
        }
        if ((rank.equals("VIP_PLUS") || rank.equals("MVP_PLUS") || rank.equals("SUPERSTAR")) && pjo.has("rankPlusColor")) {
            plusColor = pjo.get("rankPlusColor").getAsString();
        }
        if (pjo.has("giftingMeta")) {
            JsonObject giftMeta = pjo.get("giftingMeta").getAsJsonObject();
            if (giftMeta.has("ranksGiven")) {
                gifted_rank = giftMeta.get("ranksGiven").getAsLong();
            }
        }
        if (pjo.has("karma")) {
            karma = pjo.get("karma").getAsLong();
        }
        if (rank.equals("VIP_PLUS") && !pjo.has("rankPlusColor")) {
            plusColor = "GOLD";
        }
        if (rank.equals("SUPERSTAR") && pjo.has("monthlyRankColor")) {
            rankColor = pjo.get("monthlyRankColor").getAsString();
        }
        if (pjo.has("prefix")) {
            rank = pjo.get("prefix").getAsString().replaceAll("[§]", "&").replaceAll("(&+)([0-9a-fA-F])", "").replaceAll("(&+)([k-orK-OR])", "").replace("[", "").replace("]", "");
        }
        if (pjo.has("userLanguage")) {
            language = pjo.get("userLanguage").getAsString();
        }
        if (pjo.has("stats")) {
            if (pjo.get("stats").getAsJsonObject().has("Bedwars")) {
                JsonObject bwjo = pjo.get("stats").getAsJsonObject().get("Bedwars").getAsJsonObject();
                if (bwjo.has("final_kills_bedwars")) {
                    bw_final_kill_long = bwjo.get("final_kills_bedwars").getAsLong();
                    bw_final_killed = String.valueOf(bwjo.get("final_kills_bedwars").getAsLong());
                }
                if (bwjo.has("final_deaths_bedwars")) {
                    bw_final_death_long = bwjo.get("final_deaths_bedwars").getAsLong();
                    bw_final_death = String.valueOf(bwjo.get("final_deaths_bedwars").getAsLong());
                }
                if (bwjo.has("kills_bedwars")) {
                    bw_kill_long = bwjo.get("kills_bedwars").getAsLong();
                    bw_kill = String.valueOf(bwjo.get("kills_bedwars").getAsLong());
                }
                if (bwjo.has("deaths_bedwars")) {
                    bw_death_long = bwjo.get("deaths_bedwars").getAsLong();
                    bw_death = String.valueOf(bwjo.get("deaths_bedwars").getAsLong());
                }
                if (bwjo.has("wins_bedwars")) {
                    bw_win_long = bwjo.get("wins_bedwars").getAsLong();
                    bw_win_games = String.valueOf(bwjo.get("wins_bedwars").getAsLong());
                }
                if (bwjo.has("winstreak")) {
                    bw_winstreak = bwjo.get("winstreak").getAsLong();
                }
                if (bwjo.has("losses_bedwars")) {
                    bw_losses_long = bwjo.get("losses_bedwars").getAsLong();
                    bw_losses_games = String.valueOf(bwjo.get("losses_bedwars").getAsLong());
                }
                if (pjo.has("achievements") && pjo.get("achievements").getAsJsonObject().has("bedwars_level")) {
                    level = String.valueOf(pjo.get("achievements").getAsJsonObject().get("bedwars_level").getAsLong());
                }
                if (bwjo.has("beds_broken_bedwars")) {
                    bw_bed_broken = String.valueOf(bwjo.get("beds_broken_bedwars").getAsLong());
                }
                if (bwjo.has("beds_lost_bedwars")) {
                    bw_bed_lost = String.valueOf(bwjo.get("beds_lost_bedwars").getAsLong());
                }
            }
            if (pjo.get("stats").getAsJsonObject().has("SkyWars")) {
                JsonObject swjo = pjo.get("stats").getAsJsonObject().get("SkyWars").getAsJsonObject();
                if (swjo.has("levelFormatted")) {
                    String formated = swjo.get("levelFormatted").getAsString();
                    sw_level = HyUtils.uncolor(formated);
                }
                if (swjo.has("coins")) {
                    sw_coins = swjo.get("coins").getAsLong();
                }
                if (swjo.has("deaths")) {
                    sw_deaths = swjo.get("deaths").getAsLong();
                }
                if (swjo.has("losses")) {
                    sw_losses = swjo.get("losses").getAsLong();
                }
                if (swjo.has("kills")) {
                    sw_kills = swjo.get("kills").getAsLong();
                }
                if (swjo.has("wins")) {
                    sw_wins = swjo.get("wins").getAsLong();
                }
            }
            if (pjo.get("stats").getAsJsonObject().has("Duels")) {
                JsonObject duel = pjo.get("stats").getAsJsonObject().get("Duels").getAsJsonObject();
                if (duel.has("losses")) {
                    de_losses = duel.get("losses").getAsLong();
                }
                if (duel.has("kills")) {
                    de_kills = duel.get("kills").getAsLong();
                }
                if (duel.has("wins")) {
                    de_wins = duel.get("wins").getAsLong();
                }
            }
        }
        if (pjo.has("networkExp")) {
            network_level = String.valueOf(getLevelByNetworkEXP(pjo.get("networkExp").getAsDouble()));
        }
        if (pjo.has("displayname")) {
            display = pjo.get("displayname").getAsString();
        }
    }

    public long getKarma() {
        return karma;
    }

    public String getRank() {
        return rank;
    }

    public String getLanguage() {
        return language;
    }

    public String getLevel() {
        return level;
    }

    public String getBw_final_death() {
        return bw_final_death;
    }

    public String getBw_final_killed() {
        return bw_final_killed;
    }

    public String getBw_win_games() {
        return bw_win_games;
    }

    public String getNetwork_level() {
        return network_level;
    }

    public String getBw_bed_broken() {
        return bw_bed_broken;
    }

    public String getBw_losses_games() {
        return bw_losses_games;
    }

    public String getBw_bed_lost() {
        return bw_bed_lost;
    }

    public long getBw_final_death_long() {
        return bw_final_death_long;
    }

    public long getBw_final_kill_long() {
        return bw_final_kill_long;
    }

    public long getBw_death_long() {
        return bw_death_long;
    }

    public long getBw_kill_long() {
        return bw_kill_long;
    }

    public String getBw_death() {
        return bw_death;
    }

    public long getBw_winstreak() {
        return bw_winstreak;
    }

    public String getBw_kill() {
        return bw_kill;
    }

    public long getBw_losses_long() {
        return bw_losses_long;
    }

    public long getGifted_rank() {
        return gifted_rank;
    }

    public long getBw_win_long() {
        return bw_win_long;
    }

    public String getDisplay() {
        return display;
    }

    public long getDe_kills() {
        return de_kills;
    }

    public long getDe_losses() {
        return de_losses;
    }

    public long getDe_wins() {
        return de_wins;
    }

    public long getSw_coins() {
        return sw_coins;
    }

    public long getSw_deaths() {
        return sw_deaths;
    }

    public long getSw_kills() {
        return sw_kills;
    }

    public long getSw_losses() {
        return sw_losses;
    }

    public String getSw_level() {
        return sw_level;
    }

    public long getSw_wins() {
        return sw_wins;
    }

    private long getLevelByNetworkEXP(double exp) {
        double REVERSE_PQ_PREFIX = -(10000 - 0.5 * 2500) / 2500;
        double REVERSE_CONST = REVERSE_PQ_PREFIX * REVERSE_PQ_PREFIX;
        double GROWTH_DIVIDES_2 = 2.0 / 2500.0;
        return exp < 0 ? 1 : (long) Math.floor(1 + REVERSE_PQ_PREFIX + Math.sqrt(REVERSE_CONST + GROWTH_DIVIDES_2 * exp));
    }

    public String getRankColor() {
        return rankColor;
    }

    public String getPlusColor() {
        return plusColor;
    }
}
