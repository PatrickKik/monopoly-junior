package nl.patrickkik.monjun;

import lombok.Data;
import lombok.Getter;
import nl.patrickkik.monjun.domain.Player;
import nl.patrickkik.monjun.domain.PlayerToken;

import java.util.HashMap;
import java.util.Map;

@Getter
public class GameRunnerStats {
    private Map<PlayerToken, Integer> playerToWinCount = new HashMap<>();

    public void addWin(Player player) {
        int wins = playerToWinCount.getOrDefault(player.getToken(), 0);
        playerToWinCount.put(player.getToken(), wins + 1);
    }
}
