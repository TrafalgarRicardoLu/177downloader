import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.*;
import java.util.HashMap;
import java.util.LinkedList;

public class ComicLinkList {

    private HashMap<String, String> comicMap = new HashMap<>();

    private LinkedList<String> indexList = new LinkedList<>();

    private LinkedList<String> imageList = new LinkedList<>();

    private static volatile ComicLinkList comicLinkList = null;

    private ComicLinkList() throws IOException {
    }

    public static ComicLinkList getInstance() throws IOException {
        if (comicLinkList == null) {
            synchronized (ComicLinkList.class) {
                if (comicLinkList == null) {
                    comicLinkList = new ComicLinkList();
                    return comicLinkList;
                }
            }
        }
        return comicLinkList;
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
            if (data.indexOf("Permalink to") != -1 && data.indexOf("bookmark") != -1) {
                int comicBegin = data.indexOf("href=");
                int comicEnd = data.indexOf("title");
                String comicLink = data.substring(comicBegin + 6, comicEnd - 2);
                int titleBegin = data.indexOf("Permalink to");
                data = data.substring(titleBegin);
                int titleEnd = data.indexOf("\"");
                String title = data.substring(13, titleEnd);
                if (!comicMap.containsKey(title)) {
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
            if (data.indexOf("single-navi") != -1) {
                while (data.indexOf("href=") != -1) {
                    int begin = data.indexOf("href=");
                    data = data.substring(begin);
                    int end = data.indexOf("span");
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
            if (data.indexOf("alignnone") != -1) {
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
