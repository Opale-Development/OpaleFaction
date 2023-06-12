package fr.opaleuhc.opalefaction.shop.guis;

import fr.mrmicky.fastinv.FastInv;
import fr.opaleuhc.opalefaction.shop.ShopGUIManager;
import org.bukkit.entity.Player;

public class MainShopGUI extends FastInv {

    public MainShopGUI() {
        super(54, "§6§lBoutique");

        setItem(11, ShopGUIManager.BLOCKS, e -> {
            Player p = (Player) e.getWhoClicked();
            p.sendMessage("§cCette fonctionnalité n'est pas encore disponible.");
            //new BlocksShopGUI().open(e.getPlayer());
        });
    }
}
