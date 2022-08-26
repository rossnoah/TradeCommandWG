import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;

import java.util.Objects;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class WorldGuardTrade extends JavaPlugin {

    private static StateFlag TRADE_COMMAND_ALLOWED_FLAG;
    public static WorldGuardTrade instance;
    private String denyCommandMsg;

    public static StateFlag getTradeCommandAllowedFlag() {
        return TRADE_COMMAND_ALLOWED_FLAG;
    }

    @Override
    public void onEnable() {
        instance = this;
        this.getServer().getPluginManager().registerEvents(new CommandListener(this), this);

        // Config Initialisation
        getConfig().options().copyDefaults();
        saveDefaultConfig();
        setDenyCommandMsg();
    }

    @Override
    public void onLoad() {
        FlagRegistry registry = WorldGuard.getInstance().getFlagRegistry();

        // Register Flag
        try {
            // Create the town creation flag.
                StateFlag flag = new StateFlag("trade-command", false);
            registry.register(flag);
            TRADE_COMMAND_ALLOWED_FLAG = flag;
        } catch (FlagConflictException e) {
            Flag<?> existing = registry.get("trade-command");

            // Happens if a flag with the same name exists.
            if (existing instanceof StateFlag) {
                TRADE_COMMAND_ALLOWED_FLAG = (StateFlag) existing;
                getLogger().warning("Found WorldGuard Flag with that matches, overriding it.");
            } else {
                getLogger().warning("Trade WorldGuard Flag could not be created:\n" + e.getMessage());
            }
        }
    }

    public static WorldGuardTrade getInstance() {
        return instance;
    }

	public String getDenyCommandMsg() {
		return denyCommandMsg;
	}

	public void setDenyCommandMsg() {
		this.denyCommandMsg = ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(getConfig().getString("language.deny-command")));
	}
}
