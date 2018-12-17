package com.cmit.testing.utils;

import com.cmit.testing.entity.SysUser;
import net.sf.jxls.reader.ReaderBuilder;
import net.sf.jxls.reader.XLSReadStatus;
import net.sf.jxls.reader.XLSReader;
import net.sf.jxls.transformer.XLSTransformer;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.xml.sax.SAXException;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.apache.shiro.util.ClassUtils.getResourceAsStream;

/**
 * @author YangWanLi
 * @date 2018/9/11 16:57
 */
public class ExcelJXLSUtil<T> {

//    ExcelJXLSUtil.class.getResource("/")


	 /**
     * 导出
     *
     * @param response
     * @param destFileName 文件名
     * @param templateFileName 模板名称
     * @param datas  导出数据
     */
    public void export(HttpServletResponse response, String destFileName, String templateFileName, Object datas){

        Map<String,Object> beans = new HashMap<String,Object>();
        beans.put("datas", datas);
        XLSTransformer transformer = new XLSTransformer();
        InputStream in=null;
        OutputStream out=null;

        try {
            //设置响应
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(destFileName+".xls", "utf-8") );
            response.setContentType("application/vnd.ms-excel");
            in=getResourceAsStream(Constants.TEMPLATE_PATH+templateFileName );
            Workbook workbook=transformer.transformXLS(in, beans);
            out=response.getOutputStream();
            //将内容写入输出流并把缓存的内容全部发出去
            workbook.write(out);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        } finally {
            //这里接管了response,我们自己把response flush出去,所以返回类型是void(不要用什么String,否则会报响应已提交过的异常)
            if (in!=null){try {in.close();} catch (IOException e) {}}
            if (out!=null){try {out.close();} catch (IOException e) {}}
        }
    }


    /**
     * 导入
     * @param filePath
     * @param configPath
     * @return
     */
    public List<T> importEcle(String filePath,String configPath) {
        XLSReader mainReader = null;
        InputStream inputXML = null;
        InputStream inputXLS = null;
        try {
            inputXLS = new BufferedInputStream(new FileInputStream(filePath));
            List<T> datas = new ArrayList<>();
            inputXML = new BufferedInputStream(getClass().getClassLoader().getResourceAsStream(configPath));
            mainReader = ReaderBuilder.buildFromXML(inputXML);
            Map<String, Object> beans = new HashMap();
            beans.put("datas", datas);
            XLSReadStatus readStatus = mainReader.read(inputXLS, beans);
            return datas;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        }finally {
        	if (inputXLS!=null){try {inputXLS.close();} catch (IOException e) {}}
            if (inputXML!=null){try {inputXML.close();} catch (IOException e) {}}

        }
        return null;
    }

    public static void main(String[] args) {
        ExcelJXLSUtil<SysUser> excelJXLSUtil = new ExcelJXLSUtil<>();
        List<SysUser> sysUsers = excelJXLSUtil.importEcle("C:\\Users\\hasee\\Documents\\测试.xls","/xml/SysUserConfig.xml");
        System.out.println("成功");
    }
}
