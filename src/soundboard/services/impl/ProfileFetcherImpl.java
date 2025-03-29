package soundboard.services.impl;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import soundboard.domain.Sound;
import soundboard.services.ProfileFetcher;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProfileFetcherImpl implements ProfileFetcher {
    private static final String LOGIN_URL = "https://www.myinstants.com/accounts/login";
    private static final String FAVORITES_URL = "https://www.myinstants.com/en/favorites/";
    private static final String BASE_URL = "https://www.myinstants.com";
    //private static final String PROFILE_URL = returnStringUrlFromFile();
    private static final String SOUND_FILES_FOLDER = "src/SoundFiles/";

    private final Map<String, String> cookies = new HashMap<>();

    public void login(String username, String password) throws Exception {
        Connection.Response loginPageResponse = Jsoup.connect(LOGIN_URL)
                .method(Connection.Method.GET)
                .execute();

        Document loginPage = loginPageResponse.parse();
        String csrfToken = loginPage.select("input[name=csrfmiddlewaretoken]").attr("value");

        Connection.Response response = Jsoup.connect(LOGIN_URL)
                .data("csrfmiddlewaretoken", csrfToken) // Include CSRF token
                .data("login", username)                // Login field
                .data("password", password)            // Password field
                .data("remember", "on")                // Optional: remember checkbox
                .cookies(loginPageResponse.cookies())  // Pass initial cookies
                .method(Connection.Method.POST)
                .execute();

        cookies.putAll(response.cookies());

        if (cookies.isEmpty()) {
            throw new Exception("login failed");
        }
    }

    @Override
    public List<Sound> getOnlineSounds() {
        List<Sound> onlineSounds = new ArrayList<>();
        String PROFILE_URL = returnStringUrlFromFile();
        assert PROFILE_URL != null;

        try {
            Connection.Response profileResponse = Jsoup.connect(PROFILE_URL)
                    .cookies(cookies)
                    .method(Connection.Method.GET)
                    .execute();

            Document profilePage = profileResponse.parse();

            Elements playButtons = profilePage.select(".small-button");

            String regex = "onclick=\"play\\('([^']+)'[^\"]*\" title=\"Play (.+?) sound\"";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(playButtons.toString());

            while (matcher.find()) {
                String downloadUrl = BASE_URL + matcher.group(1);
                String soundName = matcher.group(2);
                String soundFileName = downloadUrl.substring(downloadUrl.lastIndexOf("/") + 1);
                String localFilePath = SOUND_FILES_FOLDER + soundFileName;

                onlineSounds.add(new Sound(soundName, localFilePath, soundFileName, downloadUrl));
            }
        } catch (IOException e) {
            // Handle IOException, e.g., connection issues, file not found, etc.
            System.err.println("Error downloading profile: " + e.getMessage());
        } catch (Exception e) {
            // Handle any other unexpected exceptions
            System.err.println("Unexpected error: " + e.getMessage());
        }

        return onlineSounds;
    }

    private static String returnStringUrlFromFile() {
        String fileName = "profile_url.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line = reader.readLine();
            if (line == null) {
                throw new IOException("File is empty");
            }
            return line;
        } catch (IOException e) {
            System.err.println("Error while reading profile URL from file: " + e.getMessage());
            return null;
        }
    }
}
