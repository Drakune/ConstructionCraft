package de.constructcraft.commands;

import de.constructcraft.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class GoTo {

    public GoTo(Player player,String[] args) {
        if(args.length == 1) {
            if(Bukkit.getPlayer(args[0]) != null) {
                Player target = Bukkit.getPlayer(args[0]);
                player.teleport(target.getLocation());
                player.sendMessage(Main.prefixMessage+"Du hast dich zu "+target.getName()+" teleportiert");
                return;
            }
            return;
        }
        else if(args.length < 2) {
            player.sendMessage(Main.prefixWarning + "Es wurden zu wenig Argumente angegeben!");
            return;
        }
        Location loc = new Location(Bukkit.getWorld("world"),Double.parseDouble(args[0]),Double.parseDouble(args[1]),Double.parseDouble(args[2]));
        player.teleport(loc);
    }
}
