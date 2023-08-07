package nl.patrickkik.monjun;

import nl.patrickkik.monjun.domain.*;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

public class GameRunner {
    public void run() {
        int numberOfGames = 1_000;
        GameRunnerStats gameRunnerStats = new GameRunnerStats();

        long begin = System.currentTimeMillis();

        IntStream.range(0, numberOfGames)
                .peek(nr -> {
                    if (nr % 100 == 0) {
                        System.out.println(nr);
                    }
                })
                .mapToObj(nr -> {
                    List<Player> players = List.of(
                            new Player(PlayerToken.CAR, Strategy.SAFE),
                            new Player(PlayerToken.BOAT, Strategy.BUY),
                            new Player(PlayerToken.CAT, Strategy.BUY),
                            new Player(PlayerToken.DOG, Strategy.BUY)
                    );
                    Game game = new Game(nr);
                    return game.start(players);
                })
                .forEach(gameStats -> {
                    gameRunnerStats.addWin(gameStats.getWinner());
                });
        long end = System.currentTimeMillis();

        System.out.printf("Simulatie van %d spellen duurde %d ms.%n", numberOfGames, end - begin);

        gameRunnerStats.getPlayerToWinCount()
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .forEach(entry -> System.out.println("Speler %s heeft %d keer gewonnen.".formatted(entry.getKey(), entry.getValue())));
    }

    public static double calculateStandardDeviation(List<Integer> array) {

        // get the sum of array
        double sum = 0.0;
        for (double i : array) {
            sum += i;
        }
        System.out.println("sum: " + sum);

        // get the mean of array
        int length = array.size();
        double mean = sum / length;
        System.out.println("mean: " + mean);

        int median = array.get(length / 2);
        System.out.println("median: " + median);

        // calculate the standard deviation
        double standardDeviation = 0.0;
        for (double num : array) {
            standardDeviation += Math.pow(num - mean, 2);
        }
        standardDeviation = Math.sqrt(standardDeviation / length);
        System.out.println("standardDeviation: " + standardDeviation);

        return standardDeviation;
    }
}
