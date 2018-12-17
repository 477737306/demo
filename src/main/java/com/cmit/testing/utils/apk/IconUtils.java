package com.cmit.testing.utils.apk;

import org.apache.commons.io.IOUtils;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;

import java.io.*;

/**
 * @author XieZuLiang
 * @description TODO 通过ApkInfo 里的applicationIcon从APK里解压出icon图片并存放到磁盘上
 * @date 2018/8/30 0030 下午 2:34.
 */
public class IconUtils {

    /**
     * 从指定的apk文件里获取指定file的流
     *
     * @param apkpath
     * @param fileName
     * @return
     */
    public static InputStream extractFileFromApk(String apkpath, String fileName) {
        ZipFile zFile = null;
        try {
            zFile = new ZipFile(apkpath);
            ZipEntry entry = zFile.getEntry(fileName);
            entry.getComment();
            entry.getCompressedSize();
            entry.getCrc();
            entry.isDirectory();
            entry.getSize();
            entry.getMethod();
            InputStream stream = zFile.getInputStream(entry);
            return stream;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeZipFile(zFile);
        }
        return null;
    }



    /**
     * 解压APK
     *
     * @param apkpath
     * @param fileName
     * @param outputPath
     * @throws Exception
     * @author SHANHY
     */
    public static void extractFileFromApk(String apkpath, String fileName, String outputPath) {
        InputStream stream  = null;
        BufferedOutputStream bos = null;
        BufferedInputStream bis = null;
        FileOutputStream out = null;
        ZipFile zFile = null;
        try {
            zFile = new ZipFile(apkpath);
            ZipEntry entry = zFile.getEntry(fileName);
            stream = zFile.getInputStream(entry);
            File file = new File(outputPath);
            out = new FileOutputStream(file);
            bos = new BufferedOutputStream(out, 1024);
            byte[] b = new byte[1024];
            bis = new BufferedInputStream(stream, 1024);
            int num = 0;
            while ((num = bis.read(b)) != -1) {
                bos.write(b, 0, num);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(bos);
            IOUtils.closeQuietly(bis);
            IOUtils.closeQuietly(out);
            IOUtils.closeQuietly(stream);
            closeZipFile(zFile);
        }
    }

    public static void closeZipFile(ZipFile zipFile){
        if (zipFile != null)
        {
            try {
                zipFile.close();
            } catch (IOException e) {

            }
        }
    }
}
