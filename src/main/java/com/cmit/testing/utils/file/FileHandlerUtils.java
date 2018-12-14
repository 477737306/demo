package com.cmit.testing.utils.file;

import org.apache.commons.codec.CharEncoding;
import org.apache.commons.codec.Charsets;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Properties;

/**
 * @author XieZuLiang
 * @description TODO 文件工具类
 * @date 2018/8/30 0030 下午 2:50.
 */
public class FileHandlerUtils {
    /**
     * 深度创建文件，包括文件夹在内，如果不存在则创建。
     * @param file 文件路径
     * @return 成功创建文件，则返回 true，文件存在，则返回 false。
     * @throws IOException
     */
    public static boolean createFile(String file) throws IOException
    {
        return createFile(new File(file));
    }

    /**
     * 深度创建文件，包括文件夹在内，如果不存在则创建。
     * @param file 文件对象
     * @return 成功创建文件，则返回 true，文件存在，则返回 false。
     * @throws IOException
     */
    public static boolean createFile(File file) throws IOException
    {
        File parent = file.getParentFile();
        parent.mkdirs();
        return file.createNewFile();
    }

    /**
     * 以UTF-8编码读取 properties 文件
     * @param file properties 文件
     * @return Properties 对象
     */
    public static Properties readProperties(String file)
    {
        return readProperties(new File(file));
    }

    /**
     * 以UTF-8编码读取 properties 文件
     * @param file properties 文件
     * @return Properties 对象
     */
    public static Properties readProperties(File file)
    {
        Properties p = new Properties();
        readProperties(p, file);
        return p;
    }

    /**
     * 以UTF-8编码读取 properties 文件
     * @param p 要设置的 Properties 对象
     * @param file properties 文件
     */
    public static void readProperties(Properties p, String file)
    {
        readProperties(p, new File(file));
    }

    /**
     * 以UTF-8编码读取 properties 文件
     * @param p 要设置的 Properties 对象
     * @param file properties 文件
     */
    public static void readProperties(Properties p, File file)
    {
        try (InputStreamReader in = new InputStreamReader(new FileInputStream(file), CharEncoding.UTF_8))
        {
            p.load(in);
        }
        catch (IOException e)
        {
            throw new IllegalStateException(e.getMessage());
        }
    }

    /**
     * 获得文件名
     * @param file 文件
     * @return 文件名，如果文件为null则返回null。
     */
    public static String getFileName(String file)
    {
        if (file == null)
        {
            return null;
        }
        return new File(file).getName();
    }

    /**
     * 获得文件名
     * @param file 文件
     * @return 文件名，如果文件为null则返回null。
     */
    public static String getFileName(File file)
    {
        if (file == null)
        {
            return null;
        }
        return file.getName();
    }

    /**
     * 获得文件名前缀
     * @param file 文件
     * @return 文件名前缀，如果文件为null则返回null。
     */
    public static String getFileNamePrefix(String file)
    {
        if (file == null)
        {
            return null;
        }
        return getFileNamePrefix(new File(file));
    }

