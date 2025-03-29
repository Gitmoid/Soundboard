package soundboard.controllers;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyPressHandler extends KeyAdapter {

    private final SoundPlayer soundPlayer;

    public KeyPressHandler(SoundPlayer soundPlayer) {
        this.soundPlayer = soundPlayer;
    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        int keyCode = keyEvent.getKeyCode();
        if (keyCode == 37) {
            soundPlayer.stopSound();
        }
    }
}