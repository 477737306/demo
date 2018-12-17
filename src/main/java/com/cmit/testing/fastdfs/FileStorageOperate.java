package com.cmit.testing.fastdfs;

import java.io.File;
import java.io.InputStream;

/**
 * @author XieZuLiang
 * @description TODO 文件存储操作接口
 * @date 2018/8/29 0029 下午 4:49.
 */
public interface FileStorageOperate {
    /**
     * 文件上传到文件服务器
     * @param filePath 待上传文件路径
     * @return 可访问的路径
     */
    String fileStorageUploadFile(String filePath);
    /**
     * 文件上传到文件服务器
     * @param uploadFile 待上传的文件
     * @return 可访问的路径
     */
    String fileStorageUploadFile(File uploadFile);

    /***
     * 文件上传到文件服务器
     * @param uploadFileBytes 上传的文件字节
     * @param fileName 文件名，只是用来取后缀名，并非是上传后的文件名
     * @return 可访问的路径
     */
    String fileStorageUploadFile(byte[] uploadFileBytes, String fileName);

    /**
     * 以流的方式上传文件到文件服务器
     * @param uploadFileInput 文件流
     * @param fileName 文件名，只是用来取后缀名，并非是上传后的文件名
     * @return 可访问的路径
     */
    String fileStorageUploadFile(InputStream uploadFileInput, String fileName);

    /**
     * 从文件上传到文件服务器（即图片缩略图、文件纪要等）
     * @param masterFileId 主文件ID ，原始文件可访问ID，主文件上传的返回值（不带域名）
     * @param uploadSlaveFile 待上传的从文件
     * @param prefixName 从文件后缀名（与主文件区分，例如主文件ID+prefixName.jpg）
     * @return 从文件可访问的路径
     */
    String fileStorageUploadSlaveFile(String masterFileId, File uploadSlaveFile, String prefixName);

    String fileStorageUploadSlaveFile(String masterFileId, byte[] uploadSlaveFileBytes, String prefixName, String fileName);

    String fileStorageUploadSlaveFile(String masterFileId, InputStream uploadSlaveFileInput, String prefixName, String fileName);

    /**
     * 删除文件
     * @param fileId 文件ID，唯一标识
     * @return 是否删除成功
     */
    boolean deleteFile(String fileId);

    /**
     * 下载文件
     * @param fileId 文件ID，唯一标识
     * @return byte 返回文件的二进制
     */
    byte[] downloadFile(String fileId);

    /**
     * 下载文件
     * @param fileId 文件ID，唯一标识
     * @return byte 返回文件的大小
     */
    Long getFileSize(String fileId);
}
