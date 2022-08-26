
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.Locale;

public class CommandListener implements Listener {
    WorldGuardTrade plugin;

    public CommandListener(WorldGuardTrade plugin) {
        this.plugin = plugin;
    }



    @EventHandler
    public void TradeCommand(PlayerCommandPreprocessEvent event) {
        // Get the player.
        Player player = event.getPlayer();
        if(event.getMessage().toLowerCase(Locale.ROOT).contains("/trade")||event.getMessage().toLowerCase(Locale.ROOT).contains("/tradesystem:trade")) {

            validateAction(player, event);
        }
    }

    public void validateAction(Player player, Cancellable event) {
        // Convert properties into WorldGuard compatible objects.
        BlockVector3 loc = BukkitAdapter.asBlockVector(player.getLocation());
        World world = BukkitAdapter.adapt(player.getWorld());
        LocalPlayer wgPlayer = WorldGuardPlugin.inst().wrapPlayer(player);

        // Get the regions.
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionManager regions = container.get(world);

        // If there are no regions don't bother.
        if (regions == null) {
            return;
        }

        // Get the regions the player is in.
        ApplicableRegionSet set = regions.getApplicableRegions(loc);

        // Check the flag.
        if (!set.testState(wgPlayer, WorldGuardTrade.getTradeCommandAllowedFlag())) {
            // Cancel Town creation
            player.sendMessage(plugin.getDenyCommandMsg());
            event.setCancelled(true);
        }
    }
}
