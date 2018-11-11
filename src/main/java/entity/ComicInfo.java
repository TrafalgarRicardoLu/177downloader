package entity;

public class ComicInfo {
    private String comicName;
    private String comicCover;
    private String comicUrl;

    public ComicInfo(String comicName, String comicCover, String comicUrl) {
        this.comicName = comicName;
        this.comicCover = comicCover;
        this.comicUrl = comicUrl;
    }

    public String getComicName() {
        return comicName;
    }

    public String getComicCover() {
        return comicCover;
    }

    public String getComicUrl() {
        return comicUrl;
    }
}
