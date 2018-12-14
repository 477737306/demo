package com.cmit.testing.service.app.impl;

import com.cmit.testing.dao.app.AppScriptFileMapper;
import com.cmit.testing.dao.app.AppScriptMapper;
import com.cmit.testing.dao.app.AppScriptStepMapper;
import com.cmit.testing.entity.ModelScript;
import com.cmit.testing.entity.ScriptStep;
import com.cmit.testing.entity.SysPermission;
import com.cmit.testing.entity.app.AppCase;
import com.cmit.testing.entity.app.AppScript;
import com.cmit.testing.entity.app.AppScriptFile;
import com.cmit.testing.entity.app.AppScriptStep;
import com.cmit.testing.entity.script.Attribute;
import com.cmit.testing.entity.script.Script;
import com.cmit.testing.entity.script.Step;
import com.cmit.testing.entity.script.StepBean;
import com.cmit.testing.entity.vo.AppScriptVO;
import com.cmit.testing.entity.vo.AppStepVO;
import com.cmit.testing.entity.vo.ScriptVO;
import com.cmit.testing.entity.vo.SubAppScript;
import com.cmit.testing.exception.TestSystemException;
import com.cmit.testing.fastdfs.FileStorageOperate;
import com.cmit.testing.page.PageBean;
import com.cmit.testing.security.shiro.ShiroKit;
import com.cmit.testing.service.SysPermissionService;
import com.cmit.testing.service.app.*;
import com.cmit.testing.service.impl.BaseServiceImpl;
import com.cmit.testing.utils.*;
import com.cmit.testing.utils.app.ConstantUtil;
import com.cmit.testing.utils.app.StructureAppiumScript;
import com.cmit.testing.utils.file.FileHandlerUtils;
import com.cmit.testing.utils.file.FolderUtils;
import com.cmit.testing.utils.file.ZipUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;
import org.testng.TestException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.*;


/***
 * create by chenxiaozhang
 */
