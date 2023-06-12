package fr.opaleuhc.opalefaction.shop;

import fr.opaleuhc.opalefaction.OpaleFaction;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ShopGUIManager {

    public static ShopGUIManager INSTANCE;
    public static ItemStack BLOCKS = null;
    private final OpaleFaction plugin;

    public ShopGUIManager(OpaleFaction plugin) {
        INSTANCE = this;
        this.plugin = plugin;

        plugin.getLogger().info("Registering Shops and Items...");

        BLOCKS = new ItemStack(Material.STONE);
        BLOCKS.setDisplayName("§6§lBlocs");
    }
}
