package soundboard;

import soundboard.GUI.SoundboardGUI;
import soundboard.Repository.Sound;

import java.util.ArrayList;
import java.util.List;

public class SoundboardApp {
    public static void main(String[] args) throws Exception {
        SoundDownloader sd = new SoundDownloader();
        List<Sound> sounds = new ArrayList<Sound>();
        sd.downloadFavorites(sounds);
        new SoundboardGUI();
    }
}