package soundboard.controllers;

import soundboard.services.impl.SoundPlayerImpl;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyPressHandler extends KeyAdapter {

    private final SoundPlayerImpl soundPlayerImpl;

    public KeyPressHandler(SoundPlayerImpl soundPlayerImpl) {
        this.soundPlayerImpl = soundPlayerImpl;
    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        int keyCode = keyEvent.getKeyCode();
        if (keyCode == 37) {
            soundPlayerImpl.stopSound();
        }
    }
}