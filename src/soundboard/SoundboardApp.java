package soundboard;

import soundboard.View.SoundboardGUI;

public class SoundboardApp {
    public static void main(String[] args) throws Exception {
        initializeApp();
    }

    public static void initializeApp() {
        new SoundboardGUI();
    }
}