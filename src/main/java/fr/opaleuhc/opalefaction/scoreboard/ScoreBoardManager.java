package fr.opaleuhc.opalefaction.scoreboard;

import fr.mrmicky.fastboard.FastBoard;
import fr.opaleuhc.opalefaction.OpaleFaction;
import fr.opaleuhc.opalefaction.dependencies.luckperms.LuckPermsAPI;
import fr.opaleuhc.opalefaction.faction.Faction;
import fr.opaleuhc.opalefaction.faction.FactionManager;
import fr.opaleuhc.opalefaction.utils.DateUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ScoreBoardManager {

    public static ScoreBoardManager INSTANCE;
    public Map<UUID, FastBoard> boards = new HashMap<>();
    public Map<UUID, Integer> numbers = new HashMap<>();

    public ScoreBoardManager() {
        INSTANCE = this;

        OpaleFaction.INSTANCE.getServer().getScheduler().runTaskTimerAsynchronously(OpaleFaction.INSTANCE, () -> {
            for (FastBoard board : this.boards.values()) {
                updateBoard(board);
            }
        }, 0, 20);
    }

    public String getTPS() {
        double tps = Bukkit.getTPS()[0];
        if (tps > 20.00) tps = 20.00;
        return String.format("%.2f", tps);
    }

    public void setBoardNumber(UUID uuid, int number) {
        numbers.put(uuid, number);
        updateBoard(boards.get(uuid));
    }

    public int getBoardNumber(UUID uuid) {
        return numbers.getOrDefault(uuid, 0);
    }

    public void updateBoard(FastBoard board) {
        Player player = board.getPlayer();

        final int number = getBoardNumber(player.getUniqueId());

        final String time = DateUtils.getTimeFormatted();

        if (number == 0) {
            board.updateTitle("§3§lOpaleFaction");
            board.updateLines("§8" + OpaleFaction.VERSION,
                    "",
                    "§cErreur interne",
                    "§cCode 0 (quitté)",
                    "",
                    "§7" + time + " §f| §7" + getTPS(),
                    "",
                    "§3mc.opaleuhc.fr"
            );
        } else if (number == 1) {
            board.updateTitle("§3§lOpaleFaction");
            Faction faction = FactionManager.INSTANCE.getFactionOf(player.getUniqueId());
            if (faction != null) {
                board.updateLines("§8" + OpaleFaction.VERSION,
                        "",
                        "§3" + player.getName(),
                        "§f• Grade : §7" + LuckPermsAPI.INSTANCE.getPrefix(player.getUniqueId()),
                        "§f• Argent : " + "§a???$",
                        "",
                        "§6" + faction.getName(),
                        "§f• Membres : §a" + faction.getOnlineMembers() + "§7/§e" + faction.getMembers().size(),
                        "§f• Banque : §a???$",
                        "§f• Points : §a???",
                        "§f• Classement : §a???",
                        "",
                        "§7" + time + " §f| §7" + getTPS(),
                        "",
                        "§3mc.opaleuhc.fr"
                );
                return;
            }
            board.updateLines("§8" + OpaleFaction.VERSION,
                    "",
                    "§3" + player.getName(),
                    "§f• Grade : §7" + LuckPermsAPI.INSTANCE.getPrefix(player.getUniqueId()),
                    "§f• Argent : " + "§7",
                    "",
                    "...",
                    "",
                    "§7" + time + " §f| §7" + getTPS(),
                    "",
                    "§3mc.opaleuhc.fr"
            );
        }
    }
}
