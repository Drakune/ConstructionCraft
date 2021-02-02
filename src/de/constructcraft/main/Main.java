package de.constructcraft.main;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {


    public static String prefixMessage;
    public static String prefixWarning;

    public Main() {
        this.prefixMessage = new String(ChatColor.BOLD + "" + ChatColor.DARK_PURPLE + "[DR4] " + ChatColor.YELLOW);
        this.prefixWarning = new String(ChatColor.BOLD + "" + ChatColor.DARK_PURPLE + "[DR4] " + ChatColor.RED + ChatColor.ITALIC);
    }

    @Override
    public void onEnable() {
        this.getLogger().info("ConstructCraft has been loaded Successfully");
        this.getLogger().info("Plugin made by"+"\r\n" +
                "    ____  ____  __ __  __ ____  ___   _______\r\n" +
                "   / __ \\/ __ \\/ // / / //_/ / / / | / /__  /\r\n" +
                "  / / / / /_/ / // /_/ ,< / / / /  |/ / /_ < \r\n" +
                " / /_/ / _, _/__  __/ /| / /_/ / /|  /___/ / \r\n" +
                "/_____/_/ |_|  /_/ /_/ |_\\____/_/ |_//____/  \r\n" +
                "                                             \r\n" +
                "");

        CmdExec executor = new CmdExec(this);
        getCommand(executor.cmdGoto).setExecutor(executor);
        getCommand(executor.cmdConstruct).setExecutor(executor);

        this.getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        System.out.println("ConstructCraft has been shut down successfully");
    }
}
