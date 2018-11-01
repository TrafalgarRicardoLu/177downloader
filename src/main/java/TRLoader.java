import java.io.*;
import java.net.*;
import java.util.*;

public class TRLoader {

    private static ComicLinkList comicLinkList;
    private static String downloadPath = "D:\\Github\\trloader\\src\\main\\resources\\images\\";

    static {
        try {
            comicLinkList = ComicLinkList.getInstance();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void download(String url, int number) throws IOException {
        URL originUrl = new URL(url);
        HashMap comicMap = comicLinkList.initComicMap(originUrl);
        Set<String> keySet = comicMap.keySet();

        int comicCount =0;
        for (String comicName : keySet) {
            if(comicCount>=number){
                System.out.println(comicCount+"comics have been downloaded");
                return;
            }
            URL comicLink = new URL((String) comicMap.get(comicName));

            System.out.println("Comic: " + comicName + "\n" + "Link: " + comicLink.toString() + "\n");
            System.out.println("Collecting Index");

            LinkedList<String> indexList = comicLinkList.initIndexList(comicLink);
            LinkedList<String> imageList = null;

            File comicPath = new File(downloadPath + comicName);
            if (!comicPath.exists()) {
                comicPath.mkdir();
            }

            for (String indexLink : indexList) {
                imageList = comicLinkList.initImageList(new URL(indexLink));
            }

            System.out.println("\n" + "Downloading " + comicName);

            ImageDownloader id = new ImageDownloader(imageList,comicPath.toString());

            Thread id1 = new Thread(id);
            Thread id2 = new Thread(id);
            Thread id3 = new Thread(id);
            Thread id4 = new Thread(id);
            Thread id5 = new Thread(id);

            id1.start();
            id2.start();
            id3.start();
            id4.start();
            id5.start();

            while (true){
                if(id.getCount()>=imageList.toArray().length){
                    break;
                }
            }

            CompressUtil.compress(comicPath.toString(), comicPath.toString() + ".zip");
            indexList.clear();
            imageList.clear();
            System.out.println("zip " + comicName + " finished!\n");

            comicCount++;
        }

    }


//    private int getImageCount(String comicName) {
//        int i = comicName.lastIndexOf('P') - 1;
//        int sum = 0, exp = 0;
//        for (; i > 0; i--) {
//            char temp = comicName.charAt(i);
//            if (temp > '9' || temp < '0') {
//                break;
//            } else {
//                sum += Math.pow(10, exp) * (temp - '0');
//                exp++;
//            }
//        }
//        return sum;
//    }

    public static void main(String[] args) throws IOException {
        TRLoader TRLoader = new TRLoader();
        TRLoader.download("http://www.177pic.info/html/category/tt", 1);
    }

}
