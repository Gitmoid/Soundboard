package soundboard.controllers;

import soundboard.domain.Sound;
import soundboard.services.impl.SoundManagerImpl;
import soundboard.services.impl.SoundPlayerImpl;

import java.util.List;

public class SoundController {
    private final SoundManagerImpl soundManagerImpl;
    private final SoundPlayerImpl soundPlayerImpl = new SoundPlayerImpl();

    public SoundController() {
        this.soundManagerImpl = SoundManagerImpl.getInstance();
    }

    public List<Sound> getSounds() {
        return soundManagerImpl.getSounds();
    }

    public void synchronizeSounds() {
        soundManagerImpl.synchronizeSounds();
    }

    public void saveLocalSoundObjectsToJsonFile() {
        soundManagerImpl.saveLocalSoundObjectsToJsonFile();
    }

    public void playSound(Sound sound) {
        soundPlayerImpl.playSound(sound.getFilePath());
    }

    public void stopSound() {
        soundPlayerImpl.stopSound();
    }
}
