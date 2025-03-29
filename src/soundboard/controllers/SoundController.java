package soundboard.controllers;

import soundboard.domain.Sound;
import soundboard.services.impl.SoundManagerImpl;

import java.util.List;

public class SoundController {
    private final SoundManagerImpl soundManagerImpl;

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
}
