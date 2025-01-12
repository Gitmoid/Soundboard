package soundboard.SoundPlayer;

import java.io.BufferedInputStream;


public class SoundPlayer {
    private Thread currentPlayingThread = null;
    private MP3Player mp3Player = new MP3Player();

    public synchronized void playSound(String filePath) {
        if (currentPlayingThread != null && currentPlayingThread.isAlive()) {
            mp3Player.stop();
        }
        currentPlayingThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mp3Player.play(filePath);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        currentPlayingThread.start();
    }
}
