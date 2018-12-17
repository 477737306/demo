package com.cmit.testing.web;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.cmit.testing.dao.app.AppCaseResultMapper;
import com.cmit.testing.entity.DownloadFileDto;
import com.cmit.testing.entity.SysReport;
import com.cmit.testing.entity.vo.CaseExcResultVO;
import com.cmit.testing.entity.vo.CommonResultVO;
import com.cmit.testing.exception.TestSystemException;
import com.cmit.testing.fastdfs.FileStorageOperate;
import com.cmit.testing.service.SysReportService;
import com.cmit.testing.service.TestCaseService;
import com.cmit.testing.service.app.AppCaseService;
import com.cmit.testing.utils.*;
import com.cmit.testing.utils.file.FileHandlerUtils;
import com.cmit.testing.utils.file.FolderUtils;
import com.cmit.testing.utils.file.ZipUtils;
import com.cmit.testing.utils.verify.HttpUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.cmit.testing.entity.vo.TestCaseReportVO;
import com.cmit.testing.service.TestCaseReportService;

/**
 * 文件下载导出相关接口
 * @author dingpeng
 * 2018年11月16日上午10:51:15
 *
 */
@RestController
@RequestMapping("/api/v1/file")
public class FileController extends BaseController {

	private final static Logger LOGGER = LoggerFactory.getLogger(FileController.class);


	@Autowired
	private TestCaseReportService testCaseReportService;

	@Autowired
	private TestCaseService testCaseService;
	@Autowired
	private AppCaseService appCaseService;
	@Autowired
	private AppCaseResultMapper appCaseResultMapper;
	@Autowired
	private FileStorageOperate fileStorageOperate;
	@Autowired
	private SysReportService sysReportService;



