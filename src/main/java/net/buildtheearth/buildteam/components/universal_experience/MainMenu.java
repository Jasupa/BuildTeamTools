package net.buildtheearth.buildteam.components.universal_experience;

import net.buildtheearth.Main;
import net.buildtheearth.utils.Item;
import net.buildtheearth.utils.MenuItem;
import net.buildtheearth.utils.Utils;
import net.buildtheearth.utils.menus.AbstractMenu;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.ipvp.canvas.mask.Mask;

import java.util.ArrayList;

/**
 * The main menu for the BTE universal navigator. <p>
 * <p>
 * <p> Accessed from here is an explore menu, a build menu (plot system and tools) and a tutorials menu
 * All of these 3 icons can be enabled and disabled <p>
 * <p>
 * <p> Also in Main Menu is an option to toggle whether the navigator item is in the hotbar. A player can always use /navigator to open
 * the navigator <p>
 * <p>
 * <p> The menu has 3 rows. The centre row is occupied with Build, Explore and Tutorials (if enabled), and the last row holds the navigator hide option <p>
 *
 */
public class MainMenu extends AbstractMenu
{
    private static final int iRows = 3;
    private static final String szInventoryName = "Build the Earth";
    private static final ArrayList<MenuItem> menuItems = getGui();
    private static final FileConfiguration config = Main.instance.getConfig();

    public MainMenu(Player menuPlayer)
    {
        super(iRows, szInventoryName, menuPlayer);
    }

    /**
     * Produces a list of Menu Items for the MainMenu gui
     * @see MenuItem
     * @return
     */
    public static ArrayList<MenuItem> getGui()
    {
        //Initiates the list
        ArrayList<MenuItem> menuItems = new ArrayList<>();

        //Gets the list of items needed in the GUI
        int iItemsNeeded = 0;
        boolean[] bItemsNeeded = new boolean[3];
        int[] iSlotsToBeUsed;

        bItemsNeeded[0] = config.getBoolean("navigator.main_menu_items.build.enabled");
        if (bItemsNeeded[0])
            iItemsNeeded++;

        bItemsNeeded[1] = config.getBoolean("navigator.build_menu_items.explore.enabled");
        if (bItemsNeeded[1])
            iItemsNeeded++;

        bItemsNeeded[2] = config.getBoolean("navigator.build_menu_items.tutorials.enabled");
        if (bItemsNeeded[2])
            iItemsNeeded++;

        //Deals with the number of items needed
        if (iItemsNeeded == 0)
            return menuItems;
        else
            iSlotsToBeUsed = MenuItem.getSlotIndexesMiddleRowOf3(iItemsNeeded);

        //-----------------------------------------------------
        //--------------Adds the items to the GUI--------------
        //-----------------------------------------------------
        int iItem = 0;

        //-------------------------------------------
        //-------------------Build-------------------
        //-------------------------------------------
        if (bItemsNeeded[0])
        {
            ArrayList<String> buildLore = new ArrayList<>();
            buildLore.add(Utils.loreText("Click to access the build menu !"));

            ItemStack buildItem = Item.create(Material.getMaterial(config.getString("navigator.main_menu_items.build.material")), ChatColor.GREEN +"" +ChatColor.BOLD +"Build", 1, buildLore);
            MenuItem build = new MenuItem(iSlotsToBeUsed[iItem], buildItem, player ->
            {
                //Opens the build menu for the player
                new BuildMenu(player);
            });
            menuItems.add(build);

            iItem++;
        }

        //-------------------------------------------
        //------------------Explore------------------
        //-------------------------------------------
        if (bItemsNeeded[1])
        {
            ArrayList<String> exploreLore = new ArrayList<>();
            exploreLore.add(Utils.loreText("Click to explore the earth !"));

            ItemStack exploreItem = Item.create(Material.getMaterial(config.getString("navigator.main_menu_items.explore.material")), ChatColor.YELLOW +"" +ChatColor.BOLD +"Explore", 1, exploreLore);
            MenuItem explore = new MenuItem(iSlotsToBeUsed[iItem], exploreItem, player ->
            {
                //Opens the explore menu for the player
                new ExploreMenu(player);
            });
            menuItems.add(explore);

            iItem++;
        }

        //-------------------------------------------
        //-----------------Tutorials-----------------
        //-------------------------------------------
        if (bItemsNeeded[2])
        {
            ArrayList<String> tutorialsLore = new ArrayList<>();
            tutorialsLore.add(Utils.loreText("Click to do some tutorials !"));

            ItemStack tutorialsItem = Item.create(Material.getMaterial(config.getString("navigator.main_menu_items.tutorials.material")), ChatColor.AQUA +"" +ChatColor.BOLD +"Tutorials", 1, tutorialsLore);
            MenuItem tutorials = new MenuItem(iSlotsToBeUsed[iItem], tutorialsItem, player ->
            {
                //Open New tutorials menu
            });
            menuItems.add(tutorials);
        }

        //--------------------------------------------
        //-----------------Toggle nav-----------------
        //--------------------------------------------
        ArrayList<String> navLore = new ArrayList<>();
        navLore.add(Utils.loreText("Click to toggle the navigator in your inventory"));
        navLore.add(Utils.loreText("You can always open the navigator with /navigator"));

        ItemStack navToggleItem = Item.create(Material.KNOWLEDGE_BOOK, ChatColor.AQUA +"" +ChatColor.BOLD +"Tutorials", 1, navLore);
        MenuItem toggleNav = new MenuItem(18, navToggleItem, player ->
        {
            Main.buildTeamTools.updatePreference(PreferenceType.NavigatorEnabled, player.getUniqueId());
        });
        menuItems.add(toggleNav);

        return menuItems;
    }

    @Override
    protected void setMenuItemsAsync()
    {
        setMenuItemsAsyncViaMenuItems(menuItems);
    }

    @Override
    protected void setItemClickEventsAsync()
    {
        setMenuItemClickEventsAsyncViaMenuItems(menuItems);
    }

    @Override
    protected Mask getMask()
    {
        return null;
    }
}
