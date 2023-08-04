package nl.patrickkik.monjun;

import java.util.BitSet;

public class Main {
    static {
        // must set before the Logger
        String path = Main.class.getClassLoader()
                .getResource("logging.properties").getFile();
        System.setProperty("java.util.logging.config.file", path);
    }
    public static void main(String[] args) {
        new GameRunner().run();
    }
}