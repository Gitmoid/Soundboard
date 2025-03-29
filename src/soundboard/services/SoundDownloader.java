package soundboard.services;

public interface SoundDownloader {
    void downloadFile(String fileUrl) throws Exception;
}