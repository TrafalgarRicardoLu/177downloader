package utils;

/**
 * @author trafalgar
 */
public class ConfigHelper {
    private static final String DOWNLOAD_PATH = System.getProperties().getProperty("user.home") + "\\Downloads\\";

    private static final String INDEX_URL = "http://www.177pic.info/html/category/tt";

    private static final String COMIC_NAME_FEATURE = "disp_a";

    private static final String INDEX_BEGIN_FEATURE = "single-navi";

    private static final String[] INDEX_URL_FEATURE = {"href=","span"};

    private static final String IMAGE_FEATURE ="alignnone";

    public static String getDownloadPath() {
        return DOWNLOAD_PATH;
    }

    public static String getComicNameFeature() {
        return COMIC_NAME_FEATURE;
    }

    public static String getIndexBeginFeature(){
        return INDEX_BEGIN_FEATURE;
    }

    public static String[] getIndexUrlFeature(){
        return INDEX_URL_FEATURE;
    }

    public static String getImageFeature(){
        return IMAGE_FEATURE;
    }

    public static String getIndexUrl() {
        return INDEX_URL;
    }
}
