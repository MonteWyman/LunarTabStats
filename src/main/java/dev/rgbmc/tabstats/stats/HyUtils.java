package dev.rgbmc.tabstats.stats;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.rgbmc.tabstats.TabStats;
import dev.rgbmc.tabstats.exceptions.UnknowPlayerException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class HyUtils {
    private static final List<String> bots = new ArrayList<>();
    private JsonObject json;

    public HyUtils() {
        try {
            URL url = new URL("https://api.hypixel.net/key?key=" + TabStats.API_KEY);
            StringBuilder sb = new StringBuilder();
            URLConnection connection = url.openConnection(TabStats.proxy);
            connection.setConnectTimeout(2000);
            BufferedReader reader = null;
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
            String str;
            while ((str = reader.readLine()) != null) {
                sb.append(str);
                sb.append("\n");
            }
            sb.delete(sb.length() - 1, sb.length());
            JsonObject jo = new JsonParser().parse(sb.toString()).getAsJsonObject().get("record").getAsJsonObject();
            json = jo;
        } catch (Exception e) {
        }
    }

    public static boolean isRealPlayer(String name) {
        if (bots.contains(name)) return false;
        try {
            URL url = new URL("https://api.minecraftservices.com/users/profiles/minecraft/" + name);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection(TabStats.proxy);
            connection.setConnectTimeout(2000);
            connection.connect();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                return true;
            } else if (connection.getResponseCode() == 429) {
                return false;
            }
        } catch (IOException e) {
            return false;
        }
        bots.add(name);
        return false;
    }

    public static boolean check(String n) {
        if (n.isEmpty()) {
            return true;
        }
        if (n.length() == 10) {
            int num = 0;
            int let = 0;
            char[] var4 = n.toCharArray();
            for (char c : var4) {
                if (Character.isLetter(c)) {
                    if (Character.isUpperCase(c)) {
                        return false;
                    }
                    ++let;
                } else {
                    if (!Character.isDigit(c)) {
                        return false;
                    }
                    ++num;
                }
            }
            return num >= 2 && let >= 2;
        }

        return false;
    }

    public static String request(String link) throws Exception {
        URL url = new URL(link);
        StringBuilder sb = new StringBuilder();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection(TabStats.proxy);
        connection.setConnectTimeout(2000);
        connection.connect();
        if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) throw new UnknowPlayerException();
        BufferedReader reader = null;
        reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
        String str;
        while ((str = reader.readLine()) != null) {
            sb.append(str);
            sb.append("\n");
        }
        sb.delete(sb.length() - 1, sb.length());
        return sb.toString();
    }

    public static InputStream requestIO(String link) throws Exception {
        URL url = new URL(link);
        URLConnection connection = url.openConnection(TabStats.proxy);
        connection.setConnectTimeout(2000);
        connection.connect();
        return connection.getInputStream();
    }

    public static String uncolor(String text) {
        return text.replaceAll("[§]", "&").replaceAll("(&+)([0-9a-fA-F])", "").replaceAll("(&+)([k-orK-OR])", "");
    }

    protected String getMinRequestTotal() {
        if (json == null) return "获取失败";
        return json.get("queriesInPastMin").getAsString();
    }

    protected String getRequestTotal() {
        if (json == null) return "获取失败";
        return json.get("totalQueries").getAsString();
    }
}
