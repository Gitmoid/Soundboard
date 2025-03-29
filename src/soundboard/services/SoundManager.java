package soundboard.services;

import soundboard.domain.Sound;

import java.util.List;

public interface SoundManager {
    List<Sound> getSounds();
    void synchronizeSounds();
    void addSound(Sound sound);
    void removeSound(Sound sound);
    void saveLocalSoundObjectsToJsonFile();
}
