import com.sun.org.apache.xpath.internal.SourceTree;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Test;
import utils.ConfigHelper;

import java.io.*;
import java.net.*;

public class TRLoaderTest {

    @Test
    public void testDownload() throws IOException, InterruptedException {
        TRLoader trLoader = new TRLoader();
        trLoader.downloadByIndex(1);

    }

    @Test
    public void testHashName() {
        String path = ConfigHelper.getDownloadPath();
        File file = new File(path + "[雨霧MIO] 婦性交為 奪われた人妻 [204P]");
        String md5 = DigestUtils.md5Hex(file.toString());
        System.out.println(md5);
    }

    @Test
    public void GetPageContext() throws IOException {
        URL originUrl = new URL("http://www.177piczz.info/html/category/tt");
        System.setProperty("http.agent", "Chrome");

        HttpURLConnection connection = (HttpURLConnection) originUrl.openConnection();
        connection.setRequestMethod("GET");

        InputStream input = connection.getInputStream();
        InputStreamReader read = new InputStreamReader(input, "UTF-8");
        BufferedReader br = new BufferedReader(read);

        String data = br.readLine();
        while (data != null) {
            if (data.contains(ConfigHelper.getComicNameFeature())) {
                int comicBegin = data.indexOf("href=");
                int comicEnd = data.indexOf("class");
                String comicUrl = data.substring(comicBegin + 6, comicEnd - 2);

                int titleBegin = data.indexOf("Permalink to");
                data = data.substring(titleBegin);
                int titleEnd = data.indexOf("\"");
                String comicName = data.substring(13, titleEnd);

                int coverBegin = data.indexOf("src=");
                int coverEnd = data.indexOf(".jpg");
                String comicCover = data.substring(coverBegin + 5, coverEnd + 4);
            }

            data = br.readLine();
        }


        br.close();
        read.close();
        input.close();
        connection.disconnect();

    }

}
