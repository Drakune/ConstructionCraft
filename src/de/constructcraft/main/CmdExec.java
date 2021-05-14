package de.constructcraft.main;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import java.util.Objects;

public class CmdExec implements CommandExecutor {

    private final Main plugin;

    Location mapPlayerLocationPos1;
    Location mapPlayerLocationPos2;

    Block[][][] blocks;

    public String cmdGoto = "goto";
    public String cmdConstruct = "cc";

    public CmdExec(Main plugin) {
        this.plugin = plugin;
    }

    // TODO: Get Wooden Hoe with a command like the axe in WorldEdit.
    //     Use left and right-click for pos1 and pos.
    //     if you left click on block, do not destroy block => interrupting block_break
    //     The Hoe should be enchanted or something else so you can check if you have the ConstructCraft Hoe.

    //TODO: Plugin for cutting trees just like TreeCapitator

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if(sender instanceof Player) {
            if(cmd.getName().equalsIgnoreCase(cmdConstruct)) {
                if(args.length == 0) {
                    sender.sendMessage(Main.prefixWarning+"Du hast zu wenig Argumente angegeben!");
                    return false;
                }
                else if(args.length > 1) {
                    sender.sendMessage(Main.prefixWarning+"Du hast zu viele Argumente angegeben");
                    return false;
                }
                if(args[0].equalsIgnoreCase("pos1"))
                {
                    this.setLocation1((Player)sender);
                    return true;
                }
                if(args[0].equalsIgnoreCase("pos2"))
                {
                    this.setLocation2((Player)sender);
                    return true;
                }
                if(args[0].equalsIgnoreCase("getpositions"))
                {
                    this.getLocationSavedFromPlayer(((Player) sender));
                    return true;
                }
                if(args[0].equalsIgnoreCase("copy")) {
                    this.copyFromPos1ToPos2((Player)sender);
                    return true;
                }
                if(args[0].equalsIgnoreCase("paste")) {
                    this.pasteFromBlockLocationHashMap((Player)sender);
                    return true;
                }
                return true;
            }
            if(cmd.getName().equalsIgnoreCase(cmdGoto)) {
                this.teleportTo((Player)sender,args);
                return true;
            }
            return true;
        }
        return false;
    }

    public void setLocation1(Player sender) {
        this.mapPlayerLocationPos1 = new Location(sender.getWorld(),
                sender.getLocation().getBlockX(),
                sender.getLocation().getBlockY(),
                sender.getLocation().getBlockZ());
        sender.sendMessage(Main.prefixMessage+"Die erste Position wurde erfolgreich gespeichert!");
    }

    public void setLocation2(Player sender) {
        this.mapPlayerLocationPos2 = new Location(sender.getWorld(),
                sender.getLocation().getBlockX(),
                sender.getLocation().getBlockY(),
                sender.getLocation().getBlockZ());
        sender.sendMessage(Main.prefixMessage+"Die zweite Position wurde erfolgreich gespeichert!");
    }

    public Location getMapPlayerLocationPos1() {
        return this.mapPlayerLocationPos1;
    }

    public Location getMapPlayerLocationPos2() {
        return this.mapPlayerLocationPos2;
    }

    public void pasteFromBlockLocationHashMap(Player player) {
        if(this.blocks == null) {
            return;
        }

        Location locPlayer = player.getLocation();

        int xLoc = locPlayer.getBlockX();
        int yLoc = locPlayer.getBlockY();
        int zLoc = locPlayer.getBlockZ();

        for(int y = 0; y< blocks[1].length; y++) {
            yLoc++;
            for(int z = 0; z < blocks[2].length; z++) {
                zLoc++;
                for(int x = 0; x<blocks[0].length; x++) {
                    Objects.requireNonNull(locPlayer.getWorld()).getBlockAt(xLoc,yLoc,zLoc).setType(blocks[x][y][z].getType());
                    xLoc++;
                }
            }
        }

        player.sendMessage("Gebaeude gesetzt");
        player.teleport(new Location(player.getWorld(),locPlayer.getBlockX()+10,locPlayer.getBlockY(),locPlayer.getBlockZ()+10));

    }

    public void getLocationSavedFromPlayer(Player player) {
        //Position 1
        TextComponent pos1 = new TextComponent();
        Location pos1Loc;
        String xyzPos1 = "";

        try {
            pos1Loc = getMapPlayerLocationPos1();
            xyzPos1 = String.valueOf("X: "+pos1Loc.getBlockX())+ ", Y: " +String.valueOf(pos1Loc.getBlockY())+ ", Z: " +String.valueOf(pos1Loc.getBlockZ());
        }
        catch(NullPointerException x) {
            player.sendMessage(Main.prefixWarning+"Du hast keine erste Position gesetzt");
            return;
        }
        pos1.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,new Text("Teleportiere dich zur ersten Position")));
        pos1.setText(Main.prefixMessage+"Erste Position: "+xyzPos1);
        pos1.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/goto "+pos1Loc.getBlockX()+" "+pos1Loc.getBlockY()+" "+pos1Loc.getBlockZ()));

        //Position 2
        TextComponent pos2 = new TextComponent();
        Location pos2Loc = new Location(player.getWorld(),0,0,0);
        String xyzPos2 = "";
        try {
            pos2Loc = getMapPlayerLocationPos2();
            xyzPos2 = String.valueOf("X: "+pos2Loc.getBlockX())+ ", Y: " +String.valueOf(pos2Loc.getBlockY())+ ", Z: " +String.valueOf(pos2Loc.getBlockZ());
        }
        catch(NullPointerException x) {
            player.sendMessage(Main.prefixWarning+"Du hast keine zweite Position gesetzt");
            return;
        }
        pos2.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,new Text("Teleportiere dich zur zweiten Position")));
        pos2.setText(Main.prefixMessage+"Zweite Position: "+xyzPos2);
        pos2.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/goto "+pos2Loc.getBlockX()+" "+pos2Loc.getBlockY()+" "+pos2Loc.getBlockZ()));

        player.spigot().sendMessage(pos1);
        player.spigot().sendMessage(pos2);
    }

    public void copyFromPos1ToPos2(Player player) {

        plugin.getLogger().info("TEST");

        Location loc1 = getMapPlayerLocationPos1();
        Location loc2 = getMapPlayerLocationPos2();

        plugin.getLogger().info("TEST2");

        int x1 = 0;
        int x2 = 0;
        int y1 = 0;
        int y2 = 0;
        int z1 = 0;
        int z2 = 0;


        plugin.getLogger().info("TEST3");

        if (loc1.getBlockX() < loc2.getBlockX() || loc1.getBlockX() == loc2.getBlockX()) {
            x1 = loc1.getBlockX();
            x2 = loc2.getBlockX();
            player.sendMessage(x1 + " zu " + x2);
        } else if (loc1.getBlockX() > loc2.getBlockX()) {
            x1 = loc2.getBlockX();
            x2 = loc1.getBlockX();
            player.sendMessage(x1 + " zu " + x2);
        }
        if (loc1.getBlockY() < loc2.getBlockY() || loc1.getBlockY() == loc2.getBlockY()) {
            y1 = loc1.getBlockY();
            y2 = loc2.getBlockY();
            player.sendMessage(y1 + " zu " + y2);
        } else if (loc1.getBlockY() > loc2.getBlockY()) {
            y1 = loc2.getBlockY();
            y2 = loc1.getBlockY();
            player.sendMessage(y1 + " zu " + y2);
        }
        if (loc1.getBlockZ() < loc2.getBlockZ() || loc1.getBlockZ() == loc2.getBlockZ()) {
            z1 = loc1.getBlockZ();
            z2 = loc2.getBlockZ();
            player.sendMessage(z1 + " zu " + z2);
        } else if (loc1.getBlockZ() > loc2.getBlockZ()) {
            z1 = loc2.getBlockZ();
            z2 = loc1.getBlockZ();
            player.sendMessage(z1 + " zu " + z2);
        }

        x2+=1;
        z2+=1;
        y2+=1;

        plugin.getLogger().info("TEST4");

        int xArr = x2-x1;
        int yArr = y2-y1;
        int zArr = z2-z1;

        this.blocks = new Block[xArr][zArr][yArr];

        plugin.getLogger().info("TEST5");

        plugin.getLogger().info("X Array: " + xArr + " Y Array: " + yArr + " Z Array: " + zArr);

        Location locBlock;

        int ix = -1;
        int iy = -1;
        int iz = -1;

        try {
            for (int y = y1; y < y2; y++) {
                iy++;
                for (int z = z1; z < z2; z++) {
                    iz++;
                    for (int x = x1; x < x2; x++) {
                        ix++;
                        locBlock = new Location(player.getWorld(), x, y, z);
                        plugin.getLogger().info("X: " + x + " Y: " + y + " Z: " + z);
                        blocks[ix][iz][iy] = locBlock.getBlock();
                    }
                    ix = -1;
                }
                iz = -1;
            }
        }
        catch(Exception x) {
            plugin.getLogger().info("Exception: "+x.toString());
        }
    }

    public void teleportTo(Player sender,String[] args) {
        if(args.length  == 0) {
            sender.sendMessage(Main.prefixWarning+"Du hast zu wenig Argumente angegeben!");
        }
        else if(args.length == 1) {
            if(Bukkit.getPlayer(args[0]) != null) {
                Player target = Bukkit.getPlayer(args[0]);
                assert target != null;
                sender.teleport(target.getLocation());
                sender.sendMessage(Main.prefixMessage+"Du hast dich zu "+target.getName()+" teleportiert");
            }
            else {
                sender.sendMessage(Main.prefixWarning+"Der angegebene Spieler ("+args[0]+") ist nicht online!");
            }
        }
        else if(args.length == 2) {
            sender.sendMessage(Main.prefixWarning + "Es wurden zu wenig Argumente angegeben!");
        }
        else if(args.length == 3) {
            Location loc = new Location(Bukkit.getWorld("world"),Double.parseDouble(args[0]),Double.parseDouble(args[1]),Double.parseDouble(args[2]));
            sender.teleport(loc);
        }
        else {
            sender.sendMessage(Main.prefixWarning+"Du hast zu viele Argumente angegeben");
        }
    }



















}
