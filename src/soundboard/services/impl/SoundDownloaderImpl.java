package soundboard.services.impl;

import soundboard.services.SoundDownloader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Paths;

public class SoundDownloaderImpl implements SoundDownloader {
    @Override
    public void downloadFile(String fileUrl) throws Exception {
        String outputPath = "src/SoundFiles/";

        try {
            File directory = new File(outputPath);
            if (!directory.exists()) {
                if (!directory.mkdirs()) {
                    throw new IOException("Failed to create directory: " + outputPath);
                }
            }

            URL url = new URL(fileUrl);
            String fileName = Paths.get(url.getPath()).getFileName().toString(); // Extract file name

            String fullOutputPath = outputPath + fileName; // Create full file path

            try (InputStream in = url.openStream();
                 FileOutputStream out = new FileOutputStream(fullOutputPath)) {

                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = in.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
            }

        } catch (Exception e) {
            System.err.println("Download failed: " + e.getMessage());
        }
    }
}
