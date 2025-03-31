package soundboard.services.impl;

import soundboard.services.MP3Player;

import javax.swing.SwingWorker;

public class SoundPlayerImpl {
    private SwingWorker<Void, Void> currentPlayingWorker = null;
    private final MP3Player mp3Player;

    public SoundPlayerImpl() {
        this.mp3Player = new MP3Player();
    }

    public synchronized void playSound(String filePath) {
        int playbackDelayInMilliseconds = 500;
        stopSound();
        currentPlayingWorker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
                try {
                    Thread.sleep(playbackDelayInMilliseconds);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
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