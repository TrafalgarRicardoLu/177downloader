import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.util.LinkedList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author trafalgar
 */
public class ImageDownloader implements Runnable {

    private volatile int count = 1;
    private volatile Object[] imageArray;
    private String comicPath;
    private volatile int imageCount;

    ImageDownloader(LinkedList<String> imageList, String comicPath) {
        this.imageArray = imageList.toArray();
        this.comicPath = comicPath;
        this.imageCount = imageArray.length;
    }

    private void downloadImage(URL imageUrl, String imagePath) throws IOException {
        System.setProperty("http.agent", "Chrome");
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 1080));

        HttpURLConnection connection = (HttpURLConnection) imageUrl.openConnection(proxy);
        connection.setRequestMethod("GET");
        InputStream is = connection.getInputStream();

        byte[] bs = new byte[1024];
        int len;
        FileOutputStream os = new FileOutputStream(imagePath);
        while ((len = is.read(bs)) != -1) {
            os.write(bs, 0, len);
        }

        os.close();
        is.close();
    }

    @Override
    public void run() {
        while (count < imageCount) {
            String imageLink = (String) imageArray[count - 1];
            String imagePath = comicPath + "/" + count + ".jpg";
            File imageFile = new File(imagePath);

            if (!imageFile.exists()) {
                try {
                    imageFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    System.out.println(count + "/" + imageCount + " " + imageLink);
                    downloadImage(new URL(imageLink), imagePath);
                    count++;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public int getCount() {
        return count;
    }
}
