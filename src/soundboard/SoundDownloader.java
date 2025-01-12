package soundboard;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import soundboard.Repository.Sound;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SoundDownloader {
    private static final String LOGIN_URL = "https://www.myinstants.com/accounts/login";
    private static final String FAVORITES_URL = "https://www.myinstants.com/en/favorites/";
    private static final String BASE_URL = "https://www.myinstants.com";
    private static final String SOUND_FILES_FOLDER = "src/SoundFiles/";
    private static final String PROFILE_URL = readProfileUrl("profile_url.txt");

    private Map<String, String> cookies = new HashMap<>();

    public void login(String username, String password) throws Exception {
        Connection.Response loginPageResponse = Jsoup.connect(LOGIN_URL)
                .method(Connection.Method.GET)
                .execute();

        Document loginPage = loginPageResponse.parse();
        String csrfToken = loginPage.select("input[name=csrfmiddlewaretoken]").attr("value");

        // Log the CSRF token from the form for debugging
        System.out.println("CSRF Token from Form: " + csrfToken);

        Connection.Response response = Jsoup.connect(LOGIN_URL)
                .data("csrfmiddlewaretoken", csrfToken) // Include CSRF token
                .data("login", username)                // Login field
                .data("password", password)            // Password field
                .data("remember", "on")                // Optional: remember checkbox
                .cookies(loginPageResponse.cookies())  // Pass initial cookies
                .method(Connection.Method.POST)
                .execute();

        cookies.putAll(response.cookies());

        if (!cookies.isEmpty()) {
            System.out.println("login successful");
        } else {
            throw new Exception("login failed");
        }
    }

    public void downloadProfile(List<Sound> sounds) throws Exception {
        assert PROFILE_URL != null;
        Connection.Response profileResponse = Jsoup.connect(PROFILE_URL)
                .cookies(cookies)
                .method(Connection.Method.GET)
                .execute();

        if (profileResponse.statusCode() == 200) {
            System.out.println(profileResponse.body());
        } else {
            System.out.println("nope");
        }

        Document profilePage = profileResponse.parse();

        Elements playButtons = profilePage.select(".small-button");

        String regex = "onclick=\"play\\('([^']+)'[^\"]*\" title=\"Play (.+?) sound\"";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(playButtons.toString());

        while (matcher.find()) {
            String downloadUrl = BASE_URL + matcher.group(1);
            String soundName = matcher.group(2);
            String soundFileName = downloadUrl.substring(downloadUrl.lastIndexOf("/") + 1);
            System.out.println(soundName);

            String localFilePath = SOUND_FILES_FOLDER + soundFileName;

            if (Files.exists(Paths.get(localFilePath))) {
                System.out.println("File already exists: " + localFilePath);
            } else {
                downloadFile(downloadUrl, localFilePath);
                System.out.println("Downloaded: " + localFilePath);
            }

            sounds.add(new Sound(soundName, localFilePath, soundFileName, downloadUrl));
        }
    }

    private void downloadFile(String fileUrl, String outputPath) throws Exception {
        try (InputStream in = new URL(fileUrl).openStream();
             FileOutputStream out = new FileOutputStream(outputPath)) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
        }
    }

    private static String readProfileUrl(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            return reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
