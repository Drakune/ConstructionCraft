package de.constructcraft.main;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import java.util.HashMap;

public class CmdExec implements CommandExecutor {

    private Main plugin;

    HashMap<Player,Location> MapPlayerLocationPos1 = new HashMap<Player,Location>();
    HashMap<Player,Location> MapPlayerLocationPos2 = new HashMap<Player,Location>();

    public String cmdGoto = "goto";
    public String cmdConstruct = "cc";

    public CmdExec(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if(sender instanceof Player) {
            if(cmd.getName().equalsIgnoreCase("cc")) {
                if(args[0].equalsIgnoreCase("pos1"))
                {
                    this.setLocation1((Player)sender);
                }
                if(args[0].equalsIgnoreCase("pos2"))
                {
                    setLocation2((Player)sender);
                }
                if(args[0].equalsIgnoreCase("getpositions"))
                {
                    getLocationSavedFromPlayer(((Player) sender));
                }

            }
            if(cmd.getName().equalsIgnoreCase("goto")) {
                teleportTo((Player)sender,args);
            }

        }
        return false;
    }
    public void setLocation1(Player sender) {
        Location loc1 = new Location(sender.getWorld(),
                sender.getLocation().getBlockX(),
                sender.getLocation().getBlockY(),
                sender.getLocation().getBlockZ());
        MapPlayerLocationPos1.put(sender,loc1);
        sender.sendMessage(Main.prefixMessage+"Die erste Position wurde erfolgreich gespeichert!");
    }

    public void setLocation2(Player sender) {
        Location loc2 = new Location(sender.getWorld(),
                sender.getLocation().getBlockX(),
                sender.getLocation().getBlockY(),
                sender.getLocation().getBlockZ());
        MapPlayerLocationPos2.put(sender,loc2);
        sender.sendMessage(Main.prefixMessage+"Die zweite Position wurde erfolgreich gespeichert!");
    }

    public HashMap<Player,Location> getMapPlayerLocationPos1() {
        return this.MapPlayerLocationPos1;
    }

    public HashMap<Player,Location> getMapPlayerLocationPos2() {
        return this.MapPlayerLocationPos2;
    }

    public void getLocationSavedFromPlayer(Player player) {
        //Position 1
        TextComponent pos1 = new TextComponent();
        Location pos1Loc;
        String xyzPos1 = "";

        try {
            pos1Loc = getMapPlayerLocationPos1().get(player);
            xyzPos1 = String.valueOf("X: "+pos1Loc.getBlockX())+ ", Y: " +String.valueOf(pos1Loc.getBlockY())+ ", Z: " +String.valueOf(pos1Loc.getBlockZ());
        }
        catch(NullPointerException x) {
            player.sendMessage(Main.prefixWarning+"Du hast keine erste Position gesetzt");
            return;
        }
        pos1.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,new ComponentBuilder("Teleportiere dich zur ersten Position").create()));
        pos1.setText(Main.prefixMessage+"Erste Position: "+xyzPos1);
        pos1.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/goto "+pos1Loc.getBlockX()+" "+pos1Loc.getBlockY()+" "+pos1Loc.getBlockZ()));

        //Position 2
        TextComponent pos2 = new TextComponent();
        Location pos2Loc = new Location(player.getWorld(),0,0,0);
        String xyzPos2 = "";
        try {
            pos2Loc = getMapPlayerLocationPos2().get(player);
            xyzPos2 = String.valueOf("X: "+pos2Loc.getBlockX())+ ", Y: " +String.valueOf(pos2Loc.getBlockY())+ ", Z: " +String.valueOf(pos2Loc.getBlockZ());
        }
        catch(NullPointerException x) {
            player.sendMessage(Main.prefixWarning+"Du hast keine zweite Position gesetzt");
            return;
        }
        pos2.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,new ComponentBuilder("Teleportiere dich zur zweiten Position").create()));
        pos2.setText(Main.prefixMessage+"Zweite Position: "+xyzPos2);
        pos2.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/goto "+pos2Loc.getBlockX()+" "+pos2Loc.getBlockY()+" "+pos2Loc.getBlockZ()));

        player.spigot().sendMessage(pos1);
        player.spigot().sendMessage(pos2);
    }

    public void teleportTo(Player sender,String[] args) {
        if(args.length  == 0) {
            sender.sendMessage(Main.prefixWarning+"Du hast zu wenig Argumente angegeben!");
            return;
        }
        if(args.length == 1) {
            if(Bukkit.getPlayer(args[0]) != null) {
                Player target = Bukkit.getPlayer(args[0]);
                sender.teleport(target.getLocation());
                sender.sendMessage(Main.prefixMessage+"Du hast dich zu "+target.getName()+" teleportiert");
                return;
            }
            else {
                sender.sendMessage(Main.prefixWarning+"Der angegebene Spieler ("+args[0]+") ist nicht online!");
                return;
            }
        }
        else if(args.length == 2) {
            sender.sendMessage(Main.prefixWarning + "Es wurden zu wenig Argumente angegeben!");
            return;
        }
        else if(args.length == 3) {
            Location loc = new Location(Bukkit.getWorld("world"),Double.parseDouble(args[0]),Double.parseDouble(args[1]),Double.parseDouble(args[2]));
            sender.teleport(loc);
            return;
        }
    }
}
