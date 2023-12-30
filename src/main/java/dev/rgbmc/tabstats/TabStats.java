package dev.rgbmc.tabstats;

import dev.rgbmc.tabstats.command.CleanCacheCommand;
import dev.rgbmc.tabstats.command.OverlayCommand;
import net.weavemc.loader.api.ModInitializer;
import net.weavemc.loader.api.command.CommandBus;

import java.net.*;
import java.util.List;

public class TabStats implements ModInitializer {
    public static String API_KEY = "7a33d22f-2b5c-47ba-a7d0-79799bcc6ae1" // Integrated API-Key
    public static Proxy proxy = Proxy.NO_PROXY;

    @Override
    public void preInit() {
        System.out.println("Initializing TabStats...");
        System.out.println("Getting Proxy Status...");
        if (System.getProperty("tabstats.proxy", "false").equalsIgnoreCase("true")) {
            System.setProperty("java.net.useSystemProxies", "true");
            System.out.println("Detecting proxies...");
            List<Proxy> l = null;
            try {
                l = ProxySelector.getDefault().select(new URI("https://www.baidu.com"));
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            if (l != null) {
                for (Proxy o : l) {
                    System.out.println("[TabStats] Proxy Type: " + o.type());

                    InetSocketAddress addr = (InetSocketAddress) o.address();

                    if (addr == null) {
                        System.out.println("[TabStats] Proxy feature disabled in system setting");
                    } else {
                        System.out.println("[TabStats] Hostname: " + addr.getHostName());
                        System.out.println("[TabStats] Port: " + addr.getPort());
                        proxy = o;
                        break;
                    }
                }
            }
            System.setProperty("java.net.useSystemProxies", "false");
        }

        CommandBus.register(new OverlayCommand());
        CommandBus.register(new CleanCacheCommand());
    }
}
