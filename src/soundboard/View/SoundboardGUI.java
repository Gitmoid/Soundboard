package soundboard.View;

import soundboard.controllers.SoundController;
import soundboard.domain.Sound;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

public class SoundboardGUI extends JFrame {

    private JFrame mainGUI;
    private JPanel menuPanel;
    private JPanel soundPanel;
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
        mainGUI = new JFrame();
        menuPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        soundPanel = new JPanel(new GridLayout(0, 2));

        mainGUI.setTitle("Soundboard");
        ImageIcon img = new ImageIcon("audio-waves.png");
        mainGUI.setIconImage(img.getImage());
        mainGUI.setSize(800, 600);
        mainGUI.setLocationRelativeTo(null);

        menuPanel.setBackground(Color.decode("#3e3e42"));
        addMenu();
        soundPanel.setBackground(Color.decode("#252526"));
        addFrameWithSounds(sounds);

        // KeyPressHandler keyPressHandler = new KeyPressHandler(soundPlayer);
        // mainGUI.addKeyListener(keyPressHandler);
        mainGUI.setFocusable(true);
        mainGUI.requestFocusInWindow();
        mainGUI.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        mainGUI.addWindowListener(new SoundboardWindowListener());

        mainGUI.pack();
        mainGUI.setVisible(true);
    }

    private void addFrameWithSounds(List<Sound> sounds) {
        soundPanel.removeAll();
        for (Sound sound : sounds) {
            addSoundButton(sound, soundPanel);
        }
        mainGUI.getContentPane().add(soundPanel, BorderLayout.CENTER);
        mainGUI.pack();
        soundPanel.revalidate();
        soundPanel.repaint();
    }

    private void addSoundButton(Sound sound, JPanel soundPanel) {
        JButton button = new JButton(sound.getName());
        button.setBackground(Color.decode("#1e1e1e"));
        button.setForeground(Color.decode("#007acc"));
        button.setBorder(BorderFactory.createBevelBorder(0));
        button.setFocusable(false);
        button.addActionListener(e -> {
            soundController.playSound(sound);
            soundPanel.requestFocus(); // Ensure mainGUI retains focus after button click
        });

        FontMetrics metrics = button.getFontMetrics(button.getFont());
        int width = metrics.stringWidth(sound.getName()) + 20;
        int height = metrics.getHeight() + 10;
        button.setPreferredSize(new Dimension(width, height));

        soundPanel.add(button);
    }

    private void addMenu() {
        addEnterProfileUrlButton(menuPanel);
        addSynchronizeSongsButton(menuPanel);
        addEditPlaybackDelay(menuPanel);

        mainGUI.add(menuPanel, BorderLayout.NORTH);
    }

    private JButton createMenuButton(String text, ActionListener actionListener) {
        JButton button = new JButton(text);
        button.setBackground(Color.decode("#2d2d30"));
        button.setForeground(Color.decode("#007AAC"));
        button.setBorder(BorderFactory.createBevelBorder(0));
        button.setFocusable(false);
        button.setPreferredSize(new Dimension(150, 30));
        button.addActionListener(actionListener);
        return button;
    }

    private void addEnterProfileUrlButton(JPanel menuPanel) {
        JButton button = createMenuButton("Enter Profile URL",
                e -> {
            // enter profile url
        });

        menuPanel.add(button);
    }

    private void addSynchronizeSongsButton(JPanel menuPanel) {
        JButton button = createMenuButton("Synchronize Sounds",
                e -> {
            soundController.synchronizeSounds();
            updateGUI();
        });

        menuPanel.add(button);
    }

    private void addEditPlaybackDelay(JPanel menuPanel) {
        JButton button = createMenuButton("Edit Playback Delay",
                e -> {
                    // enter delay in ms
                });

        menuPanel.add(button);
    }

    private void updateGUI() {
        mainGUI.getContentPane().remove(soundPanel);
        List<Sound> sounds = soundController.getSounds();
        addFrameWithSounds(sounds);
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
