import org.apache.commons.codec.digest.DigestUtils;
import utils.CompressUtil;
import utils.ConfigHelper;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 * @author trafalgar
 */
public class TRLoader {

    private static ComicUrlList comicUrlList = ComicUrlList.getInstance();
    private static String downloadPath = ConfigHelper.getDownloadPath();

    private void download(String url, Integer index, Integer count) throws IOException, InterruptedException {
        URL originUrl = new URL(url);
        HashMap comicMap = comicUrlList.initComicMap(originUrl);
        Set<String> keySet = comicMap.keySet();

        int comicCount = 0;
        int indexTemp = 1;
        count = (count == null) ? keySet.size() : count;
        count = (index == null) ? count : 1;

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

            System.out.println("\nComic: " + comicName + "\n" + "Link: " + comicUrl.toString() + "\n");
            System.out.println("Collecting Index");

            LinkedList<String> indexList = comicUrlList.initIndexList(comicUrl);
            LinkedList<String> imageList = null;

            File comicFolder = new File(downloadPath + comicName + "\\");
            if (!comicFolder.exists()) {
                comicFolder.mkdir();
            }

            for (String indexLink : indexList) {
                imageList = comicUrlList.initImageList(new URL(indexLink));
            }

            System.out.println("\n" + "Downloading " + comicName);

            ImageDownloader id = new ImageDownloader(imageList, comicFolder.toString());

            Thread id1 = new Thread(id);
            id1.start();
            Thread id2 = new Thread(id);
            id2.start();
            Thread id3 = new Thread(id);
            id3.start();
            Thread id4 = new Thread(id);
            id4.start();
            Thread id5 = new Thread(id);
            id5.start();

            id1.join();
            id2.join();
            id3.join();
            id4.join();
            id5.join();

            CompressUtil.compress(comicFolder.toString(), DigestUtils.md5Hex(comicName) + ".zip");

            indexList.clear();
            imageList.clear();
            System.out.println("zip " + comicName + " finished!\n");

            File[] children = comicFolder.listFiles();
            for (File child : children) {
                child.delete();
            }
            comicFolder.delete();

            comicCount++;
        }

    }

    public void downloadByIndex(int index) throws IOException, InterruptedException {
        download("http://www.177pic.info/html/category/tt", index, null);
    }

    public void downloadByCount(int count) throws IOException, InterruptedException {
        download("http://www.177pic.info/html/category/tt", null, count);
    }

    public void downloadAll() throws IOException, InterruptedException {
        download("http://www.177pic.info/html/category/tt", null, null);
    }

}
