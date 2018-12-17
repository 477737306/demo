package com.cmit.testing.service.impl;

import com.alibaba.fastjson.JSON;
import com.cmit.testing.dao.OrderRelationshipMapper;
import com.cmit.testing.dao.SimCardMapper;
import com.cmit.testing.entity.SimCard;
import com.cmit.testing.page.PageBean;
import com.cmit.testing.service.SimCardService;
import com.cmit.testing.utils.PoiUtils;
import com.cmit.testing.utils.StringUtils;
import com.cmit.testing.utils.verify.HttpTest;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by Suviky on 2018/7/18 17:33 c
 */
@Service
public class SimCardServiceImpl extends BaseServiceImpl<SimCard> implements SimCardService {
    @Autowired
    private SimCardMapper simCardMapper;

    @Autowired
    private OrderRelationshipMapper orderRelationshipMapper;
    @Resource(name = "poolExecutor")
    private ThreadPoolExecutor executor;

    @Override
    public int deleteByIds(List<Integer> ids) {
        return 0;
    }

    @Override
    public List<SimCard> getRecords(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return simCardMapper.getAllRecords();
    }

    /**
     * 分页查询所有sim卡
     *
     * @param pageBean
     * @param simCard
     * @return
     */
    @Override
    public PageBean<SimCard> findPageRecordsBySimCard(PageBean<SimCard> pageBean, SimCard simCard) {
        Page page = PageHelper.startPage(pageBean.getCurrentPage(), pageBean.getPageSize());
        List<SimCard> list = simCardMapper.findByPage(simCard);
        pageBean.setTotalNum((int) page.getTotal());

        pageBean.setItems(list);
        return pageBean;
    }

