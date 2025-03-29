package soundboard.mappers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import soundboard.domain.Sound;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JsonMapper {

    private static final Gson gson = new Gson();

    public static String soundObjectToJsonString(Sound sound) {
        return gson.toJson(sound);
    }

    public static Sound jsonStringToSoundObject(String jsonString) {
        return gson.fromJson(jsonString, Sound.class);
    }

    public static List<Sound> readSoundObjectsFromJsonFile() {
        List<Sound> sounds = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("sounds.json"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Sound sound = gson.fromJson(line, Sound.class);
                sounds.add(sound);
            }
        } catch (IOException e) {
            System.out.println("Error reading JSON file: "  + e.getMessage());
        }

        return sounds;
    }

    public static List<Sound> jsonStringToListSoundObject(String jsonString) {
        return gson.fromJson(jsonString, new TypeToken<List<Sound>>() {}.getType());
    }

//    public static String soundObjectToJsonString(Sound sound) {
//        return "{"
//                + "\"name\":\"" + sound.getName() + "\","
//                + "\"filePath\":\"" + sound.getFilePath() + "\","
//                + "\"fileName\":\"" + sound.getFileName() + "\","
//                + "\"url\":\"" + sound.getUrl() + "\","
//                + "\"audioDurationInSeconds\":" + sound.getAudioDurationInSeconds()
//                + "}";
//    }

//    public static Sound jsonStringToSoundObject(String jsonString) {
//        String startDelimiter = ":\"";
//        String endDelimiter = "\",";
//        String soundName = jsonString.split(startDelimiter)[1].split(endDelimiter)[0];
//        String filePath = jsonString.split(startDelimiter)[2].split(endDelimiter)[0];
//        String fileName = jsonString.split(startDelimiter)[3].split(".mp3")[0];
//        String url = jsonString.split(startDelimiter)[4].split(endDelimiter)[0];
//        return new Sound(soundName, filePath, fileName, url, null);
//    }
}
