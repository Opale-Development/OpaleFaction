package fr.opaleuhc.opalefaction.scoreboard;

import fr.mrmicky.fastboard.FastBoard;
import fr.opaleuhc.opalefaction.OpaleFaction;
import fr.opaleuhc.opalefaction.dependencies.luckperms.LuckPermsAPI;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.util.*;

public class ScoreBoardManager {

    public static ScoreBoardManager INSTANCE;
    public Map<UUID, FastBoard> boards = new HashMap<>();
    public Map<UUID, Integer> numbers = new HashMap<>();

    public ScoreBoardManager() {
        INSTANCE = this;

        OpaleFaction.INSTANCE.getServer().getScheduler().runTaskTimer(OpaleFaction.INSTANCE, () -> {
            for (FastBoard board : this.boards.values()) {
                updateBoard(board);
            }
        }, 0, 20);
    }

    public String getDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("Europe/Paris"));
        return sdf.format(new Date());
    }

    public void setBoardNumber(UUID uuid, int number) {
        numbers.put(uuid, number);
    }

    public int getBoardNumber(UUID uuid) {
        return numbers.getOrDefault(uuid, 0);
    }

    public void updateBoard(FastBoard board) {
        Player player = board.getPlayer();

        final int number = getBoardNumber(player.getUniqueId());

        if (number == 0) {
            board.updateTitle("§3§lOpaleFaction");
            board.updateLines("§8" + OpaleFaction.VERSION,
                    "",
                    "§cErreur interne",
                    "§cCode 0 (quitté)",
                    "",
                    "§7" + getDate(),
                    "",
                    "§3mc.opaleuhc.fr"
            );
        } else if (number == 1) {
            board.updateTitle("§3§lOpaleFaction");
            board.updateLines("§8" + OpaleFaction.VERSION,
                    "",
                    "§3" + player.getName(),
                    "§f• Grade : §7" + LuckPermsAPI.INSTANCE.getPrefix(player.getUniqueId()),
                    "§f• Argent : " + "§7",
                    "",
                    "...",
                    "",
                    "§7" + getDate(),
                    "",
                    "§3mc.opaleuhc.fr"
            );
        }
    }
}
