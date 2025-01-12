package soundboard.SoundPlayer;

import java.io.FileInputStream;

public class MP3Player {
    private javazoom.jl.player.Player player;

    public void play(String filePath) {
        try (FileInputStream fis = new FileInputStream(filePath)) {
            player = new javazoom.jl.player.Player(fis);
            player.play();
        } catch (Exception e) {
            System.out.println("Error playing MP3 file: " + e.getMessage());
        }
    }

    public void stop() {
        player.close();
    }
}
