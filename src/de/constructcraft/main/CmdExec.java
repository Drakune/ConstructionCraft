package de.constructcraft.main;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import de.constructcraft.commands.*;

public class CmdExec implements CommandExecutor {

    private Main plugin;

    public String cmdGoto = "goto";

    public CmdExec(Main plugin) {

    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if(sender instanceof Player) {
            if(cmd.getName().equalsIgnoreCase("goto")) {
                new GoTo((Player)sender,args);
            }
        }
        return false;
    }
}
