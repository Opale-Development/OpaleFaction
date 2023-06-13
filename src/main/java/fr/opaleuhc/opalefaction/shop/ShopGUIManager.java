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

    public void convert_from_esgui() {
        /*File file = new File(plugin.getDataFolder(), "shops.yml");
        if (!file.exists()) {
            plugin.getLogger().warning("File shops.yml not found, creating it...");
            plugin.saveResource("shops.yml", false);
        }

        FileConfiguration config = YamlConfiguration.loadConfiguration(file);

        double it = 0;

        HashMap<String, Double> buy = new HashMap<>();
        HashMap<String, Double> sell = new HashMap<>();
        for (String categorie : config.getConfigurationSection("").getKeys(false)) {
            for (String item : config.getConfigurationSection(categorie).getKeys(false)) {
                buy.put(config.getString(categorie + "." + item + ".material"), config.getDouble(categorie + "." + item + ".buy"));
                sell.put(config.getString(categorie + "." + item + ".material"), config.getDouble(categorie + "." + item + ".sell"));
            }
            config.set(categorie, null);
            for (String item : buy.keySet()) {
                config.set(categorie + "." + item + ".buy", buy.get(item));
                config.set(categorie + "." + item + ".sell", sell.get(item));
            }
            buy.clear();
            sell.clear();
            it++;
            if (it > 500_000) {
                System.out.println("Problem with shop.yml, too many items");
                break;
            }
        }

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

    @Deprecated
    public void gen_brut() {
        /*
        File dir = new File(OpaleFaction.INSTANCE.getDataFolder() + "");
        if (!dir.exists()) {
            dir.mkdir();
        }
        File file = new File(OpaleFaction.INSTANCE.getDataFolder(), "shops.yml");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        FileConfiguration config = new YamlConfiguration();

        double it = 0;

        for (Material mat : Material.values()) {
            String t = mat.isFuel() ? "fuel" : mat.isEdible() ? "food" : mat.isArmor() ? "armor" : mat.isRecord() ?
                    "music" : mat.isItem() ? "items" : mat.isBlock() ? "blocks" : "other";
            config.set(t + "." + mat.name() + ".buy", 10);
            config.set(t + "." + mat.name() + ".sell", 5);
            if (it % 500 == 0) {
                System.out.println("Loaded " + it + " items");
                try {
                    config.save(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            it++;
        }

        System.out.println("Saving shop.yml...");

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Saved shop.yml");*/
    }
}