	/**
	 * 压缩截图
	 *
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/zipDownload", method = RequestMethod.GET)
	public JsonResultUtil downloadZipScreenshots(HttpServletResponse response,boolean flag, TestCaseReportVO testCaseReportVO, @RequestParam(defaultValue = "", value = "i") List<Integer> i, @RequestParam(defaultValue = "", value = "x") List<Integer> x) {

		if (null != i && i.size() > 0) {  //i代表web端用例报告id
			testCaseReportVO.setWebTestCaseReportIds(i);
		}
		if (null != x && x.size() > 0) {   //x代表app端用例报告id
			testCaseReportVO.setAppTestCaseReportIds(x);
		}
		if (i.size() > 0 && x.size() == 0) {
			testCaseReportVO.setType("web");
		}
		if (x.size() > 0 && i.size() == 0) {
			testCaseReportVO.setType("app");
		}

		List<DownloadFileDto> downloadFileDtoList = testCaseService.downloadZipScreenshots(testCaseReportVO);


		if (downloadFileDtoList.size() > 0 && flag) {
			return new JsonResultUtil(true);
		}
		if(flag){

			String fileName = "用例执行结果截图压缩包.zip";
			response.reset();
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/json");
			response.setHeader("Access-Control-Allow-Origin", "*");
//		response.setHeader("Access-Control-Allow-Methods","POST");
//		response.setHeader("Access-Control-Allow-Headers:x-requested-with","content-type");
			try {
				//弹出保存框代码
				response.setHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes("UTF-8"), "ISO8859-1"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}

			if (CollectionUtils.isNotEmpty(downloadFileDtoList)) {
				try {
					//压缩
					byte[] dataByteArr = ZipUtils.zipFile(downloadFileDtoList);
					response.getOutputStream().write(dataByteArr);
					response.flushBuffer();
				} catch (Exception e) {
					logger.error("压缩zip数据出现异常", e);
					throw new RuntimeException("压缩zip包出现异常");
				}
			}
		}
		return new JsonResultUtil(300,"无截图！");
	}


	/**
	 * web侧导出用例执行结果和报告与结果查看处用例执行结果导出
	 * @param response
	 * @param testCaseReportVO
	 * @param i
	 * @param x
	 */
	@RequestMapping(value = "/exportCaseExecutionResults", method = RequestMethod.GET)
	public void exportCaseExecutionResults(HttpServletResponse response, TestCaseReportVO testCaseReportVO, @RequestParam(defaultValue = "", value = "i") List<Integer> i, @RequestParam(defaultValue = "", value = "x") List<Integer> x) {

		if (null != i && i.size() > 0) {  //i代表web端用例报告id
			testCaseReportVO.setWebTestCaseReportIds(i);
		}
		if (null != x && x.size() > 0) {   //x代表app端用例报告id
			testCaseReportVO.setAppTestCaseReportIds(x);
		}

		if (i.size() > 0 && x.size() == 0) {
			testCaseReportVO.setType("web");
		}
		if (x.size() > 0 && i.size() == 0) {
			testCaseReportVO.setType("app");
		}

		List<TestCaseReportVO> testCaseReports = testCaseReportService.findAll(testCaseReportVO);
		List<TestCaseReportVO> testList = new ArrayList<>();
		SimpleDateFormat sdf = new SimpleDateFormat(Constants.YYYYMMDDHHMMSS);
		for (TestCaseReportVO test : testCaseReports) {
			if (test.getExcuteTime() != null) {
				String excuteTime = sdf.format(test.getExcuteTime());
				test.setExcuteTimeStr(excuteTime);
			}

			if (Constants.EXCUTE_RESULT_SUCCESS.equals(test.getExcuteResult())) {
				test.setExcuteResultStr("成功");
			} else if (Constants.EXCUTE_RESULT_FAILED.equals(test.getExcuteResult())) {
				test.setExcuteResultStr("失败");
			}

			if (Constants.PASS_STATE_SUCCESS.equals(test.getPassStatus())) {
				test.setPassStatusStr("通过");
			} else if (Constants.PASS_STATE_FAILED.equals(test.getPassStatus())) {
				test.setPassStatusStr("不通过");
			}
			
			//APP侧查看日志地址
            if (StringUtils.isNotEmpty(test.getLog()) && test.getLog().contains("group1")){
                test.setLog("https://yhmncs.chinamobilesz.com:8080//"+test.getLog());
            }


			testList.add(test);
		}
		ExcelJXLSUtil<TestCaseReportVO> excelJXLSUtil = new ExcelJXLSUtil<>();
		excelJXLSUtil.export(response, "用例执行结果", "excel/webCaseExecutionResults.xls", testList);
	}

