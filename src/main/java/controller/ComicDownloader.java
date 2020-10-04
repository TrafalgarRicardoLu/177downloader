package controller;

import com.sun.jmx.remote.internal.ArrayQueue;
import entity.ComicInfo;
import entity.ComicList;
import org.apache.commons.codec.digest.DigestUtils;
import utils.CompressUtil;
import utils.ConfigHelper;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @author trafalgar
 */
public class ComicDownloader implements Runnable {
    private static ComicList comicList = ComicList.getInstance();
    private static final String downloadPath = ConfigHelper.getDownloadPath();
    public static ArrayQueue<ComicInfo> downloadQueue = new ArrayQueue<>(26);
    public static boolean closeFlag = true;

    private void downloadByComicInfo(ComicInfo comicInfo) throws IOException, InterruptedException {
        URL comicUrl = new URL(comicInfo.getComicUrl());

        System.out.println("\nComic: " + comicInfo.getComicName() + "\n" + "Link: " + comicUrl.toString() + "\n");
        System.out.println("Collecting Index");

        ArrayList<String> indexList = comicList.initIndexList(comicUrl);
        ArrayList<String> imageList = null;

        File comicFolder = new File(downloadPath + comicInfo.getComicName() + "\\");
        if (!comicFolder.exists()) {
            comicFolder.mkdir();
        }

        for (String indexLink : indexList) {
            imageList = comicList.initImageList(new URL(indexLink));
        }

        System.out.println("\n" + "Downloading " + comicInfo.getComicName());

        Executor executor = Executors.newFixedThreadPool(ConfigHelper.getThreadNumber());
        for(int i=0;i<5;i++){
            executor.execute(new ImageDownloader(imageList,comicFolder.toString()));
        }

        CompressUtil.compress(comicFolder.toString(), ConfigHelper.getDownloadPath() + DigestUtils.md5Hex(comicInfo.getComicName()) + ".zip");
        System.out.println("zip " + comicInfo.getComicName() + " finished!\n");

        indexList.clear();
        imageList.clear();

        File[] children = comicFolder.listFiles();
        for (File child : children) {
            child.delete();
        }
        comicFolder.delete();
    }

    @Override
    public void run() {
        while (closeFlag) {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            while(!downloadQueue.isEmpty()) {
                try {
                    downloadByComicInfo(downloadQueue.get(0));
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                downloadQueue.remove(0);
            }
        }
    }
}
