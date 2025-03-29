package soundboard.domain;

public class Sound {
    private String name;
    private String filePath;
    private String fileName;
    private String url;
    private Integer audioDurationInSeconds;

    public Sound(String name, String filePath, String fileName, String url, Integer audioDurationInSeconds) {
        this.name = name;
        this.filePath = filePath;
        this.fileName = fileName;
        this.url = url;
        this.audioDurationInSeconds = audioDurationInSeconds;
    }

    public Sound(String name, String filePath, String fileName, String url) {
        this(name, filePath, fileName, url, null);
    }

    public Sound(String name, String filePath) {
        this(name, filePath, null, null, null);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Integer getAudioDurationInSeconds() {
        return audioDurationInSeconds;
    }

    public void setAudioDurationInSeconds(int audioDurationInSeconds) {
        this.audioDurationInSeconds = audioDurationInSeconds;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setAudioDurationInSeconds(Integer audioDurationInSeconds) {
        this.audioDurationInSeconds = audioDurationInSeconds;
    }
}
