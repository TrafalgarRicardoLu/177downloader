import utils.CompressUtil;
import utils.ConfigHelper;
import sun.security.provider.MD5;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author trafalgar
 */
public class TRLoader {

    private static ComicUrlList comicUrlList = ComicUrlList.getInstance();
    private static String downloadPath = ConfigHelper.getDownloadPath();

    private void download(String url, Integer index, Integer count) throws IOException {
        URL originUrl = new URL(url);
        HashMap comicMap = comicUrlList.initComicMap(originUrl);
        Set<String> keySet = comicMap.keySet();

        int comicCount = 0;
        int indexTemp = 1;
        count = (count == null) ? keySet.size() : count;
        for (String comicName : keySet) {
            if (comicCount >= count) {
                System.out.println(comicCount + "comics have been downloaded");
                return;
            }

            if (index != null && index != indexTemp) {
                indexTemp++;
                continue;
            }

            URL comicUrl = new URL((String) comicMap.get(comicName));

            System.out.println("Comic: " + comicName + "\n" + "Link: " + comicUrl.toString() + "\n");
            System.out.println("Collecting Index");

            LinkedList<String> indexList = comicUrlList.initIndexList(comicUrl);
            LinkedList<String> imageList = null;

            File comicPath = new File(downloadPath + comicName);
            if (!comicPath.exists()) {
                comicPath.mkdir();
            }

            for (String indexLink : indexList) {
                imageList = comicUrlList.initImageList(new URL(indexLink));
            }

            System.out.println("\n" + "Downloading " + comicName);

            ImageDownloader id = new ImageDownloader(imageList, comicPath.toString());

            ExecutorService downloadService = Executors.newFixedThreadPool(5);
            for (int i = 0; i < 5; i++) {
                downloadService.submit(id);
            }

            while (id.getCount() < imageList.toArray().length) {
                System.out.println("Stack");
            }

            CompressUtil.compress(comicPath.toString(), new MD5() + ".zip");
            comicPath.deleteOnExit();

            indexList.clear();
            imageList.clear();
            System.out.println("zip " + comicName + " finished!\n");

            comicCount++;
        }

    }

    public void downloadByIndex(int index) throws IOException {
        download("http://www.177pic.info/html/category/tt", index, null);
    }

    public void downloadByCount(int count) throws IOException {
        download("http://www.177pic.info/html/category/tt", null, count);
    }

    public void downloadAll() throws IOException {
        download("http://www.177pic.info/html/category/tt", null, null);
    }

    public static void main(String[] args) throws IOException {
        TRLoader trLoader = new TRLoader();
        trLoader.download("http://www.177pic.info/html/category/tt", null, 1);
    }
}
