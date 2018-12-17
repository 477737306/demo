package com.cmit.testing.utils.file;

import java.io.File;

/**
 * @author XieZuLiang
 * @description TODO 文件夹工具类，包括创建文件夹(不创建文件)、删除文件夹及文件夹内文件等操作
 * @date 2018/8/30 0030 下午 2:48.
 */
public class FolderUtils {
    /**
     * 按路径创建文件夹，如果父文件夹不存在则一同创建
     *
     * @param folderPath
     *            需要创建的文件夹路径
     * @return
     */
    public static void createFolder(String folderPath) {
        File file = new File(folderPath);
        // 如果文件夹不存在则创建
        if (!file.exists() && !file.isDirectory()) {
            //System.out.println("创建文件夹:" + folderPath);
            file.mkdirs();
        }
    }

    /**
     * 根据路径删除指定的目录或文件，无论存在与否
     *
     * @param path
     *            要删除的目录或文件
     * @return 删除成功返回 true，否则返回 false。
     */
    public static boolean deleteFolder(String path) {
        boolean flag = false;
        File file = new File(path);
        // 判断目录或文件是否存在
        if (file.exists()) {
            // 判断是否为文件
            if (file.isFile()) {
                // 为文件时调用删除文件方法
                return deleteFile(path);
            } else {
                // 为目录时调用删除目录方法
                return deleteDirectory(path);
            }
        }
        return flag;
    }

    /**
     * 删除目录（文件夹）以及目录下的文件
     *
     * @param path
     *            被删除目录的文件路径
     * @return 目录删除成功返回true，否则返回false
     */
    public static boolean deleteDirectory(String path) {
        // 如果path不以文件分隔符结尾，自动添加文件分隔符
        if (!path.endsWith(File.separator)) {
            path = path + File.separator;
        }
        File dirFile = new File(path);
        // 如果dir对应的文件不存在，或者不是一个目录，则退出
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            return false;
        }
        boolean flag = true;
        // 删除文件夹下的所有文件(包括子目录)
        File[] files = dirFile.listFiles();
        for (int i = 0; i < files.length; i++) {
            // 删除子文件
            if (files[i].isFile()) {
                flag = deleteFile(files[i].getAbsolutePath());
                if (!flag)
                    break;
            } // 删除子目录
            else {
                flag = deleteDirectory(files[i].getAbsolutePath());
                if (!flag)
                    break;
            }
        }
        if (!flag)
            return false;
        // 删除当前目录
        return dirFile.delete();
    }

    /**
     * 删除单个文件
     *
     * @param path
     *            被删除文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    public static boolean deleteFile(String path) {
        File file = new File(path);
        // 路径为文件且不为空则进行删除
        if (file.isFile() && file.exists()) {
            return file.delete();
        }
        return false;
    }


}