    /**
     * 获得文件名前缀
     * @param file 文件
     * @return 文件名前缀，如果文件为null则返回null。
     */
    public static String getFileNamePrefix(File file)
    {
        if (file == null)
        {
            return null;
        }
        String fileName = file.getName();
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex == -1)
        {
            return fileName;
        }
        return fileName.substring(0, lastDotIndex);
    }

    /**
     * 获得文件扩展名
     * @param file 文件
     * @return 文件扩展名。如果文件为null则返回null；如果文件名是以.为结尾，则返回""；如果没有.则返回null。
     */
    public static String getFileNameExt(String file)
    {
        if (file == null)
        {
            return null;
        }
        return getFileNameExt(new File(file));
    }

    /**
     * 获得文件扩展名
     * @param file 文件
     * @return 文件扩展名。如果文件为null则返回null；如果文件名是以.为结尾，则返回""；如果没有.则返回null。
     */
    public static String getFileNameExt(File file)
    {
        if (file == null)
        {
            return null;
        }
        String fileName = file.getName();
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex == -1)
        {
            return null;
        }
        return fileName.substring(lastDotIndex + 1);
    }

    /**
     * 将文件以字节方式拷贝至输出流中
     * @param file 文件
     * @param out 输出流
     * @throws IOException
     */
    public static void copy(File file, OutputStream out) throws IOException
    {
        FileInputStream in = null;
        try
        {
            in = new FileInputStream(file);
            IOUtils.copy(in, out);
        }
        finally
        {
            IOUtils.closeQuietly(in);
        }
    }

    /**
     * 将文件以字符方式拷贝至输出流中
     * @param file 文件
     * @param out 输出流
     * @param encoding 文件和输出流的字符编码
     * @throws IOException
     */
    public static void copy(File file, OutputStream out, String encoding) throws IOException
    {
        copy(file, Charsets.toCharset(encoding), out, Charsets.toCharset(encoding));
    }

    /**
     * 将文件以字符方式拷贝至输出流中
     * @param file 文件
     * @param fileEncoding 文件的字符编码
     * @param out 输出流
     * @param outEncoding 输出流的字符编码
     * @throws IOException
     */
    public static void copy(File file, String fileEncoding, OutputStream out, String outEncoding) throws IOException
    {
        copy(file, Charsets.toCharset(fileEncoding), out, Charsets.toCharset(outEncoding));
    }

    /**
     * 将文件以字符方式拷贝至输出流中
     * @param file 文件
     * @param fileEncoding 文件的字符编码
     * @param out 输出流
     * @param outEncoding 输出流的字符编码
     * @throws IOException
     */
    public static void copy(File file, Charset fileEncoding, OutputStream out, Charset outEncoding) throws IOException
    {
        InputStreamReader in = null;
        try
        {
            in = new InputStreamReader(new FileInputStream(file), fileEncoding);
            IOUtils.copy(in, new OutputStreamWriter(out, outEncoding));
        }
        finally
        {
            IOUtils.closeQuietly(in);
        }
    }

    /**
     * 将文件以字符的方式拷贝至输出流中
     * @param file 文件
     * @param out 输出流
     * @throws IOException
     */
    public static void copy(File file, Writer out) throws IOException
    {
        copy(file, out, Charset.defaultCharset());
    }

    /**
     * 将文件以字符的方式拷贝至输出流中
     * @param file 文件
     * @param out 输出流
     * @param encoding 文件的字符编码
     * @throws IOException
     */
    public static void copy(File file, Writer out, String encoding) throws IOException
    {
        copy(file, out, Charsets.toCharset(encoding));
    }

    /**
     * 将文件以字符的方式拷贝至输出流中
     * @param file 文件
     * @param out 输出流
     * @param encoding 文件的字符编码
     * @throws IOException
     */
    public static void copy(File file, Writer out, Charset encoding) throws IOException
    {
        InputStreamReader in = null;
        try
        {
            in = new InputStreamReader(new FileInputStream(file), encoding);
            IOUtils.copy(in, out);
        }
        finally
        {
            IOUtils.closeQuietly(in);
        }
    }

    /**
     * 将输入流以字节方式拷贝至文件中
     * @param in 输入流
     * @param file 文件
     * @throws IOException
     */
    public static void copy(InputStream in, File file) throws IOException
    {
        FileOutputStream out = null;
        try
        {
            out = new FileOutputStream(file);
            IOUtils.copy(in, out);
        }
        finally
        {
            IOUtils.closeQuietly(out);
        }
    }

    /**
     * 将输入流以字符方式拷贝至文件中
     * @param in 输入流
     * @param file 文件
     * @param encoding 输入流和文件的字符编码
     * @throws IOException
     */
    public static void copy(InputStream in, File file, String encoding) throws IOException
    {
        copy(in, Charsets.toCharset(encoding), file, Charsets.toCharset(encoding));
    }

    /**
     * 将输入流以字符方式拷贝至文件中
     * @param in 输入流
     * @param inEncoding 输入流的字符编码
     * @param file 文件
     * @param fileEncoding 文件的字符编码
     * @throws IOException
     */
    public static void copy(InputStream in, String inEncoding, File file, String fileEncoding) throws IOException
    {
        copy(in, Charsets.toCharset(inEncoding), file, Charsets.toCharset(fileEncoding));
    }

    /**
     * 将输入流以字符方式拷贝至文件中
     * @param in 输入流
     * @param inEncoding 输入流的字符编码
     * @param file 文件
     * @param fileEncoding 文件的字符编码
     * @throws IOException
     */
    public static void copy(InputStream in, Charset inEncoding, File file, Charset fileEncoding) throws IOException
    {
        OutputStreamWriter out = null;
        try
        {
            out = new OutputStreamWriter(new FileOutputStream(file), fileEncoding);
            IOUtils.copy(new InputStreamReader(in, inEncoding), out);
        }
        finally
        {
            IOUtils.closeQuietly(out);
        }
    }

    /**
     * 将输入流以字符的方式拷贝至文件中
     * @param in 输入流
     * @param file 文件
     * @throws IOException
     */
    public static void copy(Reader in, File file) throws IOException
    {
        copy(in, file, Charset.defaultCharset());
    }

    /**
     * 将输入流以字符的方式拷贝至文件中
     * @param in 输入流
     * @param file 文件
     * @param encoding 文件的字符编码
     * @throws IOException
     */
    public static void copy(Reader in, File file, String encoding) throws IOException
    {
        copy(in, file, Charsets.toCharset(encoding));
    }

    /**
     * 将输入流以字符的方式拷贝至文件中
     * @param in 输入流
     * @param file 文件
     * @param encoding 文件的字符编码
     * @throws IOException
     */
    public static void copy(Reader in, File file, Charset encoding) throws IOException
    {
        OutputStreamWriter out = null;
        try
        {
            out = new OutputStreamWriter(new FileOutputStream(file), encoding);
            IOUtils.copy(in, out);
        }
        finally
        {
            IOUtils.closeQuietly(out);
        }
    }
}