    @Override
    public List<SimCard> getAllRecordsBySimCard(SimCard simCard) {
        //业务筛选，支持多个办理或不办理。
        List<SimCard> list = new ArrayList<>();
        List<Map<String, Object>> orderStatusList = simCard.getOrderStatusList();
        List<String> productList = new ArrayList<>();//已办理产品名称
        List<String> noProductList = new ArrayList<>();//未办理产品名称

        if (orderStatusList != null && orderStatusList.size() > 0) {
            for (int i = 0; i < orderStatusList.size(); i++) {
                if (null != orderStatusList.get(i).get("orderStatus") && orderStatusList.get(i).get("orderStatus").equals(0)) {//未办理
                    noProductList.add((String) orderStatusList.get(i).get("product"));
                } else {//已办理
                    productList.add((String) orderStatusList.get(i).get("product"));
                }
            }
            List<SimCard> list_1 = new ArrayList<>();//已办理
            List<SimCard> list_2 = new ArrayList<>();//未办理
            if (productList.size() > 0) {
                simCard.setProduct(productList);
                simCard.setStatus(1);
                list_1 = simCardMapper.getAllRecordsBySimCard(simCard);
            }
            if (noProductList.size() > 0) {
                simCard.setProduct(noProductList);
                simCard.setStatus(0);
                list_2 = simCardMapper.getAllRecordsBySimCard(simCard);
            }
            if (productList.size() > 0 && productList.size() > 0) {//求交集
                list_1.retainAll(list_2);
                list.addAll(list_1);
            } else {
                list.addAll(list_1);
                list.addAll(list_2);
            }
        } else {
            list = simCardMapper.getAllRecordsBySimCard(simCard);
        }
        return list;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateAllLocalData(String simcardIds) {
        Date currentDate = new Date();
        SimCard simCard2 = new SimCard();
        List<SimCard> list = new ArrayList<>();
        if (StringUtils.isNotEmpty(simcardIds)) {
            List<String> ids = Arrays.asList(simcardIds.split(","));
            list = simCardMapper.queryByIds(ids);
        } else {
            list = simCardMapper.getAllRecordsBySimCard(simCard2);
        }
        if (list != null && list.size() > 0) {
            List<Future> lists = new ArrayList<>();
            for (SimCard simCard1 : list) {
                executor.execute(() -> {
                    Future<Integer> future = executor.submit(() -> {
                        updateSimCardByPhone(simCard1, currentDate);
                        return 0;
                    });
                    lists.add(future);
                });
            }
            for (int i = 0; i < lists.size(); i++) {
                try {
                    lists.get(i).get();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void updateSimCardByPhone(SimCard simCard1, Date currentDate) {
        try {
            String timestamp = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
            String authcode = HttpTest.getAuthCode(simCard1.getPhone(), timestamp);
            String balanceResult = HttpTest.getBalance(simCard1.getPhone(), "01", authcode, timestamp);
            Map<String, Object> map = JSON.parseObject(balanceResult);
            if (null != map.get("bizCode") && "1".equals(String.valueOf(map.get("bizCode")))) {
                String balance = String.valueOf(map.get("balance"));
                if (StringUtils.isNotEmpty(balance)) {
                    simCard1.setBalance(Double.parseDouble(balance));
                    if(Double.parseDouble(balance) < 0){
                        simCard1.setAvailableStatus(0);
                    }
                }
            }
            String userResult = HttpTest.getUserinfo(simCard1.getPhone(), "01", authcode, timestamp);
            Map<String, Object> map1 = JSON.parseObject(userResult);
            if (null != map1.get("bizCode") && "1".equals(String.valueOf(map1.get("bizCode")))) {
                String billValueStr = String.valueOf(map1.get("billValue"));
                String usedValueStr = String.valueOf(map1.get("usedValue"));

                if (StringUtils.isNotEmpty(billValueStr) && StringUtils.isNotEmpty(usedValueStr)) {
                    double billValue = Double.parseDouble(billValueStr);//总流量
                    double usedValue = Double.parseDouble(usedValueStr);//已使用流量
                    double flow = billValue - usedValue;  //剩下的流量
                    simCard1.setFlow(flow);
                }
            }
            String integralResult = HttpTest.getIntegral(simCard1.getPhone(), "01", authcode, timestamp);
            Map<String, Object> map2 = JSON.parseObject(integralResult);
            if (null != map2.get("bizCode") && "1".equals(String.valueOf(map2.get("bizCode")))) {
                String totalPoint = String.valueOf(map2.get("totalPoint"));//总积分
                String pointBalance = String.valueOf(map2.get("pointBalance"));//用户可用积分
                if (map2.get("pointTypeInfo") != null) {
                    List<Map<String, String>> list = (List<Map<String, String>>) map2.get("pointTypeInfo");
                    list.forEach(e -> {
                        if (e.get("pointType").equals("01")) {
                            //消费积分 consumePoint
                            simCard1.setConsumePoint(String.valueOf(e.get("pointSum")));
                        }
                        if (e.get("pointType").equals("02")) {
                            //促销积分 promotionPoint
                            simCard1.setPromotionPoint(String.valueOf(e.get("pointSum")));
                        }
                    });
                }
                if (StringUtils.isNotEmpty(totalPoint)) {
                    simCard1.setTotalPoint(Double.parseDouble(totalPoint));
                }
                if (StringUtils.isNotEmpty(pointBalance)) {
                    simCard1.setPointBalance(Double.parseDouble(pointBalance));
                }
            }
            simCard1.setSynchronizedTime(currentDate);
            simCardMapper.updateByPrimaryKey(simCard1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int deleteByPrimaryKey(Integer id) {
        SimCard simCard = simCardMapper.selectByPrimaryKey(id);
        orderRelationshipMapper.removeByPhone(simCard.getPhone());
        return simCardMapper.deleteByPrimaryKey(simCard.getId());
    }

    @Override
    public void importExcel(MultipartFile multipartFile, int startRow, int endRow) throws IOException {
        SimpleDateFormat sdf = new SimpleDateFormat(" yyyy-MM-dd HH:mm:ss ");
        // 判断文件是否存在
        String originalFilename = multipartFile.getOriginalFilename();
        if (multipartFile == null) {
            throw new IOException("Excel文件不存在！");
        }
        Workbook wb = null;
        InputStream fis = null;
        List<Row> rowList = new ArrayList<Row>();
        try {
            // 去读Excel
            if (originalFilename.endsWith("xls")) {
                wb = new HSSFWorkbook(multipartFile.getInputStream());
            } else if (originalFilename.endsWith("xlsx")) {
                wb = new XSSFWorkbook(multipartFile.getInputStream());
            } else {

            }
            Sheet sheet = wb.getSheetAt(0);
            // 获取最后行号
            int lastRowNum = sheet.getLastRowNum();
            if (lastRowNum > 0) { // 如果>0，表示有数据
                logger.info("\n开始读取名为【" + sheet.getSheetName() + "】的内容：");
            }
            List<SimCard> simList = new ArrayList<>();
            Row row = null;
            // 循环读取
            for (int i = startRow; i <= lastRowNum + endRow; i++) {
                SimCard simCard = new SimCard();
                row = sheet.getRow(i);
                if (row != null) {
                    rowList.add(row);
                    // 获取每一单元格的值
                    for (int j = 0; j <= 16; j++) {
                        Cell cell = row.getCell(j);
                        if (cell != null) {
                            String cellValue = PoiUtils.readCellVal(cell);
                            switch (j) {
                                case 0:
                                    simCard.setId(Integer.parseInt(cellValue));
                                    break;
                                case 1:
                                    simCard.setOperator(cellValue);
                                    break;
                                case 2:
                                    simCard.setProvince(cellValue);
                                    break;
                                case 3:
                                    simCard.setBrand(cellValue);
                                    break;
                                case 4:
                                    simCard.setPhone(cellValue);
                                    break;
                                case 5:
                                    simCard.setServiceCode(cellValue);
                                    break;
                                case 6:
                                    simCard.setImsi(cellValue);
                                    break;
                                case 7:
                                    simCard.setMonthCost(Double.parseDouble(cellValue));
                                    break;
                                case 8:
                                    simCard.setMonthCostInfo(cellValue);
                                    break;
                                case 9:
                                    simCard.setStatus(Integer.parseInt(cellValue));
                                    break;
                                case 10:
                                    simCard.setCancelTime(sdf.parse(cellValue));
                                    break;
                                case 11:
                                    simCard.setSimHost(cellValue);
                                    break;
                                case 12:
                                    simCard.setContactInfo(cellValue);
                                    break;
                                case 13:
                                    simCard.setDescription(cellValue);
                                    break;
                                case 14:
                                    simCard.setBalance(Double.parseDouble(cellValue));
                                    break;
                                case 15:
                                    simCard.setFlow(Double.parseDouble(cellValue));
                                    break;
                                //case 16 :simCard.setFlow(Double.parseDouble(cellValue));break;
                                default:
                                    break;
                            }
                        }
                    }
                    simList.add(simCard);
                }
            }
            for (SimCard simcard : simList) {
                SimCard byPhone = simCardMapper.getByPhone(simcard.getPhone());
                if (byPhone == null) {
                    simCardMapper.insert(simcard);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
            wb.close();
        }
    }

    @Override
    public void exportExcel(SimCard simCard) {

    }

    @Override
    public SimCard getByPhone(String phone) {
        return simCardMapper.getByPhone(phone);
    }
}
