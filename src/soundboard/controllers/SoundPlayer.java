package soundboard.controllers;

import javax.swing.SwingWorker;

public class SoundPlayer {
    private SwingWorker<Void, Void> currentPlayingWorker = null;
    private final MP3Player mp3Player = new MP3Player();

    public synchronized void playSound(String filePath) {
        stopSound();
        currentPlayingWorker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
                mp3Player.play(filePath);
                return null;
            }
        };

        currentPlayingWorker.execute();
    }

    public void stopSound() {
        if (currentPlayingWorker != null && !currentPlayingWorker.isDone()) {
            mp3Player.stop();
        }
    }
}