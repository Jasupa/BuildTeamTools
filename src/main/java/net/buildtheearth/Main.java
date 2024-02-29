package net.buildtheearth;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import net.buildtheearth.modules.tpll.TpllModule;
import net.buildtheearth.modules.updater.UpdateChecker;
import net.buildtheearth.modules.utils.ChatHelper;
import net.buildtheearth.modules.utils.io.ConfigUtil;
import net.buildtheearth.modules.warp.WarpModule;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.util.List;
import java.util.UUID;


public class Main extends JavaPlugin implements PluginMessageListener {

    public static Main instance;

    /**
     * Handles all of the plugin's features
     */
    public static BuildTeamTools buildTeamTools;

    public static BuildTeamTools getBuildTeamTools() {
        return buildTeamTools;
    }

    public static void setBuildTeamTools(BuildTeamTools buildTeamTools) {
        Main.buildTeamTools = buildTeamTools;
    }

    @Override
    public void onEnable() {
        instance = this;

        buildTeamTools = new BuildTeamTools();
        boolean successful = buildTeamTools.start();

        if (!successful) return;

        String resultMessage = UpdateChecker.start(this.getFile());
        ChatHelper.logDebug("Plugin with version %s started. %s", getDescription().getVersion(), resultMessage);
    }

    @Override
    public void onDisable() {
        buildTeamTools.stop();
        getLogger().info(ChatHelper.error("Plugin stopped."));
    }

    // Methods for managing the configuration file
    @Override
    public FileConfiguration getConfig() {
        return ConfigUtil.getInstance().configs[0];
    }

    @Override
    public void reloadConfig() {
        ConfigUtil.getInstance().reloadFiles();
    }

    // Getters & Setters

    @Override
    public void saveConfig() {
        ConfigUtil.getInstance().saveFiles();
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (channel.equals("BuildTeam")) {

            // If the player is not on the list of people communicating with the network, add his uuid to that list
            List<UUID> communicators = Main.getBuildTeamTools().getProxyModule().getCommunicators();
            if (!communicators.contains(player.getUniqueId())) {
                communicators.add(player.getUniqueId());
            }

            // Read the incoming data
            ByteArrayDataInput in = ByteStreams.newDataInput(message);
            String subChannel = in.readUTF();

            ChatHelper.logDebug("Plugin Message received: %s from Player: %s", subChannel, player.getName());

            if (subChannel.equalsIgnoreCase("Ping")) {
                // Do something? not sure what tbh
            }

            // Reset the stats cache
            if (subChannel.equalsIgnoreCase("Stats")) {
                String status = in.readUTF();
                if (status.equals("OK")) {
                    buildTeamTools.getStatsModule().resetCache();
                }
            }

            // Add a new universal tpll target to the queue
            if (subChannel.equals("Tpll")) {
                TpllModule.addTpllToQueue(in, player);
            }

            // Add a new universal warp target to the queue
            if (subChannel.equals("UniversalWarps")) {
                WarpModule.addWarpToQueue(in, player);
            }
        }
    }
}
