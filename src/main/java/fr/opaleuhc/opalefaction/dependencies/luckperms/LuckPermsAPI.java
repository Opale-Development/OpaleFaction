package fr.opaleuhc.opalefaction.dependencies.luckperms;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.user.User;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.util.UUID;

public class LuckPermsAPI {

    public static LuckPermsAPI INSTANCE;
    private final net.luckperms.api.LuckPerms luckPermsAPI;

    public LuckPermsAPI() {
        INSTANCE = this;

        RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(net.luckperms.api.LuckPerms.class);
        assert provider != null;
        luckPermsAPI = provider.getProvider();
        System.out.println("LuckPermsAPI loaded.");
    }

    public String getPrefix(UUID uuid) {
        User user = luckPermsAPI.getUserManager().getUser(uuid);
        String prefix = user != null ? user.getCachedData().getMetaData().getPrefix() : "";
        return prefix != null ? prefix.replace("&", "§") : "";
    }

    public String getSuffix(UUID uuid) {
        User user = luckPermsAPI.getUserManager().getUser(uuid);
        String suffix = user != null ? user.getCachedData().getMetaData().getSuffix() : "";
        return suffix != null ? suffix.replace("&", "§") : "";
    }

}
