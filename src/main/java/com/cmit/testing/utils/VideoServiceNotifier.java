package com.cmit.testing.utils;

import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

public class VideoServiceNotifier {
    private String port = "8080";
    private Logger logger = LoggerFactory.getLogger(VideoServiceNotifier.class);

    public VideoServiceNotifier() {
    }

    public VideoServiceNotifier(String port) {
        this.port = port;
    }

    public String notifyStart(String hostAddress, String filename) throws IOException {
        String videoFileName = "demo";
        if (hostAddress == null) {
            return null;
        }
        if (filename != null && filename.trim().length() > 0) {
            videoFileName = filename;
        }
        String notifyUrl = String.format("http://%s:%s/test?method=start&videoName=%s", hostAddress, port, videoFileName);
        logger.info("Headed to {}", notifyUrl);
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        try {
            HttpGet get = new HttpGet(notifyUrl);
            response = httpClient.execute(get);
            String fileName = EntityUtils.toString(response.getEntity());
            return fileName;
        } finally {
            httpClient.close();
        }
    }

    public void notifyStop(String hostAddress) throws IOException {
        if (hostAddress == null) {
            return;
        }
        String notifyUrl = String.format("http://%s:%s/test?method=stop", hostAddress, port);

        logger.info("Headed to {}", notifyUrl);

        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        try {
            HttpGet get = new HttpGet(notifyUrl);
            response = httpClient.execute(get);
            String status = EntityUtils.toString(response.getEntity());

            return;
        } finally {
            response.close();
            httpClient.close();
        }
    }


    public void notifyDestroy(String hostAddress) throws IOException {
        if (hostAddress == null) {
            return;
        }
        String notifyUrl = String.format("http://%s:%s/test?method=destroy", hostAddress, port);

        logger.info("Headed to {}", notifyUrl);

        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        try {
            HttpGet get = new HttpGet(notifyUrl);
            response = httpClient.execute(get);
            String status = EntityUtils.toString(response.getEntity());

            return;
        } finally {
            response.close();
            httpClient.close();
        }
    }


    /**
     * 下载录像
     *
     * @param hostAddress 远程ip
     * @param videoName   录像名称
     * @param path        存放地址
     * @return
     * @throws IOException
     */
    public File downloadVideo(String hostAddress, String videoName, String path) throws IOException {

        String videoURL = String.format("http://%s:%s/movies/%s.avi", hostAddress, port, videoName);

        logger.info("Downloading video at {}", videoURL);

        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        File destination = new File(String.format("%s%s.avi", path, videoName));
        try {
            HttpGet get = new HttpGet(videoURL);
            response = httpClient.execute(get);
            HttpEntity httpEntity = response.getEntity();
            FileUtils.copyInputStreamToFile(httpEntity.getContent(), destination);
            return new File(String.format("%s%s.avi", path, videoName));
        } finally {
            if (response != null) {
                response.close();
            }

            if (httpClient != null) {
                httpClient.close();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        String videoName = "c0374584-a904-4fcf-978c-ad0a9dcd22be";
        String videoURL =

                //String.format("http://%s/movies/%s.avi", "localhost", "646e5535-b5f6-42f5-ad21-518831f3f71f");
                String.format("http://127.0.0.1/movies/%s.avi", videoName);


        LoggerFactory.getLogger(VideoServiceNotifier.class).info("Downloading video at {}", videoURL);

        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        File destination = new File(String.format("%s.avi", videoName));

        try {
            HttpGet get = new HttpGet(videoURL);
            response = httpClient.execute(get);

            HttpEntity httpEntity = response.getEntity();
            FileUtils.copyInputStreamToFile(httpEntity.getContent(), destination);

        } finally {
            if (response != null) {
                response.close();
            }

            if (httpClient != null) {
                httpClient.close();
            }
        }
    }

}
