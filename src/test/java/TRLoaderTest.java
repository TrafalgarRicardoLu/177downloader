import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Test;
import utils.ConfigHelper;

import java.io.File;
import java.io.IOException;

public class TRLoaderTest {

    @Test
    public void testDownload() throws IOException, InterruptedException {
        TRLoader trLoader = new TRLoader();
        trLoader.downloadByIndex(1);

    }

    @Test
    public void testHashName(){
        String path = ConfigHelper.getDownloadPath();
        File file = new File(path+"[雨霧MIO] 婦性交為 奪われた人妻 [204P]");
        String md5 = DigestUtils.md5Hex(file.toString());
        System.out.println(md5);
    }
}