@Service("appScriptService")
public class AppScriptServiceImpl extends BaseServiceImpl<AppScript> implements AppScriptService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppScriptServiceImpl.class);

    @Autowired
    private AppScriptMapper appScriptMapper;

    @Autowired
    private AppCaseService appCaseService;

    @Autowired
    private AppScriptFileMapper appScriptFileMapper;

    @Autowired
    private AppScriptStepMapper  appScriptStepMapper;

    @Autowired
    private AppScriptStepService appScriptStepService;

    @Autowired
    private AppScriptFileService appScriptFileService;

    @Autowired
    private AppScriptService appScriptService;

    @Autowired
    private AppService appService;

    @Autowired
    private FileStorageOperate fileStorageOperate;
    @Autowired
    private SysPermissionService sysPermissionService;

    /**
     * APP脚本保存
     */
    @Transactional
    @Override
    public int saveAppScript(Map<String, Object> map, Integer id) {
        Integer createPerson = ShiroKit.getUser().getId();
        Integer scriptTag = (Integer) map.get("scriptTag");
        AppScript appScript = new AppScript();

        String scriptTempPath = (String) map.get("scriptTempPath");
        if (null != id)
        {
            // 更新脚本
            appScript = appScriptMapper.selectByPrimaryKey(id);
            appScript.setUpdatePerson(createPerson);
            appScript.setUpdateTime(new Date());
        }
        else
        {
            if (1 == scriptTag)
            {
                //普通脚本才有业务父菜单ID
                appScript.setBusinessId((Integer) map.get("businessId"));
            }
            // 新建脚本
            if (StringUtils.isEmpty(scriptTempPath))
            {
                throw new TestSystemException("请上传脚本文件");
            }
            appScript.setScriptTag(scriptTag);
            appScript.setCreatePerson(createPerson);
            appScript.setUpdatePerson(createPerson);
            appScript.setCreateTime(new Date());
            appScript.setUpdateTime(new Date());
            appScript.setOrderCode((Integer) map.get("orderCode"));
            appScript.setPhoneHeight((String) map.get("phoneHeight"));
            appScript.setPhoneWidth((String) map.get("phoneWidth"));
            appScript.setIsClear((Integer) map.get("isClear"));
            appScript.setIsInstall((Integer) map.get("isInstall"));
            appScript.setIsUninstall((Integer) map.get("isUninstall"));

        }

        appScript.setScriptName((String) map.get("name"));
        appScript.setScriptType(2);
        appScript.setDescription((String) map.get("description"));

        if (null == id)
        {
            File file = new File(scriptTempPath);
            appScript.setScriptFileName(file.getName());
            // 脚本zip文件还未上传
            String scriptFilePath = fileStorageOperate.fileStorageUploadFile(file);
            if (StringUtils.isEmpty(scriptFilePath))
            {
                throw new TestSystemException("脚本zip文件保存失败");
            }
            appScript.setScriptPath(scriptFilePath);
            appScript.setMd5Code(Md5Utils.getFileMD5Str(scriptTempPath));
        }
        else if (null != id && StringUtils.isNotEmpty(scriptTempPath))
        {
            File file = new File(scriptTempPath);
            // 编辑脚本，并重新上传了zip文件，需要删除之前FastDFS上的zip文件
            if (StringUtils.isNotEmpty(appScript.getScriptPath()))
            {
                fileStorageOperate.deleteFile(appScript.getScriptPath());
            }
            String scriptFilePath = fileStorageOperate.fileStorageUploadFile(file);
            appScript.setScriptPath(scriptFilePath);
            appScript.setMd5Code(Md5Utils.getFileMD5Str(scriptTempPath));
            appScript.setScriptFileName(file.getName());
        }

        // 获取到脚本文件的相关信息
        AppScriptFile scriptFile = new AppScriptFile();
        if (null != id)
        {
            appScriptMapper.updateByPrimaryKeySelective(appScript);

            scriptFile = appScriptFileService.getScriptFileByScriptId(id).get(0);
        }
        else
        {
            appScriptMapper.insertSelective(appScript);
        }

        // 获取脚本的步骤
        List<Map<String, Object>> stepList = (List<Map<String, Object>>) map.get("step");

        // 脚本保存时，脚本文件和脚本步骤都需要发生变化
        scriptFile.setScriptId(appScript.getScriptId());
        scriptFile.setOrderCode(1);
        scriptFile.setAppId((Integer) map.get("appId"));
        scriptFile.setAppName((String) map.get("appName"));
        scriptFile.setAppVersion((String) map.get("appVersion"));
        scriptFile.setPackageName((String) map.get("packageName"));
        scriptFile.setStepCount(stepList.size());

        if (null == id)
        {
            appScriptFileService.insertNotFull(scriptFile);
        }
        else
        {
            appScriptFileService.updateAppScriptFile(scriptFile);
            // 删除之前的步骤信息，重新添加步骤
            List<Integer> ids = new ArrayList<>();
            ids.add(scriptFile.getScriptFileId());
            appScriptStepService.deleteByScriptFileId(ids);
        }

        List<AppScriptStep> appScriptSteps = new ArrayList<AppScriptStep>();
        int stepNo = 1;
        for (Map<String, Object> stepMap : stepList){
            AppScriptStep scriptStep = new AppScriptStep();
            buildScriptStepByForm(scriptStep, stepMap);
            scriptStep.setStepNo(stepNo+"");
            scriptStep.setScriptFileId(scriptFile.getScriptFileId());
            scriptStep.setScriptId(appScript.getScriptId());
            appScriptSteps.add(scriptStep);
            stepNo++;
        }

        appScriptStepService.insertList(appScriptSteps);

        /**** 以下代码开始将步骤详情编译打包成jar文件 *****/
        boolean isClear = appScript.getIsClear() != 0;

        String basePath = "";
        if (StringUtils.isNotEmpty(scriptTempPath))
        {
            // 表示脚本新建，或者脚本更新时zip文件从新上传了一个
            String fileNamePrefix = FileHandlerUtils.getFileNamePrefix(scriptTempPath);
            basePath = new File(scriptTempPath).getParent() + File.separator + fileNamePrefix;
        }
        else
        {
            // 表示脚本更新，但没有上传脚本zip文件，需从FastDFS上下载zip包
            byte[] fileBytes = fileStorageOperate.downloadFile(appScript.getScriptPath());
            if (fileBytes == null)
            {
                throw new TestSystemException("脚本zip文件丢失");
            }
            String zipPath = ConstantUtil.SCRIPT_TEMP_DIR + System.currentTimeMillis() + File.separator + appScript.getScriptFileName();
            File zipFile = new File(zipPath);
            try {
                FileUtils.writeByteArrayToFile(zipFile, fileBytes);
                ZipUtils.unzip(zipPath, zipFile.getParent() + File.separator);
                basePath = zipFile.getParent() + File.separator + FileHandlerUtils.getFileNamePrefix(zipPath);
                //解压后，pics目录可能变成了文件
                File picsFile = new File(basePath + File.separator + "pics");
                if(!picsFile.exists())
                {
                    picsFile.mkdirs();
                }
                if (picsFile.exists() && !picsFile.isDirectory())
                {
                    picsFile.delete();
                    picsFile.mkdirs();
                }
            } catch (IOException e) {
                throw new TestSystemException("脚本zip文件解压失败");
            }
        }

        String picsPath = basePath + File.separator + "pics";

        if (StringUtils.isNotEmptyAll(appScript.getPhoneHeight(), appScript.getPhoneWidth()))
        {
            StructureAppiumScript structureAppiumScript = new StructureAppiumScript(isClear,
                    Integer.parseInt(appScript.getPhoneHeight()), Integer.parseInt(appScript.getPhoneWidth()));
            String execJarTempUrl = structureAppiumScript.generateScriptJar(appScriptSteps, basePath, picsPath);

            if (StringUtils.isNotEmpty(execJarTempUrl))
            {
                if (StringUtils.isNotEmpty(scriptFile.getExecuteJarUrl()))
                {
                    fileStorageOperate.deleteFile(scriptFile.getExecuteJarUrl());
                }
                String jarFileId = fileStorageOperate.fileStorageUploadFile(execJarTempUrl);
                if (StringUtils.isEmpty(jarFileId))
                {
                    throw new TestSystemException("脚本保存: 脚本Jar文件保存失败");
                }
                String jarMd5Code = Md5Utils.getFileMD5Str(execJarTempUrl);
                scriptFile.setExecuteJarUrl(jarFileId);
                scriptFile.setMd5Code(jarMd5Code);
                appScriptFileService.updateAppScriptFile(scriptFile);
            }
            else
            {
                FolderUtils.deleteDirectory(new File(basePath).getParent());
                // jar文件生成失败
                throw new TestSystemException("脚本保存: 脚本步骤编译生成Jar文件失败!");
            }
            FolderUtils.deleteDirectory(new File(basePath).getParent());
        }

        return appScript.getScriptId();
    }

    @Override
    public int delScriptByIds(List<Integer> ids)
    {
        int i = 0;
        List<AppCase> caseList = new ArrayList<>();
        for (Integer scriptId : ids)
        {
            AppCase appCase = new AppCase();
            appCase.setScriptId(scriptId);
            caseList.addAll(appCaseService.getAllByAppCase(appCase));
        }
        if (caseList.size() > 0)
        {
            throw new TestSystemException("脚本已被其他用例所关联不能删除");
        }
        // 存放脚本zip包路径
        List<String> zipList = new ArrayList<>();
        // 存放脚本jar文件路径
        List<String> jarList = new ArrayList<>();
        for (Integer scriptId : ids)
        {
            AppScript appScript = appScriptMapper.selectByPrimaryKey(scriptId);
            if (appScript != null)
            {
                zipList.add(appScript.getScriptPath());
                List<AppScriptFile> fileList = appScriptFileMapper.getScriptFileByScriptId(scriptId);
                for (AppScriptFile file : fileList)
                {
                    if (StringUtils.isNotEmpty(file.getExecuteJarUrl()))
                    {
                        jarList.add(file.getExecuteJarUrl());
                    }
                }
            }
            // 删除数据库中的数据 app_script、app_script_file、app_script_step
            appScriptStepMapper.deleteByScriptId(scriptId);
            appScriptFileMapper.deleteByscriptId(scriptId);
            i += appScriptMapper.deleteByPrimaryKey(scriptId);
        }

        // 数据库中数据删除完成后，再删除FastDFS文件服务器上的数据
        for (String zipPath : zipList)
        {
            fileStorageOperate.deleteFile(zipPath);
        }
        for (String jarPath : jarList)
        {
            fileStorageOperate.deleteFile(jarPath);
        }

        return i;
    }

    @Override
    public PageBean<AppCase> getPageByScriptId(PageBean pageBean, AppCase appCase) {
        Page<Object> page = PageHelper.startPage(pageBean.getCurrentPage(), pageBean.getPageSize());
        page.setOrderBy("order_code");
        pageBean.setItems(appCaseService.getList(appCase));
        pageBean.setTotalNum((int) page.getTotal());
        return pageBean;
    }

    @Override
    public Map<String, Object> checkScriptName(String scriptId, String scriptName) {

        Map<String,Object>  resultMap= new HashMap<>();
        int count = appScriptMapper.getCountByName(scriptId, scriptName);
        if (count == 0)
        {
            resultMap.put("unique", true);
        }
        else
        {
            resultMap.put("unique", false);
        }
        return resultMap;
    }

    @Override
    public AppScriptVO getAppScriptByScriptId(Integer scriptId) {
        return appScriptMapper.getScriptById(scriptId);
    }

    @Override
    public AppScript getScriptByScriptId(Integer scriptId) {
        return appScriptMapper.selectScriptById(scriptId);
    }
    @Override
    public List<AppScript> getAllByBusinessId(Integer businessId)
    {
        List<AppScript> scriptList = new ArrayList<>();
        AppScript as = new AppScript();
        as.setScriptTag(0);
        // 获取所有通用的APP脚本
        scriptList.addAll(appScriptMapper.selectByAppScript(as));
        // 获取当前业务下的所有普通的脚本
        as.setScriptTag(1);
        as.setBusinessId(businessId);
        scriptList.addAll(appScriptMapper.selectByAppScript(as));

        return scriptList;
    }

    @Override
    public List<SubAppScript> getAllByBusinessId1(Integer businessId) {
        List<SubAppScript> subAppScripts = new ArrayList<>();

        List<AppScript> appScriptList = appScriptMapper.getAllByBusinessId(businessId);


        /*if (appScriptList.size() > 0) {
            for (AppScript as : appScriptList) {
                SubAppScript subAppScript = new SubAppScript();
                List<Integer> scriptIds = new ArrayList<>();
                scriptIds.add(as.getScriptId());
                List<AppScriptStep> appScriptStepList =appScriptStepMapper.listByScriptIds(scriptIds);
                subAppScript.setScriptId(as.getScriptId());
                subAppScript.setScriptName(as.getScriptName());
                subAppScript.setScriptTag(as.getScriptTag());
                subAppScript.setScriptType(as.getScriptType());
                subAppScript.setDescription(as.getDescription());
                subAppScript.setUpdateTime(as.getUpdateTime());
                subAppScript.setUpdatePerson(as.getUpdatePerson());
                subAppScript.setOrderCode(as.getOrderCode());
                subAppScript.setPhoneHeight(as.getPhoneHeight());
                subAppScript.setPhoneWidth(as.getPhoneWidth());
                subAppScript.setScriptFileName(as.getScriptFileName());
                subAppScript.setScriptPath(as.getScriptPath());
                subAppScript.setMd5Code(as.getMd5Code());
                subAppScript.setBusinessId(as.getBusinessId());
                subAppScript.setCreatePerson(as.getCreatePerson());
                subAppScript.setCreateTime(as.getCreateTime());
                subAppScript.setDescription(as.getDescription());
                subAppScript.setIsClear(as.getIsClear());
                subAppScript.setIsInstall(as.getIsInstall());
                subAppScript.setIsUninstall(as.getIsUninstall());
                subAppScript.setStep(appScriptStepList);
                subAppScripts.add(subAppScript);

            }
        }*/
        return subAppScripts;
    }

    @Override
    public Map<String, Object> uploadScript(MultipartFile multipartFile, ScriptVO vo) throws Exception {

        String fileName = multipartFile.getOriginalFilename();

        //读取zip文件中文件名列表
        List<String> childFileNameList = ZipUtils.readZipFileNew(multipartFile.getInputStream());
        validateZipDirectory(childFileNameList);

        String uploadZipName = fileName.substring(0, fileName.length() - 4);
        String scriptXmlName = uploadZipName + ".xml";

        //保存到临时目录
        String tempFileDir = ConstantUtil.SCRIPT_TEMP_DIR + System.currentTimeMillis();
        FolderUtils.createFolder(tempFileDir);
        String scriptTempUrl = tempFileDir + File.separator + fileName;
        File file = new File(scriptTempUrl);
        multipartFile.transferTo(file);

        String parentDir = file.getParent() + File.separator;
        //读取并解压zip文件
        ZipUtils.zipFileRead(scriptTempUrl, parentDir);
        String unzipPicsPath = parentDir + uploadZipName + File.separator + "pics";
        //解压后，pics目录可能变成了文件
        File picsFile = new File(unzipPicsPath);
        if(!picsFile.exists())
        {
            picsFile.mkdirs();
        }
        if (picsFile.exists() && !picsFile.isDirectory())
        {
            picsFile.delete();
            picsFile.mkdirs();
        }

        String scriptXmlPath = picsFile.getParent() + File.separator + scriptXmlName;
        //解析XML文件
        Script instanceScript = parseScriptXmlWithoutBiz(scriptXmlPath);

        vo.setPhoneHeight(instanceScript.getPhoneHeight());
        vo.setPhoneWidth(instanceScript.getPhoneWidth());

        vo.setIsClear("true".equals(instanceScript.getCleanData()) ? 1 : 0);
        vo.setIsInstall("true".equals(instanceScript.getUninstallApk()) ? 1: 0);
        vo.setIsUninstall("true".equals(instanceScript.getUninstallApk()) ? 1 : 0);
        //vo.setScriptPath(file.getPath());
        vo.setScriptTempPath(file.getPath());
        vo.setAppName(instanceScript.getApplicationName());
        vo.setAppVersion(instanceScript.getVersionName());
        vo.setPackageName(instanceScript.getPackageName());
        Integer appId = appService.getAppIdByInfo(instanceScript.getApplicationName(),instanceScript.getVersionName(),instanceScript.getPackageName());
        if (appId == null)
        {
            throw new RuntimeException("上传的脚本在应用管理中找不到关联的应用，请上传相关的应用");
        }
        vo.setAppId(appId);

        List<AppStepVO> stepVoList = new ArrayList<AppStepVO>();
        // 添加脚本文件对应的脚本步骤信息
        List<Step> stepList = instanceScript.getStepList();
        for (Step step : stepList)
        {
            AppStepVO sv = new AppStepVO();
            List<Attribute> attrList = step.getAttributeList();
            buildScriptStep(sv, attrList);
            stepVoList.add(sv);
        }
        vo.setStepList(stepVoList);
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("script", vo);
        if (vo.getScriptId() != null) {
            AppScript as = appScriptMapper.selectByPrimaryKey(vo.getScriptId());
            vo.setScriptName(as.getScriptName());
            vo.setDescription(as.getDescription());
        }

        return resultMap;
    }

    /**
     * 解析脚本XML文件
     * @param scriptXmlPath
     * @return
     */
    private Script parseScriptXmlWithoutBiz(String scriptXmlPath) {
        StringReader reader = null;
        try {
            JAXBContext jaxb = JAXBContext.newInstance(Script.class);
            Unmarshaller unmarshaller = jaxb.createUnmarshaller();
            File f = new File(scriptXmlPath);
            String scriptStr = org.apache.commons.io.FileUtils.readFileToString(f);
            reader = new StringReader(scriptStr);
            return (Script) unmarshaller.unmarshal(reader);
        }catch (Exception e){
            LOGGER.error(e.getMessage(), e);
            throw new TestSystemException("解析脚本XML文件异常");
        }finally {
            if (reader != null){
                reader.close();
            }
        }
    }

    /**
     * 校验zip包目录结构
     * @param childFileNameList
     */
    private void validateZipDirectory(List<String> childFileNameList) {
        if (CollectionUtils.isEmpty(childFileNameList))
        {
            throw new TestSystemException("脚本Zip文件异常：空的zip文件");
        }

        List<String> formatPathList = new ArrayList<>();
        for (String fileName : childFileNameList) {
            //formatPathList.add(fileName.replace("\\", File.separator));
            formatPathList.add(fileName.replace("\\", "|"));
        }
        Set<String> rootFile = new HashSet<>();
        List<String> xmlList = new ArrayList<>();
        List<String> picList = new ArrayList<>();
        List<String> picNameList = new ArrayList<>();
        int maxLength = 0;
        for (String formatDir : formatPathList) {
            // Linux 环境
            //String[] dicArray = formatDir.split("/");
            // Window 本地测试环境
            String[] dicArray = formatDir.split("\\|");
            if (dicArray.length > maxLength) {
                maxLength = dicArray.length;
            }
            rootFile.add(dicArray[0]);
            //zip文件下直接放pic和xml目录
            if (dicArray.length == 1) {
                if (dicArray[0].endsWith(".xml") || dicArray[0].endsWith("pics")) {
                    throw new TestSystemException("脚本zip文件校验异常：zip文件下直接放pic和xml目录");
                }
            }
            if (dicArray.length == 2) {
                //存在pics和xml之外的文件
                if (!(dicArray[1].endsWith(".xml") || dicArray[1].equals("pics"))) {
                    throw new TestSystemException("脚本zip文件校验异常：存在pics和xml之外的文件");
                } else {
                    if (dicArray[1].endsWith(".xml")) {
                        xmlList.add(formatDir);
                    }
                    if (dicArray[1].equals("pics")) {
                        picList.add(formatDir);
                    }
                }
            }
            if (dicArray.length == 3) {
                if (dicArray[1].equals("pics") && dicArray[2].endsWith(".jpg")) {
                    picNameList.add(formatDir);
                } else {
                    throw new TestSystemException("脚本zip文件校验异常：存在pics和xml之外的文件");
                }
            }
        }
        if (maxLength > 3 || rootFile.size() > 1 || xmlList.size() > 1) {
            throw new TestSystemException("脚本zip文件校验异常：存在pics和xml之外的文件");
        }
        boolean picFlag = xmlList.size() == 1 && picList.size() == 1;
        boolean xmlFlag = xmlList.size() == 1 && picNameList.size() >= 1;
        //缺少pic或者xml或者两者都缺
        if (!(picFlag || xmlFlag)) {
            throw new TestSystemException("脚本zip文件校验异常：缺少pic或者xml或者两者都缺");
        }
    }

    /**
     * 根据前端表单提交的数据构建脚本步骤对象
     */
    private void buildScriptStepByForm(AppScriptStep scriptStep, Map<String, Object> map){
        String actionType = (String) map.get("actionType");
        String paramType = (String) map.get("paramType");
        String paramValue = (String) map.get("paramValue");
        String paramKey = (String) map.get("paramKey");
        String orientValue = (String) map.get("orientValue");
        String remark = (String) map.get("remark");

        if (StringUtils.isNotEmpty(actionType))
        {
            switch (actionType){
                // 休眠 等待
                case AppConstants.SCRIPT_STEP_SLEEP:
                    scriptStep.setActionType(AppConstants.SCRIPT_STEP_SLEEP);
                    //scriptStep.setInputValue(paramValue);
                    scriptStep.setInputValue(paramKey);
                    scriptStep.setStepDesc(remark);
                    break;
                // 上滑
                case AppConstants.SCRIPT_STEP_UPGLIDE:
                // 下滑
                case AppConstants.SCRIPT_STEP_DOWNGLIDE:
                // 右滑
                case AppConstants.SCRIPT_STEP_RIGHTGLIDE:
                // 左滑
                case AppConstants.SCRIPT_STEP_LEFTGLIDE:
                    scriptStep.setActionType(actionType);
                    scriptStep.setStepDesc(remark);
                    break;
                // 返回键
                case AppConstants.SCRIPT_STEP_BACK:
                    scriptStep.setActionType(actionType);
                    scriptStep.setStepDesc(remark);
                    break;
                // 点击坐标动作
                case AppConstants.SCRIPT_STEP_TAP:
                    scriptStep.setActionType(AppConstants.SCRIPT_STEP_TAP);
                    if (StringUtils.isNotEmpty(orientValue))
                    {
                        scriptStep.setCoordinate(orientValue);
                        scriptStep.setStepDesc("点击坐标("+orientValue+")");
                    }
                    else
                    {
                        scriptStep.setActionText(orientValue);
                        scriptStep.setStepDesc("点击" +paramValue);
                    }
                    break;
                // 点击xpath动作
                case AppConstants.SCRIPT_STEP_CLICK:
                // 双击
                case AppConstants.SCRIPT_STEP_DOUBLECLICK:
                // 长按
                case AppConstants.SCRIPT_STEP_LONGPRESS:
                    scriptStep.setActionType(actionType);
                    scriptStep.setXpath(orientValue);
                    scriptStep.setStepDesc(remark);
                    break;
                // 图像识别点击动作
                case AppConstants.SCRIPT_STEP_PICTURETAP:
                    scriptStep.setActionType(AppConstants.SCRIPT_STEP_PICTURETAP);
                    scriptStep.setCoordinate(orientValue);
                    scriptStep.setPicName(paramValue);
                    scriptStep.setStepDesc(remark);
                    break;
                // if 判定
                case AppConstants.SCRIPT_STEP_IFJUDGE:
                // for 循环
                case AppConstants.SCRIPT_STEP_FORLOOP:
                    scriptStep.setActionType(actionType);
                    if ("start".equals(paramKey))
                    {
                        scriptStep.setActionText(AppConstants.SCRIPT_CMD_START);
                        scriptStep.setInputValue(paramValue);
                    }
                    else if ("end".equals(paramKey))
                    {
                        scriptStep.setActionText(AppConstants.SCRIPT_CMD_END);
                    }
                    scriptStep.setStepDesc(remark);
                    break;
                // 事务
                case AppConstants.SCRIPT_STEP_AFFAIR:
                    scriptStep.setActionType(AppConstants.SCRIPT_STEP_AFFAIR);
                    if ("start".equals(paramKey))
                    {
                        scriptStep.setActionText(AppConstants.SCRIPT_CMD_START);
                    }
                    else if ("end".equals(paramKey))
                    {
                        scriptStep.setActionText(AppConstants.SCRIPT_CMD_END);
                        // param2 保存的是事物结束控件的xpath
                        scriptStep.setXpath(orientValue);
                    }
                    scriptStep.setStepDesc(remark);
                    break;
                // 输入动作
                case AppConstants.SCRIPT_STEP_SENDTEXT:
                // 输入动态密码
                case AppConstants.SCRIPT_STEP_DYNAMIC:
                    scriptStep.setActionType(actionType);
                    scriptStep.setXpath(orientValue);
                    scriptStep.setInputValue(paramValue);
                    //scriptStep.setStepCode(paramType);
                    scriptStep.setStepCode(paramKey);
                    scriptStep.setStepDesc(remark);
                    break;
                // 短信校验
                case AppConstants.SCRIPT_STEP_SMSVARIFY:
                    scriptStep.setActionType(AppConstants.SCRIPT_STEP_SMSVARIFY);
                    scriptStep.setInputValue(paramKey);
                    scriptStep.setStepDesc(remark);
                    break;
                // 内容校验
                case AppConstants.SCRIPT_STEP_VERIFY:
                    scriptStep.setActionType(AppConstants.SCRIPT_STEP_VERIFY);
                    scriptStep.setInputValue(paramKey);
                    scriptStep.setStepDesc(remark);
                    break;
                default:
                    LOGGER.error("无法找到匹配项!");
                    break;
            }
        }
    }

    /**
     * 构建脚本步骤对象
     */
    private void buildScriptStep(AppStepVO scriptStep, List<Attribute> list){
        StepBean sb = new StepBean();
        sb.setExistStart(false);
        sb.setExistEnd(false);
        for (Attribute attr : list) {
            if ("action".equals(attr.getName()))
            {
                sb.setActionCode(attr.getValue());
            }
            if ("param1".equals(attr.getName()))
            {
                if ("start".equals(attr.getValue()))
                {
                    sb.setExistStart(true);
                }
                if ("end".equals(attr.getValue()))
                {
                    sb.setExistEnd(true);
                }
                sb.setParam1(attr.getValue());
            }
            if ("param2".equals(attr.getName()))
            {
                sb.setParam2(attr.getValue());
            }
            if ("param3".equals(attr.getName()))
            {
                sb.setParam3(attr.getValue());
            }
            if ("desc".equals(attr.getName()))
            {
                sb.setDesc(attr.getValue());
            }
        }

        getStepByXml(scriptStep, sb);

    }

    private void getStepByXml(AppStepVO scriptStep, StepBean sb){

        switch (sb.getActionCode()){
            case AppConstants.SCRIPT_STEP_SLEEP:
                scriptStep.setActionType(AppConstants.SCRIPT_STEP_SLEEP);
                //scriptStep.setParamValue(sb.getParam1());
                scriptStep.setParamKey(sb.getParam1());
                scriptStep.setRemark("等待" + sb.getParam1() + "秒");
                break;
            case AppConstants.SCRIPT_STEP_UPGLIDE:
                scriptStep.setActionType(AppConstants.SCRIPT_STEP_UPGLIDE);
                scriptStep.setRemark("上滑");
                break;
            case AppConstants.SCRIPT_STEP_DOWNGLIDE:
                scriptStep.setActionType(AppConstants.SCRIPT_STEP_DOWNGLIDE);
                scriptStep.setRemark("下滑");
                break;
            case AppConstants.SCRIPT_STEP_RIGHTGLIDE:
                scriptStep.setActionType(AppConstants.SCRIPT_STEP_RIGHTGLIDE);
                scriptStep.setRemark("右滑");
                break;
            case AppConstants.SCRIPT_STEP_LEFTGLIDE:
                scriptStep.setActionType(AppConstants.SCRIPT_STEP_LEFTGLIDE);
                scriptStep.setRemark("左滑");
                break;
            case AppConstants.SCRIPT_STEP_BACK:
                scriptStep.setActionType(AppConstants.SCRIPT_STEP_BACK);
                scriptStep.setRemark("按返回键");
                break;
            // 点击坐标动作
            case AppConstants.SCRIPT_STEP_TAP:
                scriptStep.setActionType(AppConstants.SCRIPT_STEP_TAP);
                if (StringUtils.isNotEmpty(sb.getParam2()))
                {
                    scriptStep.setOrientType("0");
                    scriptStep.setOrientValue(sb.getParam1() + "," + sb.getParam2());
                    scriptStep.setRemark("点击坐标("+scriptStep.getOrientValue()+")");
                }
                else
                {
                    scriptStep.setParamValue("tap");
                    scriptStep.setParamValue(sb.getParam1());
                    scriptStep.setRemark("点击坐标" + sb.getParam1());
                }

                break;
            // 点击xpath动作
            case AppConstants.SCRIPT_STEP_CLICK:
                scriptStep.setActionType(AppConstants.SCRIPT_STEP_CLICK);
                scriptStep.setOrientType("1");
                scriptStep.setOrientValue(sb.getParam1());
                scriptStep.setRemark("点击"+getBtnNameFromXpath("=", sb.getParam1()));
                break;
            case AppConstants.SCRIPT_STEP_DOUBLECLICK:
                scriptStep.setActionType(AppConstants.SCRIPT_STEP_DOUBLECLICK);
                scriptStep.setOrientType("1");
                scriptStep.setOrientValue(sb.getParam1());
                scriptStep.setRemark("双击"+getBtnNameFromXpath("=", sb.getParam1()));
                break;
            case AppConstants.SCRIPT_STEP_LONGPRESS:
                scriptStep.setActionType(AppConstants.SCRIPT_STEP_LONGPRESS);
                scriptStep.setOrientType("1");
                scriptStep.setOrientValue(sb.getParam1());
                scriptStep.setRemark("长按"+getBtnNameFromXpath("=", sb.getParam1()));
                break;
            // 图像识别点击动作
            case AppConstants.SCRIPT_STEP_PICTURETAP:
                scriptStep.setActionType(AppConstants.SCRIPT_STEP_PICTURETAP);
                scriptStep.setOrientType("0");
                scriptStep.setOrientValue(sb.getParam1() + "," + sb.getParam2());
                scriptStep.setParamKey("pictureTap");
                scriptStep.setParamValue(sb.getParam3());
                scriptStep.setRemark("图像识别");
                break;
            // if 判定
            case AppConstants.SCRIPT_STEP_IFJUDGE:
                scriptStep.setActionType(AppConstants.SCRIPT_STEP_IFJUDGE);
                if (sb.isExistStart())
                {
                    scriptStep.setParamKey(AppConstants.SCRIPT_CMD_START);
                    // param2中保存的是if判定条件
                    scriptStep.setParamValue(sb.getParam2());
                    scriptStep.setRemark("判断条件：" + sb.getParam2());
                }
                else if (sb.isExistEnd())
                {
                    scriptStep.setParamKey(AppConstants.SCRIPT_CMD_END);
                    scriptStep.setRemark("判断结束");
                }
                break;
            // for 循环
            case AppConstants.SCRIPT_STEP_FORLOOP:
                scriptStep.setActionType(AppConstants.SCRIPT_STEP_FORLOOP);
                if (sb.isExistStart())
                {
                    scriptStep.setParamKey(AppConstants.SCRIPT_CMD_START);
                    // param2中保存的是for循环次数
                    scriptStep.setParamValue(sb.getParam2());
                    scriptStep.setRemark("开始循环：" + sb.getParam2() + "次");
                }
                else if (sb.isExistEnd())
                {
                    scriptStep.setParamKey(AppConstants.SCRIPT_CMD_END);
                    scriptStep.setRemark("循环结束");
                }
                break;
            case AppConstants.SCRIPT_STEP_AFFAIR:
                scriptStep.setActionType(AppConstants.SCRIPT_STEP_AFFAIR);
                if (sb.isExistStart())
                {
                    scriptStep.setParamKey(AppConstants.SCRIPT_CMD_START);
                    scriptStep.setRemark("事务开始");
                }
                else if (sb.isExistEnd())
                {
                    scriptStep.setParamKey(AppConstants.SCRIPT_CMD_END);
                    // param2 保存的是事物结束控件的xpath
                    scriptStep.setOrientType("1");
                    scriptStep.setOrientValue(sb.getParam2());
                    scriptStep.setRemark("事务结束");
                }
                break;
            // 输入动作
            case AppConstants.SCRIPT_STEP_SENDTEXT:
                scriptStep.setActionType(AppConstants.SCRIPT_STEP_SENDTEXT);
                // param1 输入控件的xpath
                scriptStep.setOrientType("1");
                scriptStep.setOrientValue(sb.getParam1());
                // 输入的值
                scriptStep.setParamValue(sb.getParam2());
                // 输入类型
                //scriptStep.setParamType(sb.getParam3());
                scriptStep.setParamKey(sb.getParam3());
                if ("0".equals(sb.getParam3()))
                {
                    scriptStep.setRemark("输入账号");
                }
                else if ("1".equals(sb.getParam3()))
                {
                    scriptStep.setRemark("输入密码");
                }
                else if ("8".equals(sb.getParam3()))
                {
                    scriptStep.setRemark("输入动态验证码");
                }
                else if ("-1".equals(sb.getParam3()))
                {
                    scriptStep.setRemark("普通文本输入");
                }
                break;
            // 输入动态密码
            case AppConstants.SCRIPT_STEP_DYNAMIC:
                scriptStep.setActionType(AppConstants.SCRIPT_STEP_DYNAMIC);
                scriptStep.setOrientType("1");
                scriptStep.setOrientValue(sb.getParam1());

                scriptStep.setParamValue(sb.getParam2());
                //scriptStep.setParamType(sb.getParam3());
                scriptStep.setParamKey(sb.getParam3());
                scriptStep.setRemark("输入动态验证码");
                break;
            // 短信校验
            case AppConstants.SCRIPT_STEP_SMSVARIFY:
                scriptStep.setActionType(AppConstants.SCRIPT_STEP_SMSVARIFY);
                //scriptStep.setParamValue(sb.getParam1());
                scriptStep.setParamKey(sb.getParam1());
                scriptStep.setRemark("校验短信内容");
                break;
            // 内容校验
            case AppConstants.SCRIPT_STEP_VERIFY:
                scriptStep.setActionType(AppConstants.SCRIPT_STEP_VERIFY);
                //scriptStep.setParamValue(sb.getParam1());
                scriptStep.setParamKey(sb.getParam1());
                scriptStep.setRemark("校验内容");
                break;
            default:
                LOGGER.error("无法找到匹配项!");
                break;
        }
    }

    @Override
    public Map<String, Object> getAppScriptById(Integer id) {
        Map<String, Object> map = new HashMap<>();
        ScriptVO scriptVo = appScriptMapper.getScriptInfoById(id);
        if (scriptVo == null)
        {
            throw new TestSystemException("该脚本不存在");
        }
        List<AppScriptStep> stepList = appScriptStepService.getStepByScriptFileId(scriptVo.getScriptFileId());
        scriptVo.setStepList(stepFormatToVO(stepList));
        map.put("script", scriptVo);
        return map;
    }

    /**
     * 获取xpath定位的按钮名称
     */
    private String getBtnNameFromXpath(String regStr, String xpath)
    {
        if (StringUtils.isNotEmptyAll(regStr, xpath))
        {
            int index = xpath.lastIndexOf(regStr) + 1;

            if (xpath.trim().length() > 3)
            {
                if ("\'".equals(xpath.substring(index, index + 1)))
                {
                    return xpath.substring(index + 1, xpath.trim().length() - 2);
                }
                else
                {
                    return xpath.substring(index + 6, xpath.trim().length() - 7);
                }
            }
        }
        return null;
    }

    /**
     * 脚本步骤格式化
     * @return
     */
    private List<AppStepVO> stepFormatToVO(List<AppScriptStep> stepList)
    {
        List<AppStepVO> list = new ArrayList<>();

        for (AppScriptStep step : stepList)
        {
            AppStepVO vo = new AppStepVO();
            String actionType = step.getActionType();
            switch (actionType)
            {
                //休眠等待
                case AppConstants.SCRIPT_STEP_SLEEP:
                    vo.setActionType(AppConstants.SCRIPT_STEP_SLEEP);
                    //vo.setParamValue(step.getInputValue());
                    vo.setParamKey(step.getInputValue());
                    vo.setRemark(step.getStepDesc());
                    break;
                // 上滑、下滑、右滑、左滑、返回
                case AppConstants.SCRIPT_STEP_UPGLIDE:
                case AppConstants.SCRIPT_STEP_DOWNGLIDE:
                case AppConstants.SCRIPT_STEP_RIGHTGLIDE:
                case AppConstants.SCRIPT_STEP_LEFTGLIDE:
                case AppConstants.SCRIPT_STEP_BACK:
                    vo.setActionType(actionType);
                    vo.setRemark(step.getStepDesc());
                    break;
                // 点击坐标动作
                case AppConstants.SCRIPT_STEP_TAP:
                    vo.setActionType(AppConstants.SCRIPT_STEP_TAP);
                    if (StringUtils.isNotEmpty(step.getCoordinate()))
                    {
                        vo.setOrientType("0");
                        vo.setOrientValue(step.getCoordinate());
                    }
                    else
                    {
                        vo.setParamValue("tap");
                        vo.setParamValue(step.getActionText());
                    }
                    vo.setRemark(step.getStepDesc());
                    break;
                // 点击、双击、长按
                case AppConstants.SCRIPT_STEP_CLICK:
                case AppConstants.SCRIPT_STEP_DOUBLECLICK:
                case AppConstants.SCRIPT_STEP_LONGPRESS:
                    vo.setActionType(actionType);
                    vo.setOrientType("1");
                    vo.setOrientValue(step.getXpath());
                    vo.setRemark(step.getStepDesc());
                    break;
                // 图像识别点击动作
                case AppConstants.SCRIPT_STEP_PICTURETAP:
                    vo.setActionType(AppConstants.SCRIPT_STEP_PICTURETAP);
                    vo.setOrientType("0");
                    vo.setOrientValue(step.getCoordinate());
                    vo.setParamKey("pictureTap");
                    vo.setParamValue(step.getPicName());
                    vo.setRemark(step.getStepDesc());
                    break;
                // if 判定、for 循环
                case AppConstants.SCRIPT_STEP_IFJUDGE:
                case AppConstants.SCRIPT_STEP_FORLOOP:
                    vo.setActionType(actionType);
                    if ("start".equals(step.getActionText()))
                    {
                        vo.setParamKey(AppConstants.SCRIPT_CMD_START);
                        // param2中保存的是if判定条件
                        vo.setParamValue(step.getInputValue());
                    }
                    else if ("end".equals(step.getActionText()))
                    {
                        vo.setParamKey(AppConstants.SCRIPT_CMD_END);
                    }
                    vo.setRemark(step.getStepDesc());
                    break;
                // 事务
                case AppConstants.SCRIPT_STEP_AFFAIR:
                    vo.setActionType(AppConstants.SCRIPT_STEP_AFFAIR);
                    if ("start".equals(step.getActionText()))
                    {
                        vo.setParamKey(AppConstants.SCRIPT_CMD_START);
                    }
                    else if ("end".equals(step.getActionText()))
                    {
                        vo.setParamKey(AppConstants.SCRIPT_CMD_END);
                        // param2 保存的是事物结束控件的xpath
                        vo.setOrientType("1");
                        vo.setOrientValue(step.getXpath());
                    }
                    vo.setRemark(step.getStepDesc());
                    break;
                // 输入动作、输入动态密码
                case AppConstants.SCRIPT_STEP_SENDTEXT:
                case AppConstants.SCRIPT_STEP_DYNAMIC:
                    vo.setActionType(actionType);
                    vo.setOrientType("1");
                    vo.setOrientValue(step.getXpath());
                    vo.setParamValue(step.getInputValue());
                    //vo.setParamType(step.getStepCode());
                    vo.setParamKey(step.getStepCode());
                    vo.setRemark(step.getStepDesc());
                    break;
                // 短信校验
                case AppConstants.SCRIPT_STEP_SMSVARIFY:
                    vo.setActionType(AppConstants.SCRIPT_STEP_SMSVARIFY);
                    //vo.setParamValue(step.getInputValue());
                    vo.setParamKey(step.getInputValue());
                    vo.setRemark(step.getStepDesc());
                    break;
                // 内容校验
                case AppConstants.SCRIPT_STEP_VERIFY:
                    vo.setActionType(AppConstants.SCRIPT_STEP_VERIFY);
                    //vo.setParamValue(step.getInputValue());
                    vo.setParamKey(step.getInputValue());
                    vo.setRemark(step.getStepDesc());
                    break;
                default:
                    LOGGER.error("无法找到匹配项!");
                    break;
            }
            list.add(vo);
        }

        return list;
    }

    /**
     * 复制脚本
     * @param list
     * @param menuParentId
     * @return
     */
    @Override
    public boolean copy(List<SysPermission> list, int menuParentId) {
        SysPermission parentSysPermission = sysPermissionService.selectByPrimaryKey(menuParentId);
        for (SysPermission sysPermission: list) {
            this.copyDetail(sysPermission,parentSysPermission);
        }
        return true;
    }

    @Override
    @Transactional
    public int copyDetail(SysPermission sysPermission, SysPermission parentSysPermission) {
        AppScript appScript = appScriptMapper.selectByPrimaryKey(sysPermission.getDataId());
        appScript.setScriptName(sysPermission.getName());
        int oldId=appScript.getScriptId();
       int newBusinessId=parentSysPermission.getDataId();
        int scriptid=this.copyData(appScript,oldId,newBusinessId);
        sysPermission.setId(null);
        sysPermission.setDataId(scriptid);
        sysPermission.setParentId(parentSysPermission.getId());

        sysPermissionService.saveProjectPermission(sysPermission);


        return scriptid;

    }

    @Override
    public int copyData(AppScript appScript ,Integer oldId,Integer newBusinessId) {


        List<AppScriptFile> appfiles = appScriptFileMapper.listFileByscriptId(oldId);
        //AppScript appScript = appScriptMapper.selectByPrimaryKey(sysPermission.getDataId());
        //int oldId=appScript.getScriptId();
        //int newBusinessId=parentSysPermission.getDataId();
        //复制脚本复制脚本参数
        appScript.setBusinessId(newBusinessId);
        appScript.setScriptId(null);
        //String no=UUID.randomUUID().toString().substring(1,4);
        //System.out.print(no);
        appScript.setScriptName(appScript.getScriptName());
        //先下载 原本脚本的可执行jar包
       // if (StringUtils.isNotEmpty(appScript.getScriptPath()))
        byte[] sfileBytes = fileStorageOperate.downloadFile(appScript.getScriptPath());
        //存到临时目录
        if (sfileBytes==null||sfileBytes.length<1){
            throw new TestSystemException("当前脚本的可执行jar包不存在");

        }
        String szipPath =  ConstantUtil.SCRIPT_TEMP_DIR + System.currentTimeMillis() + File.separator + appScript.getScriptPath();
       if (StringUtils.isEmpty(szipPath)){
           throw new TestSystemException("临时文件保存失败");
       }
        File zfile = new File(szipPath);
        try {
            FileUtils.writeByteArrayToFile(zfile, sfileBytes);
        } catch (IOException e) {
           e.getMessage();
        }
        logger.error(szipPath);
        //重新上传 返回新的fastfds路径
        //String newszippath=fileStorageOperate.fileStorageUploadFile(zfile);
        //String newszippath=fileStorageOperate.fileStorageUploadFile(sfileBytes,appScript.getScriptPath());
        String newszippath=fileStorageOperate.fileStorageUploadFile(szipPath);
        logger.error(fileStorageOperate.fileStorageUploadFile(szipPath));
        if (StringUtils.isEmpty(newszippath)){
            throw new TestSystemException("复制的脚本jar包上传失败");
        }
        //将新的脚本zip包放到复制的文件表 插入数据库
        appScript.setScriptPath(newszippath);
        appScript.setScriptFileName(appScript.getScriptFileName());
        appScriptMapper.insert(appScript);

        int scriptid=appScript.getScriptId();
        int scriptfileid =0;
        for (AppScriptFile appScriptFile : appfiles) {
            appScriptFile.setScriptId(scriptid);
            appScriptFile.setScriptFileId(null);
            appScriptFile.setAppName(appScriptFile.getAppName());
            //先下载 原本脚本的可执行jar包
            byte[] fileBytes = fileStorageOperate.downloadFile(appScriptFile.getExecuteJarUrl());
            if (sfileBytes==null||sfileBytes.length<1){
                throw new TestSystemException("当前脚本文件的可执行jar包不存在");
            }
            //存到临时目录
            String jarPath = ConstantUtil.SCRIPT_TEMP_DIR + System.currentTimeMillis() + File.separator + appScriptFile.getExecuteJarUrl();
            //File apkFile = new File(zipBasePath + File.separator + apkFilePath);

            File file = new File(jarPath);
            try {
                FileUtils.writeByteArrayToFile(file, fileBytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //重新上传 返回新的fastfds路径
            String newJarpath=fileStorageOperate.fileStorageUploadFile(jarPath);
            if (StringUtils.isEmpty(newszippath)){
                throw new TestSystemException("复制脚本文件的jar包上传失败");
            }
            //将新的可执行jar包放到复制的文件表 插入数据库
            appScriptFile.setExecuteJarUrl(newJarpath);
            //生成新的md5值
            String  newMd5  =  Md5Utils.getFileMD5Str(jarPath);
            appScriptFile.setMd5Code(newMd5);
            appScriptFileMapper.insert(appScriptFile);
            scriptfileid =appScriptFile.getScriptFileId();
        }
        List<AppScriptStep> appScriptSteps = appScriptStepMapper.listStepByScriptId(oldId);
        for (AppScriptStep appscriptStep : appScriptSteps) {
            appscriptStep.setScriptStepId(null);
            appscriptStep.setScriptFileId(scriptfileid);
            appscriptStep.setScriptId(scriptid);
            appScriptStepMapper.insert( appscriptStep);


        }
       return  scriptid;
    }

    /**
     * 剪切脚本
     * @param list 剪切的菜单list
     * @param menuParentId 剪切到目标对象的id
     * @return
     */
    @Override
    public boolean shear(List<SysPermission> list, int menuParentId) {
        int sysResult = 0;
        int scriptResult = 0;
        SysPermission parentSysPermission = sysPermissionService.selectByPrimaryKey(menuParentId);
        for (SysPermission sysPermission: list) {
            //更新脚本菜单
            sysPermission.setParentId(menuParentId);
            sysResult = sysPermissionService.updateByPrimaryKey(sysPermission);
            //更新脚本数据
           AppScript appScript = appScriptMapper.selectByPrimaryKey(sysPermission.getDataId());
            appScript.setBusinessId(parentSysPermission.getDataId());
            appScript.setOrderCode(sysPermission.getCode());
            scriptResult = appScriptMapper.updateByPrimaryKey(appScript);
        }
        return (sysResult > 0 && scriptResult > 0);
    }

    @Override
    public AppScript getScriptById(Integer scriptId) {
        return appScriptMapper.selectScriptById(scriptId);
    }

    @Override
    public int updateByscriptId(AppScript appScript) {
        return appScriptMapper.updateByPrimaryKeySelective(appScript);
    }
}
