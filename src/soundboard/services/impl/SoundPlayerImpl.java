package soundboard.services.impl;

import soundboard.services.MP3Player;

import javax.swing.SwingWorker;

public class SoundPlayerImpl {
    private final MP3Player mp3Player;

    private SwingWorker<Void, Void> currentPlayingWorker = null;
    private boolean isPlaying = false;

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
                    Thread.currentThread().interrupt();
                    return null;
                }
                if (!isCancelled()) {
                    isPlaying = true;
                    mp3Player.play(filePath);
                }
                return null;
            }

            @Override
            protected void done() {
                isPlaying = false;
                currentPlayingWorker = null;
            }
        };

        currentPlayingWorker.execute();
    }

    public synchronized void stopSound() {
        // First, stop any currently playing sound via the MP3Player
        if (isPlaying) {
            mp3Player.stop();
            isPlaying = false;
        }

        // Then, cancel any pending SwingWorker that might be in the delay phase
        if (currentPlayingWorker != null && !currentPlayingWorker.isDone()) {
            currentPlayingWorker.cancel(true);
            currentPlayingWorker = null;
        }
    }
}