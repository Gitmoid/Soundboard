package soundboard.GUI;

import soundboard.Repository.Sound;
import soundboard.SoundManager;
import soundboard.SoundPlayer.SoundPlayer;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class SoundboardGUI extends JFrame {

    private JFrame frame;
    private SoundPlayer soundPlayer;
    private SoundManager soundManager;

    public SoundboardGUI() {
        soundPlayer = new SoundPlayer();
        soundManager = new SoundManager("src/SoundFiles");
        List<Sound> sounds = soundManager.getSounds();

        frame = new JFrame();

        frame.setTitle("Soundboard");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new GridLayout(0, 2));

        for (Sound sound : sounds) {
            addSoundButton(sound);
        }

        frame.pack();
        frame.setVisible(true);
    }

    private void addSoundButton(Sound sound) {
        JButton button = new JButton(sound.getName());
        button.addActionListener(e -> soundPlayer.playSound(sound.getFilePath()));
        frame.add(button);
    }
}
