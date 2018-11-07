import utils.ConfigHelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.*;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;

/**
 * @author trafalgar
 */
public class ComicUrlList {

    private LinkedHashMap<String, String> comicMap = new LinkedHashMap<>();

    private LinkedList<String> indexList = new LinkedList<>();

    private LinkedList<String> imageList = new LinkedList<>();

    private static volatile ComicUrlList comicUrlList = null;

    public static ComicUrlList getInstance() {
        if (comicUrlList == null) {
            synchronized (ComicUrlList.class) {
                if (comicUrlList == null) {
                    comicUrlList = new ComicUrlList();
                    return comicUrlList;
                }
            }
        }
        return comicUrlList;
    }

    public HashMap<String, String> initComicMap(URL originUrl) throws IOException {
        System.setProperty("http.agent", "Chrome");
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 1080));

        HttpURLConnection connection = (HttpURLConnection) originUrl.openConnection(proxy);
        connection.setRequestMethod("GET");

        InputStream input = connection.getInputStream();
        InputStreamReader read = new InputStreamReader(input, "UTF-8");
        BufferedReader br = new BufferedReader(read);

        String data = br.readLine();
        while (data != null) {
            if (data.contains(ConfigHelper.getComicNameFeature()[0]) &&
                    data.contains(ConfigHelper.getComicNameFeature()[1])) {
                int comicBegin = data.indexOf("href=");
                int comicEnd = data.indexOf("title");
                String comicLink = data.substring(comicBegin + 6, comicEnd - 2);
                int titleBegin = data.indexOf("Permalink to");
                data = data.substring(titleBegin);
                int titleEnd = data.indexOf("\"");
                String title = data.substring(13, titleEnd);
                if (!comicMap.containsKey(title)) {
                    System.out.println(title + "   " + comicLink);
                    comicMap.put(title, comicLink);
                }
            }
            data = br.readLine();

        }

        br.close();
        read.close();
        input.close();
        connection.disconnect();

        return comicMap;
    }

    public LinkedList<String> initIndexList(URL comicUrl) throws IOException {

        System.setProperty("http.agent", "Chrome");
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 1080));

        HttpURLConnection connection = (HttpURLConnection) comicUrl.openConnection(proxy);
        connection.setRequestMethod("GET");

        InputStream input = connection.getInputStream();
        InputStreamReader read = new InputStreamReader(input, "UTF-8");
        BufferedReader br = new BufferedReader(read);

        int indexCount = 1;
        indexList.add(comicUrl.toString());
        System.out.println("No." + indexCount++ + " index " + comicUrl.toString());

        String data = br.readLine();
        while (data != null) {
            if (data.contains(ConfigHelper.getIndexBeginFeature())) {
                while (data.contains(ConfigHelper.getIndexUrlFeature()[0])) {
                    int begin = data.indexOf(ConfigHelper.getIndexUrlFeature()[0]);
                    data = data.substring(begin);
                    int end = data.indexOf(ConfigHelper.getIndexUrlFeature()[1]);
                    if (end == -1) {
                        break;
                    }
                    indexList.add(data.substring(6, end - 3));

                    System.out.println("No." + indexCount + " index " + data.substring(6, end - 3));
                    indexCount++;
                    data = data.substring(end);
                }
            }
            data = br.readLine();
        }

        br.close();
        read.close();
        input.close();
        connection.disconnect();

        return indexList;
    }

    public LinkedList<String> initImageList(URL indexUrl) throws IOException {
        System.setProperty("http.agent", "Chrome");
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 1080));

        HttpURLConnection connection = (HttpURLConnection) indexUrl.openConnection(proxy);
        connection.setRequestMethod("GET");

        InputStream input = connection.getInputStream();
        InputStreamReader read = new InputStreamReader(input, "UTF-8");
        BufferedReader br = new BufferedReader(read);

        String data = br.readLine();
        while (data != null) {
            if (data.contains(ConfigHelper.getImageFeature())) {
                int begin = data.indexOf("src=");
                int end = data.indexOf("alt");

                imageList.add(data.substring(begin + 5, end - 2));
            }
            data = br.readLine();
        }

        br.close();
        read.close();
        input.close();
        connection.disconnect();

        return imageList;
    }

}
