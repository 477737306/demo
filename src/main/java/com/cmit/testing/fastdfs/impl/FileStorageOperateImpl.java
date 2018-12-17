package com.cmit.testing.fastdfs.impl;

import com.cmit.testing.fastdfs.FileStorageOperate;
import com.cmit.testing.fastdfs.pool.FastdfsPool;
import com.cmit.testing.fastdfs.pool.FastdfsPoolConfig;
import com.cmit.testing.utils.StringUtils;
import org.apache.commons.io.IOUtils;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.StorageClient1;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.*;

/**
 * @author XieZuLiang
 * @description TODO 文件操作接口实现
 * @date 2018/8/29 0029 下午 4:53.
 */
@Component
public class FileStorageOperateImpl implements FileStorageOperate{

    private static final Logger LOGGER = LoggerFactory.getLogger(FileStorageOperateImpl.class);

    private FastdfsPool fastdfsPool;

//    private static Pool pool;

//    @PostConstruct
//    public void init(){
//        pool = fastdfsPool;
//    }

    public FileStorageOperateImpl() {
        if (fastdfsPool == null){
            this.fastdfsPool = new FastdfsPool(new FastdfsPoolConfig());
        }
    }

    @Override
    public boolean deleteFile(String fileId) {
        if (StringUtils.isEmpty(fileId))
        {
            return false;
        }
        if (!fileId.contains("/")) {
            return false;
        }
        int returnCode = -1;
        StorageClient1 storageClient = null;
        try {
            storageClient = fastdfsPool.getResource();
            returnCode = storageClient.delete_file1(fileId);
        }catch (Exception e) {
            LOGGER.error(e.getMessage(),e );
            try {
                if (storageClient != null) {
                    fastdfsPool.returnBrokenResource(storageClient);
                }
            } catch (Exception e1) {
                LOGGER.error(e1.getMessage(), e1);
            }
        } finally {
            try {
                if (storageClient != null) {
                    fastdfsPool.returnResource(storageClient);
                }
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
            }
        }

        return returnCode == 0;
    }

    @Override
    public byte[] downloadFile(String fileId) {
        byte[] buff = null;
        StorageClient1 storageClient = null;
        try {
            storageClient = fastdfsPool.getResource();
            buff = storageClient.download_file1(fileId);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            try {
                if (storageClient != null) {
                    fastdfsPool.returnBrokenResource(storageClient);
                }
            } catch (Exception e1) {
                LOGGER.error(e1.getMessage(), e1);
            }
        } finally {
            try {
                if (storageClient != null) {
                    fastdfsPool.returnResource(storageClient);
                }
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
            }
        }

        return buff;
    }

    @Override
    public String fileStorageUploadFile(String filePath) {
        if (StringUtils.isEmpty(filePath))
        {
            LOGGER.error("未指定长传文件的路径");
            return null;
        }
        File uploadFile = new File(filePath);
        return fileStorageUploadFile(uploadFile);
    }

    @Override
    public String fileStorageUploadFile(File uploadFile) {
        FileInputStream fis = null;
        try {
            String fileName = uploadFile.getCanonicalPath();
            fis = new FileInputStream(uploadFile);
            return fileStorageUploadFile(fis, fileName);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        } finally {
            IOUtils.closeQuietly(fis);
        }
        return null;
    }

    @Override
    public String fileStorageUploadFile(byte[] uploadFileBytes, String fileName) {
        StorageClient1 storageClient = null;
        String fileExtName = null;
        if(fileName.lastIndexOf(".")!=-1){
            fileExtName = fileName.substring(fileName.lastIndexOf(".") + 1);
        }
        String file_url = null;
        try {
            storageClient = fastdfsPool.getResource();
            file_url = storageClient.upload_file1(uploadFileBytes,fileExtName,null);
        }catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            try {
                if (storageClient != null) {
                    fastdfsPool.returnBrokenResource(storageClient);
                }
            } catch (Exception e1) {
                LOGGER.error(e1.getMessage(), e1);
            }
        } finally {
            try {
                if (storageClient != null) {
                    fastdfsPool.returnResource(storageClient);
                }
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
            }
        }
        if(file_url==null || "".equals(file_url)){
            return file_url;
        }

        //String resultUrl = getDomain()+file_url;

        return file_url;
    }

    @Override
    public String fileStorageUploadFile(InputStream uploadFileInput, String fileName) {
        ByteArrayOutputStream out = null;
        try {
            if (uploadFileInput != null) {
                out = new ByteArrayOutputStream();
                IOUtils.copy(uploadFileInput,out);
                return fileStorageUploadFile(out.toByteArray(), fileName);
            }
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        } finally {
            IOUtils.closeQuietly(uploadFileInput);
            IOUtils.closeQuietly(out);
        }
        return null;
    }

    @Override
    public String fileStorageUploadSlaveFile(String masterFileId, File uploadSlaveFile, String prefixName) {
        String slaveFileId = null;
        FileInputStream fis = null;
        try {
            String fileName = uploadSlaveFile.getCanonicalPath();
            fis = new FileInputStream(uploadSlaveFile);
            slaveFileId = fileStorageUploadSlaveFile(masterFileId, fis, prefixName, fileName);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        } finally {
            IOUtils.closeQuietly(fis);
        }

        return slaveFileId;
    }

    @Override
    public String fileStorageUploadSlaveFile(String masterFileId, byte[] uploadSlaveFileBytes, String prefixName, String fileName) {

        StorageClient storageClient = null;
        String slaveFileId = null;
        String fileExtName = null;
        if(fileName.lastIndexOf(".")!=-1){
            fileExtName = fileName.substring(fileName.lastIndexOf(".") + 1);
        }
        try {
            storageClient = fastdfsPool.getResource();
            slaveFileId = ((StorageClient1) storageClient).upload_file1(masterFileId, prefixName, uploadSlaveFileBytes, fileExtName, null);
        }catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            try {
                if (storageClient != null) {
                    fastdfsPool.returnBrokenResource(storageClient);
                }
            } catch (Exception e1) {
                LOGGER.error(e1.getMessage(), e1);
            }
        } finally {
            try {
                if (storageClient != null) {
                    fastdfsPool.returnResource(storageClient);
                }
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
            }
        }
        if(slaveFileId == null || "".equals(slaveFileId)){
            return slaveFileId;
        }
        return slaveFileId;
    }

    @Override
    public String fileStorageUploadSlaveFile(String masterFileId, InputStream uploadSlaveFileInput, String prefixName, String fileName) {

        String slaveFileId = null;
        ByteArrayOutputStream out = null;
        try {
            if (uploadSlaveFileInput != null) {
                out = new ByteArrayOutputStream();
                IOUtils.copy(uploadSlaveFileInput,out);
                return fileStorageUploadSlaveFile(masterFileId, out.toByteArray(), prefixName, fileName);
            }
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        } finally {
            IOUtils.closeQuietly(uploadSlaveFileInput);
            IOUtils.closeQuietly(out);
        }
        return slaveFileId;
    }


    @Override
    public Long getFileSize(String fileId) {
        Long fileSize = null;
        StorageClient1 storageClient = null;
        try {
            storageClient = fastdfsPool.getResource();
             fileSize = storageClient.get_file_info1(fileId).getFileSize();
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            try {
                if (storageClient != null) {
                    fastdfsPool.returnBrokenResource(storageClient);
                }
            } catch (Exception e1) {
                LOGGER.error(e1.getMessage(), e1);
            }
        } finally {
            try {
                if (storageClient != null) {
                    fastdfsPool.returnResource(storageClient);
                }
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
            }
        }

        return fileSize;
    }

}
