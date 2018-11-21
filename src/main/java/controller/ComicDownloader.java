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

        CompressUtil.compress(comicFolder.toString(), ConfigHelper.getDownloadPath() + DigestUtils.md5Hex(comicInfo.getComicName()) + ".zip");

        indexList.clear();
        imageList.clear();
        System.out.println("zip " + comicInfo.getComicName() + " finished!\n");

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