	/**
	 * App侧截图导出接口
	 *
	 * @param response
	 * @param caseVO
	 */
	@RequestMapping(value = "/downloadZip", method = RequestMethod.GET)
	public JsonResultUtil downloadCompressPicZip(HttpServletResponse response, CommonResultVO caseVO,
									   @RequestParam(value = "flag", required = true) boolean flag) {

		List<DownloadFileDto> downloadFileDtoList = appCaseService.downloadCompressPicZip(caseVO);
		if (flag)
		{
			if (CollectionUtils.isEmpty(downloadFileDtoList))
			{
				throw new TestSystemException("无截图文件");
			}
			return new JsonResultUtil(200);
		}

		if (CollectionUtils.isEmpty(downloadFileDtoList))
		{
			throw new TestSystemException("无截图文件");
		}

		String fileName = "用例执行结果截图压缩包.zip";
		// 必要地清除response中的缓存信息
		response.reset();
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json");
		response.setHeader("Access-Control-Allow-Origin", "*");
		try {
			fileName = new String(fileName.getBytes("UTF-8"), "ISO8859-1");
			// 在浏览器提示用户是保存还是下载
			response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		// 根据个人需要,这个是下载文件的类型
		//response.setContentType("application/octet-stream; charset=UTF-8");

		try {
			// 将截图文件压缩成zip包
			byte[] zipByteFile = ZipUtils.zipFile(downloadFileDtoList);
			// 告诉浏览器下载文件的大小
			//response.setHeader("Content-Length", String.valueOf(zipByteFile.length));
			response.getOutputStream().write(zipByteFile);
			response.setStatus(200);
			response.flushBuffer();
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			throw new RuntimeException("截图文件压缩成zip包出现异常");
		}
		return null;
	}

	/**
	 * APP侧导出用例执行结果
	 * @param response
	 * @param caseExcResultVO
	 */
	@RequestMapping(value = "/exportAppCaseResults", method = RequestMethod.GET)
	public void exportCaseExecutionResults(HttpServletResponse response, CaseExcResultVO caseExcResultVO) {
		List<CaseExcResultVO> appCaseReports = appCaseResultMapper.getAppCaseResultByCaseId(caseExcResultVO);
		List<CaseExcResultVO> testList = new ArrayList<>();
		SimpleDateFormat sdf = new SimpleDateFormat(Constants.YYYYMMDDHHMMSS);
		for (CaseExcResultVO test : appCaseReports) {
			if (test.getExecuteTime() != null) {
				String excuteTime = sdf.format(test.getExecuteTime());
				test.setExcuteTimeStr(excuteTime);
			}

			if (Constants.EXCUTE_RESULT_SUCCESS.equals(test.getPassStatus())) {
				test.setExcuteResultStr("不通过");
			} else if (Constants.EXCUTE_RESULT_FAILED.equals(test.getPassStatus())) {
				test.setExcuteResultStr("通过");
			}

			if (Constants.PASS_STATE_SUCCESS.equals(test.getExcuteResult())) {
				test.setPassStatusStr("通过");
			} else if (Constants.PASS_STATE_FAILED.equals(test.getExcuteResult())) {
				test.setPassStatusStr("不通过");
			}
			//查看日志地址
			if (StringUtils.isNotEmpty(test.getLog()) && test.getLog().contains("group1")) {
				test.setLog("https://yhmncs.chinamobilesz.com:8080//" + test.getLog());
			}

			testList.add(test);
		}
			ExcelJXLSUtil<CaseExcResultVO> excelJXLSUtil = new ExcelJXLSUtil<>();
			excelJXLSUtil.export(response, "用例执行结果", "excel/AppCaseExecutedResult.xls", testList);
	}

	/**
	 * 根据FastDFS路径下载文件二进制流
	 * @param fileId
	 */
	@RequestMapping(value = "/download", method = RequestMethod.GET)
	public void downloadByFileId(HttpServletResponse response, String fileId)
	{
		if (StringUtils.isEmpty(fileId) || !fileId.contains("group"))
		{
			throw new TestSystemException("下载地址不存在或不合法");
		}
		String fileName = System.currentTimeMillis() + "." + FileHandlerUtils.getFileNameExt(fileId);
		byte[] fileBytes = fileStorageOperate.downloadFile(fileId);
		if (fileBytes != null)
		{
			response.reset();
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/json");
			response.setHeader("Access-Control-Allow-Origin", "*");
			response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
			try {
				response.getOutputStream().write(fileBytes);
				response.setStatus(HttpServletResponse.SC_OK);
				response.flushBuffer();
			} catch (IOException e) {
				e.printStackTrace();
				throw new TestSystemException("文件下载失败");
			}
		}
	}


	/**
	 * 导出报告
	 * @param id
	 * @param response
	 */
	@RequestMapping(value="/exportReport" ,method = RequestMethod.GET)
	public void exportReport(String id, HttpServletResponse response){
		SysReport sysReport = sysReportService.selectByPrimaryKey(Integer.parseInt(id));
		if( null == sysReport.getData()){
			throw new TestSystemException("无数据，无法导出！");
		}
		Map<String,Object> map = (Map<String,Object>) JSON.parse(sysReport.getData().toString());
		try {
			ExportReportUtil.exportExelMerge(map,response);
		} catch (Exception e) {
			e.printStackTrace();
			throw new TestSystemException("下载报告失败，报告数据有误！");
		}
	}
}