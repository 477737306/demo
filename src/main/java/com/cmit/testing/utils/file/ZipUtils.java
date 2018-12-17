package com.cmit.testing.utils.file;

import com.cmit.testing.entity.DownloadFileDto;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.io.IOUtils;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.apache.tools.zip.ZipOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipInputStream;

/**
 * @author XieZuLiang
 * @description TODO
 * @date 2018/8/30 0030 下午 2:56.
 */
public class ZipUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(ZipUtils.class);

    /**
     * 压缩目录，及目录下的所有文件
     * @param destFile 输出结果文件（压缩包）
     * @param srcDir 要压缩的文件目录
     */
    public static void zipFiles(File destFile, String srcDir)
    {
        if (destFile.exists())
        {
            destFile.delete();
        }
        ZipOutputStream zipOut = null;
        FileOutputStream fileOut = null;

        try {
            fileOut = new FileOutputStream(destFile);
            zipOut = new ZipOutputStream(fileOut);
            File srcFile = new File(srcDir);
            if (srcFile.isFile())
            {
                zipFile(srcFile, zipOut, "");
            }
            else
            {
                File[] list = srcFile.listFiles();
                for (int i = 0; i < list.length; i++)
                {
                    compress(list[i], zipOut, "");
                }
            }
            LOGGER.info("文件压缩完毕...");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(zipOut);
            IOUtils.closeQuietly(fileOut);
        }

    }


    /**
     *
     * @param entryNames
     * @param entryFiles
     * @param out
     * @throws IOException
     */
    public static void zipFiles(String[] entryNames, File[] entryFiles, OutputStream out) throws IOException {
        ZipOutputStream zip = null;
        try {
            zip=new ZipOutputStream(out);
            zip.setEncoding("GBK");
            for(int i=0; i<entryNames.length; i++){
                ZipEntry entry = new ZipEntry(entryNames[i]);
                zip.putNextEntry(entry);
                writeZip(entryFiles[i], zip);
                zip.closeEntry();
            }
        }catch (IOException e){
            throw e;
        }finally {
            if (zip != null){
                zip.finish();
            }
        }

    }

    private static void writeZip(File file, ZipOutputStream zip) throws IOException {
        FileInputStream in = null;
        try {
            in = new FileInputStream(file);
            byte[] buffer = new byte[5000];
            int byteCount = in.read(buffer);
            while(byteCount>0){
                zip.write(buffer, 0, byteCount);
                byteCount = in.read(buffer);
            }
        }catch (IOException e) {
            throw e;
        }finally {
            IOUtils.closeQuietly(in);
        }
    }

    /**
     * 压缩文件夹
     *
     * @param reoucePath
     *            需要压缩的文件夹
     * @param zipPath
     *            压缩后的文件夹
     * @throws Exception
     */
    public static void zip(String reoucePath, String zipPath) throws Exception {
        zip(zipPath, new File(reoucePath));
    }

    private static void zip(String zipFileName, File inputFile) throws Exception {
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFileName));
        zip(out, inputFile, "");
        out.close();
    }

    private static void zip(ZipOutputStream out, File f, String base)
            throws Exception {
        if (f.isDirectory()) {
            File[] fl = f.listFiles();
            out.putNextEntry(new org.apache.tools.zip.ZipEntry(base + "/"));
            base = base.length() == 0 ? "" : base + "/";
            for (int i = 0; i < fl.length; i++) {
                zip(out, fl[i], base + fl[i].getName());
            }
        } else {
            out.putNextEntry(new org.apache.tools.zip.ZipEntry(base));
            FileInputStream in = new FileInputStream(f);
            int b;
            while ((b = in.read()) != -1) {
                out.write(b);
            }
            in.close();
        }
    }

    /**
     * 快速解压文件
     *
     * @param zipFileName
     *            需要解压的ZIP 文件
     * @param outputDirectory
     *            解压的目录
     * @return
     * @throws Exception
     */
    @SuppressWarnings("rawtypes")
    public static List<String> unZip(String zipFileName, String outputDirectory) {
        FileOutputStream out = null;
        InputStream in = null;
        ZipFile zipFile = null;
        try {
            String encoding = File.separator.equalsIgnoreCase("/")?"UTF-8":"GBK";
            zipFile = new ZipFile(zipFileName,encoding);
            Enumeration e = zipFile.getEntries();
            ZipEntry zipEntry = null;
            createDirectory(outputDirectory, "");
            List<String> fileNameList = new ArrayList<String>();
            while (e.hasMoreElements()) {
                zipEntry = (ZipEntry) e.nextElement();
                String name = null;
                if (zipEntry.isDirectory()) {
                    name = zipEntry.getName();
                    name = name.substring(0, name.length() - 1);
                    File f = new File(outputDirectory + File.separator + name);
                    f.mkdir();
                } else {
                    String fileName = zipEntry.getName();
                    fileName = fileName.replace('\\', '/');
                    if (fileName.indexOf("/") != -1) {
                        createDirectory(outputDirectory, fileName.substring(0, fileName.lastIndexOf("/")));
                        //fileName = fileName.substring(fileName.lastIndexOf("/") + 1, fileName.length());
                    }
                    File f = new File(outputDirectory + File.separator + zipEntry.getName());
                    f.createNewFile();
                    in = zipFile.getInputStream(zipEntry);
                    out = new FileOutputStream(f);
                    byte[] by = new byte[1024];
                    int c;
                    while ((c = in.read(by)) != -1) {
                        out.write(by, 0, c);
                        //System.out.println(by.toString());
                    }
                }
                fileNameList.add(zipEntry.getName());
            }
            return fileNameList;
        } catch (Exception ex) {
            return null;
        }finally {
            IOUtils.closeQuietly(out);
            IOUtils.closeQuietly(in);
            try {
                if (zipFile != null){
                    zipFile.close();
                }
            }catch (IOException e){}
        }
    }

    private static void createDirectory(String directory, String subDirectory) {
        String dir[];
        File fl = new File(directory);
        try {
            if (subDirectory == "" && fl.exists() != true)
                fl.mkdir();
            else if (subDirectory != "") {
                dir = subDirectory.replace('\\', '/').split("/");
                for (int i = 0; i < dir.length; i++) {
                    File subFile = new File(directory + File.separator + dir[i]);
                    if (subFile.exists() == false)
                        subFile.mkdir();
                    directory += File.separator + dir[i];
                }
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }



    /**
     * 通过zip文件流读取zip中文件名列表
     * @param inputStream
     * @return
     * @throws Exception
     */
    public static List<String> readZipFileNew(InputStream inputStream) throws Exception {
        List<String> fileNames = new ArrayList<String>();
        InputStream in = null;
        ZipInputStream zin = null;
        try {
            in = new BufferedInputStream(inputStream);
            Charset gbk = Charset.forName("GBK");
            zin = new ZipInputStream(in,gbk);
            java.util.zip.ZipEntry ze;
            while ((ze = zin.getNextEntry()) != null) {
                if (ze.isDirectory())
                {
                    fileNames.add(ze.getName());

                } else {
                    fileNames.add(ze.getName());
                }
            }
            return fileNames;
        }catch (Exception e){
            throw e;
        }finally {
            if (zin != null)
            {
                zin.closeEntry();
            }
            if (in != null)
            {
                in.close();
            }
            if (inputStream != null)
            {
                inputStream.close();
            }
        }
    }

    /**
     * 通过zip文件流读取zip中文件名列表
     * @param inputStream
     * @return
     * @throws Exception
     */
    public static List<String> readZipFile(InputStream inputStream) throws Exception {
        List<String> fileNames = new ArrayList<String>();
//        java.util.zip.ZipFile zf = new java.util.zip.ZipFile(file);
        InputStream in = new BufferedInputStream(inputStream);
        Charset gbk = Charset.forName("GBK");
        ZipInputStream zin = new ZipInputStream(in,gbk);
        java.util.zip.ZipEntry ze;
        while ((ze = zin.getNextEntry()) != null) {
            if (ze.isDirectory()) {

            } else {
                fileNames.add(ze.getName());
            }
        }
        zin.closeEntry();
        return fileNames;
    }

    public static ArrayList unzip(String sZipPathFile, String sDestPath) {
        ArrayList<String> allFileName = new ArrayList<String>();
        FileInputStream fins = null;
        FileOutputStream fouts = null;
        ZipInputStream zins = null;
        try {
            fins = new FileInputStream(sZipPathFile);
            Charset gbk = Charset.forName("GBK");
            zins = new ZipInputStream(fins, gbk);
            java.util.zip.ZipEntry ze = null;
            byte[] ch = new byte[256];
            while ((ze = zins.getNextEntry()) != null) {
                File zfile = new File(sDestPath + ze.getName());
                File fpath = new File(zfile.getParentFile().getPath());
                if (ze.isDirectory()) {
                    if (!zfile.exists())
                        zfile.mkdirs();
                    zins.closeEntry();
                } else {
                    if (!fpath.exists())
                    {
                        fpath.mkdirs();
                    }
                    fouts = new FileOutputStream(zfile);
                    int i;
                    allFileName.add(zfile.getAbsolutePath());
                    while ((i = zins.read(ch)) != -1) {
                        fouts.write(ch, 0, i);
                    }
                    zins.closeEntry();
                    fouts.close();
                }
            }
            fins.close();
            zins.close();
        } catch (Exception e) {
            //log.error("unzip the zipFile error", e);
            allFileName = null;
        } finally {
            try {
                if (fins != null) {
                    fins.close();
                }
                if (fouts != null) {
                    fouts.close();
                }
                if (zins != null) {
                    zins.close();
                }
            } catch (IOException e) {
                //log.error("create the zipFile error", e);
            }
        }
        return allFileName;
    }


    /**
     *
     * @Description: TODO(读取Zip信息，获得zip中所有的目录文件信息)
     * @param设定文件
     * @return void 返回类型
     * @throws
     */
    public static void zipFileRead(String file, String saveRootDirectory) {
        java.util.zip.ZipFile zipFile = null;
        try {
            // 获得zip信息
            zipFile = new java.util.zip.ZipFile(file);
            @SuppressWarnings("unchecked")
            Enumeration<java.util.zip.ZipEntry> enu = (Enumeration<java.util.zip.ZipEntry>) zipFile
                    .entries();
            while (enu.hasMoreElements()) {
                java.util.zip.ZipEntry zipElement = enu.nextElement();
                InputStream read = zipFile.getInputStream(zipElement);
                String fileName = zipElement.getName();
                if (fileName != null && fileName.indexOf(".") != -1) {// 是否为文件
                    unZipFile(zipElement, read, saveRootDirectory);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(zipFile != null) {
                try {
                    zipFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }



    /**
     *
     * @Description: TODO(找到文件并读取解压到指定目录)
     * @return void 返回类型
     * @throws
     */
    public  static void unZipFile(java.util.zip.ZipEntry ze, InputStream read,
                                  String saveRootDirectory) throws Exception {
        // 如果只读取图片，自行判断就OK.
        String fileName = ze.getName();
        fileName = fileName.replaceAll("\\\\", "/");
        // 判断文件是否符合要求或者是指定的某一类型
//      if (fileName.equals("WebRoot/WEB-INF/web.xml")) {
        // 指定要解压出来的文件格式（这些格式可抽取放置在集合或String数组通过参数传递进来，方法更通用）
        File file = new File(saveRootDirectory + fileName);
        if (!file.exists()) {
            File rootDirectoryFile = new File(file.getParent());
            // 创建目录
            if (!rootDirectoryFile.exists()) {
                boolean ifSuccess = rootDirectoryFile.mkdirs();
                if (ifSuccess) {
                    //logger.debug("文件夹创建成功! "+rootDirectoryFile.getPath());
                } else {
                    //logger.debug("文件创建失败! "+rootDirectoryFile.getPath());
                }
            }
            // 创建文件
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        BufferedOutputStream write = null;
        FileOutputStream out = null;

        try {
            // 写入文件
            out = new FileOutputStream(file);
            write = new BufferedOutputStream(out);
            int cha = 0;
            while ((cha = read.read()) != -1) {
                write.write(cha);
            }
        } catch (Exception e){
            throw e;
        }finally {
            // 要注意IO流关闭的先后顺序
            if (write != null){
                write.flush();
                write.close();
            }
            if (out != null)
            {
                out.close();
            }
            if (read != null)
            {
                read.close();
            }
        }
    }


    /**
     * 解压zip文件到指定目录
     *
     * @param zipPath
     * @param destDir
     */
    public static void unZipToFold(String zipPath, String destDir) {
        ZipArchiveInputStream ins = null;
        OutputStream os = null;
        File zip = new File(zipPath);
        if (!zip.exists()) {
            return;
        }
        File dest = new File(destDir);
        if (!dest.exists()) {
            dest.mkdirs();
        }
        destDir=formatDirPath(destDir);
        try {
            ins = new ZipArchiveInputStream(new BufferedInputStream(new FileInputStream(zipPath)), "UTF-8");
            ZipArchiveEntry entry = null;
            while ((entry = ins.getNextZipEntry()) != null) {
                if (entry.isDirectory()) {
                    File directory = new File(destDir, entry.getName());
                    directory.mkdirs();
                    directory.setLastModified(entry.getTime());
                } else {
                    String absPath=formatPath(destDir+entry.getName());
                    mkdirsForFile(absPath);
                    File tmpFile=new File(absPath);
                    os=new BufferedOutputStream(new FileOutputStream(tmpFile));
                    IOUtils.copy(ins, os);
                    IOUtils.closeQuietly(os);
                    tmpFile.setLastModified(entry.getTime());
                }
            }

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(ins);
        }
    }

    private static String formatDirPath(String dir){
        if(!dir.endsWith(File.separator)){
            dir+=File.separator;
        }
        return dir;
    }
    private static void mkdirsForFile(String filePath){
        String absPath=filePath;
        String tmpPath=absPath.substring(0,absPath.lastIndexOf(File.separator));
        File tmp=new File(tmpPath);
        if(!tmp.exists()){
            tmp.mkdirs();
        }
    }


    private static String formatPath(String path){
        path=path.replace('\\', File.separatorChar);
        path=path.replace('/', File.separatorChar);
        return path;
    }


    /**
     * 解压缩
     *
     * @param zipFilePath
     *            压缩包路径
     * @param fileSavePath
     *            解压路径
     * @param isDelete
     *            是否删除源文件
     * @throws Exception
     */
    public static void unZip(String zipFilePath, String fileSavePath, boolean isDelete) throws Exception {
        try {
            (new File(fileSavePath)).mkdirs();
            File f = new File(zipFilePath);
            if ((!f.exists()) && (f.length() <= 0)) {
                throw new Exception("要解压的文件不存在!");
            }
            ZipFile zipFile = new ZipFile(f);
            String strPath, gbkPath, strtemp;
            File tempFile = new File(fileSavePath);// 从当前目录开始
            strPath = tempFile.getAbsolutePath();// 输出的绝对位置
            Enumeration<ZipEntry> e = zipFile.getEntries();
            while (e.hasMoreElements()) {
                org.apache.tools.zip.ZipEntry zipEnt = e.nextElement();
                gbkPath = zipEnt.getName();
                if (zipEnt.isDirectory()) {
                    strtemp = strPath + File.separator + gbkPath;
                    File dir = new File(strtemp);
                    dir.mkdirs();
                    continue;
                } else {
                    // 读写文件
                    InputStream is = zipFile.getInputStream(zipEnt);
                    BufferedInputStream bis = new BufferedInputStream(is);
                    gbkPath = zipEnt.getName();
                    strtemp = strPath + File.separator + gbkPath;
                    // 建目录
                    String strsubdir = gbkPath;
                    for (int i = 0; i < strsubdir.length(); i++) {
                        if (strsubdir.substring(i, i + 1).equalsIgnoreCase("/")) {
                            String temp = strPath + File.separator + strsubdir.substring(0, i);
                            File subdir = new File(temp);
                            if (!subdir.exists())
                                subdir.mkdir();
                        }
                    }
                    FileOutputStream fos = new FileOutputStream(strtemp);
                    BufferedOutputStream bos = new BufferedOutputStream(fos);
                    int len;
                    byte[] buff = new byte[1024];
                    while ((len = bis.read(buff)) != -1) {
                        bos.write(buff, 0, len);
                    }
                    bos.close();
                    fos.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        if (isDelete) {
            new File(zipFilePath).delete();
        }
    }

    /**
     * 压缩文件夹里的文件
     * 起初不知道是文件还是文件夹--- 统一调用该方法
     * @param file
     * @param out
     * @param baseDir
     */
    public static void compress(File file, ZipOutputStream out, String baseDir){
        /* 判断是目录还是文件 */
        if (file.isDirectory())
        {
            zipDir(file, out, baseDir);
        }
        else
        {
            zipFile(file, out, baseDir);
        }
    }


    /**
     * 压缩文件夹
     */
    public static void zipDir(File srcDir, ZipOutputStream out, String baseDir){
        if (!srcDir.exists())
        {
            return;
        }
        File[] files = srcDir.listFiles();
        for (int i = 0; i < files.length; i++)
        {
			/* 递归 */
            compress(files[i], out, baseDir + srcDir.getName() + "/");
        }
    }



    /**
     * 压缩单个文件
     *
     * @param srcFile
     */
    public static void zipFile(File srcFile, ZipOutputStream out, String baseDir) {
        if (!srcFile.exists())
        {
            return;
        }
        byte[] buf = new byte[1024];
        FileInputStream in = null;
        ZipEntry zipEntry = null;
        try
        {
            int len;
            in = new FileInputStream(srcFile);
            zipEntry = new ZipEntry(baseDir + srcFile.getName());
            out.putNextEntry(zipEntry);
            while ((len = in.read(buf)) > 0)
            {
                out.write(buf, 0, len);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        } finally {
            try {
                if (out != null)
                {
                    out.closeEntry();
                }
                if (in != null)
                {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public static byte[] zipFile(List<DownloadFileDto> downloadFileDtoList) throws Exception {
        /**将字节写到一个字节输出流里*/
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        java.util.zip.ZipOutputStream out = new java.util.zip.ZipOutputStream(baos);

        try {
            /**创建zip file in memory */
            for (DownloadFileDto downloadFileDto : downloadFileDtoList) {
                java.util.zip.ZipEntry entry = new java.util.zip.ZipEntry(downloadFileDto.getFileName());
                entry.setSize(downloadFileDto.getByteDataArr().length);
                out.putNextEntry(entry);
                out.write(downloadFileDto.getByteDataArr());
                out.closeEntry();
            }
        } catch (IOException e) {
            throw new RuntimeException("压缩zip包出现异常");
        } finally {
            if (out != null)
            {
                out.close();
            }
        }
        return baos.toByteArray();
    }


}
