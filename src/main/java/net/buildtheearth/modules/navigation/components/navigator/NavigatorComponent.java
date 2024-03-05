package net.buildtheearth.modules.navigation.components.navigator;

import com.sk89q.worldedit.WorldEdit;
import net.buildtheearth.BuildTeamTools;
import net.buildtheearth.modules.Component;
import net.buildtheearth.modules.common.CommonModule;
import net.buildtheearth.modules.utils.ChatHelper;
import net.buildtheearth.modules.utils.Item;
import net.buildtheearth.modules.utils.io.ConfigPaths;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class NavigatorComponent extends Component {


    public NavigatorComponent() {
        super("Navigator");
    }

    @Override
    public void onEnable() {
        disableWorldEditNavigationWand();

        super.onEnable();
    }


    private void disableWorldEditNavigationWand(){
        // Check if WorldEdit is enabled
        if (!CommonModule.getInstance().getDependencyComponent().isWorldEditEnabled())
            return;

        ChatHelper.log("Navigation Wand: " + WorldEdit.getInstance().getConfiguration().navigationWand);

        // Set the navigation wand to nether star
        WorldEdit.getInstance().getConfiguration().navigationWand = 399;

        ChatHelper.log("Navigation Wand: " + WorldEdit.getInstance().getConfiguration().navigationWand);
    }

    /*
    Toggles the navigator on or off based on the current state
    */
    public void toggle(Player player) {
        Inventory inventory = player.getInventory();

        if(!inventory.contains(getItem())) {
            inventory.setItem(getSlot(), getItem());
            player.sendMessage(ChatHelper.successful("You turned the navigator %s.", "on"));
        } else {
            inventory.remove(getItem());
            player.sendMessage(ChatHelper.successful("You turned the navigator %s.", "off"));
        }
    }

    public ItemStack getItem() {
        return Item.create(Material.COMPASS, "§aNavigator");
    }

    public short getSlot() {
        return (short) BuildTeamTools.getInstance().getConfig().getInt(ConfigPaths.NAVIGATOR_ITEM_SLOT);
    }


}
