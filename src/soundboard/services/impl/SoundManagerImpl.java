package soundboard.services.impl;

import soundboard.domain.Sound;
import soundboard.mappers.JsonMapper;
import soundboard.services.ProfileFetcher;
import soundboard.services.SoundDownloader;
import soundboard.services.SoundManager;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class SoundManagerImpl implements SoundManager {
    private static SoundManagerImpl instance;
    private final List<Sound> updatedLocalSounds = new ArrayList<>();

    private SoundManagerImpl() {
        loadSoundsFromJson();
    }

    public static synchronized SoundManagerImpl getInstance() {
        if (instance == null) {
            instance = new SoundManagerImpl();
        }
        return instance;
    }

    @Override
    public List<Sound> getSounds() {
        return updatedLocalSounds;
    }

    @Override
    public synchronized void addSound(Sound sound) {
        updatedLocalSounds.add(sound);
    }

    @Override
    public synchronized void removeSound(Sound sound) {
        updatedLocalSounds.remove(sound);
    }

    @Override
    public void synchronizeSounds() {
        ProfileFetcher profileFetcher = new ProfileFetcherImpl();
        List<Sound> onlineSounds = profileFetcher.getOnlineSounds();
        Map<String, Sound> onlineSoundMap = new HashMap<>();
        List<Sound> newLocalSounds = new ArrayList<>();

        for (Sound onlineSound : onlineSounds) {
            onlineSoundMap.put(onlineSound.getFileName(), onlineSound);
        }

        for (Sound localSound : updatedLocalSounds) {
            String localFileName = localSound.getFileName();
            Path localFilePath = Paths.get(localSound.getFilePath());

            if (onlineSoundMap.containsKey(localFileName)) {
                if (!Files.exists(localFilePath)) {
                    SoundDownloader downloader = new SoundDownloaderImpl();
                    try {
                        downloader.downloadFile(onlineSoundMap.get(localFileName).getUrl());
                    } catch (Exception e) {
                        System.err.println("Download Failed: " + e.getMessage());
                    }
                }

                newLocalSounds.add(localSound);

            } else {
                try {
                    Files.deleteIfExists(localFilePath);
                    System.out.println("Deleted local file: " + localFileName);
                } catch (Exception e) {
                    System.err.println("Error deleting local file: " + e.getMessage());
                }
            }
        }

        for (Sound onlineSound : onlineSounds) {
            boolean found = false;
            for (Sound localSound : updatedLocalSounds) {
                if (localSound.getFileName().equals(onlineSound.getFileName())) {
                    found = true;
                    break;
                }
            }

            if (!found) {
                newLocalSounds.add(onlineSound);
                SoundDownloader downloader = new SoundDownloaderImpl();
                try {
                    downloader.downloadFile(onlineSound.getUrl());
                    System.out.println("Downloaded new sound: " + onlineSound.getFileName());
                } catch (Exception e) {
                    System.err.println("Download Failed: " + e.getMessage());
                }
            }
        }

        newLocalSounds.sort(Comparator.comparing(sound -> sound.getName().toLowerCase()));
        updatedLocalSounds.clear();
        updatedLocalSounds.addAll(newLocalSounds);
        saveLocalSoundObjectsToJsonFile();
    }

    public void saveLocalSoundObjectsToJsonFile() {
        if (removeOldJsonFileAndCreateNewOne()) {
            for (Sound sound : updatedLocalSounds) {
                saveLocalSoundObjectToJsonFile(sound);
            }
        } else {
            System.err.println("Failed to create a new JSON file. Saving sounds aborted.");
        }
    }

    private void saveLocalSoundObjectToJsonFile(Sound sound) {
        String jsonSoundString = JsonMapper.soundObjectToJsonString(sound);
        appendJsonStringToJsonFile(jsonSoundString);
    }

    private boolean removeOldJsonFileAndCreateNewOne() {
        File jsonFile = new File("sounds.json");

        try {
            if (jsonFile.exists() && !jsonFile.delete()) {
                System.err.println("Failed to delete the old JSON file.");
                return false;
            }

            if (jsonFile.createNewFile()) {
                return true;
            } else {
                System.err.println("Failed to create the new JSON file.");
                return false;
            }
        } catch (IOException e) {
            System.err.println("An error occurred while handling the JSON file: " + e.getMessage());
            return false;
        }
    }

    private void appendJsonStringToJsonFile(String jsonString) {
        try {
            FileWriter writer = new FileWriter("sounds.json", true);
            writer.write(jsonString + "\n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadSoundsFromJson() {
        updatedLocalSounds.clear();
        updatedLocalSounds.addAll(JsonMapper.readSoundObjectsFromJsonFile());
    }
}
