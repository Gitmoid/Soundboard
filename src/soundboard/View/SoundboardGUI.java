package soundboard.View;

import soundboard.controllers.KeyPressHandler;
import soundboard.controllers.SoundController;
import soundboard.domain.Sound;
import soundboard.controllers.SoundPlayer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

public class SoundboardGUI extends JFrame {

    private JFrame mainGUI;
    private SoundPlayer soundPlayer;
    private final SoundController soundController;

    public SoundboardGUI() {
        soundController = new SoundController();
        synchronizeAndInitialize();
    }

    private void synchronizeAndInitialize() {
        soundController.synchronizeSounds();
        List<Sound> sounds = soundController.getSounds();
        initializeGUI(sounds);
    }

    private void initializeGUI(List<Sound> sounds) {
        soundPlayer = new SoundPlayer();

        mainGUI = new JFrame();
        mainGUI.setTitle("Soundboard");
        ImageIcon img = new ImageIcon("audio-waves.png");
        mainGUI.setIconImage(img.getImage());
        mainGUI.setSize(800, 600);
        mainGUI.setLocationRelativeTo(null);
        mainGUI.setLayout(new GridLayout(0, 2));
        addFrameWithSounds(sounds);
        addUpdateSongsButton();

        KeyPressHandler keyPressHandler = new KeyPressHandler(soundPlayer);
        mainGUI.addKeyListener(keyPressHandler);
        mainGUI.setFocusable(true);
        mainGUI.requestFocusInWindow();
        mainGUI.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        mainGUI.addWindowListener(new SoundboardWindowListener());

        mainGUI.pack();
        mainGUI.setVisible(true);
    }

    private void addFrameWithSounds(List<Sound> sounds) {
        JPanel soundsPanel = new JPanel(new GridLayout(0, 2));
        soundsPanel.removeAll();
        for (Sound sound : sounds) {
            addSoundButton(sound, soundsPanel);
        }
        mainGUI.add(soundsPanel);
    }

    private void addSoundButton(Sound sound, JPanel soundsPanel) {
        JButton button = new JButton(sound.getName());
        button.addActionListener(e -> {
            soundPlayer.playSound(sound.getFilePath());
            soundsPanel.requestFocus(); // Ensure mainGUI retains focus after button click
        });
        soundsPanel.add(button);
    }

    private void addUpdateSongsButton() {
        JButton downloadButton = new JButton("Download");
        downloadButton.addActionListener(e -> {
            soundController.synchronizeSounds();
            updateGUI();
        });
        mainGUI.getContentPane().add(downloadButton);
    }

    private void updateGUI() {
        mainGUI.getContentPane().removeAll();
        List<Sound> sounds = soundController.getSounds();
        addFrameWithSounds(sounds);
        addUpdateSongsButton();
        mainGUI.revalidate();
        mainGUI.repaint();
    }

    private class SoundboardWindowListener extends WindowAdapter {
        @Override
        public void windowClosing(WindowEvent e) {
            new Thread(() -> {
                soundController.saveLocalSoundObjectsToJsonFile();
                System.exit(0);
            }).start();
        }
    }
}
