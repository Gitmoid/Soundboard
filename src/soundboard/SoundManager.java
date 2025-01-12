package soundboard;

import soundboard.Repository.Sound;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SoundManager {
    private List<Sound> sounds;

    public SoundManager(String folderPath) {
        sounds = loadSounds(folderPath);
    }

    private List<Sound> loadSounds(String folderPath) {
        List<Sound> soundList = new ArrayList<Sound>();
        File folder = new File(folderPath);

        if (folder.exists() && folder.isDirectory()) {
            File[] files = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".mp3"));
            if (files != null) {
                for (File file : files) {
                    String name = file.getName().toLowerCase().replace(".mp3", "");
                    soundList.add(new Sound(name, file.getAbsolutePath()));
                }
            } else {
                System.out.println("No sounds found in " + folderPath);
            }
        }

        return soundList;
    }

    public List<Sound> getSounds() {
        return sounds;
    }
}
