package com.cmit.testing.service.impl;

import com.cmit.testing.dao.OrderRelationshipMapper;
import com.cmit.testing.dao.ProductMapper;
import com.cmit.testing.dao.SimCardMapper;
import com.cmit.testing.entity.OrderRelationship;
import com.cmit.testing.entity.Product;
import com.cmit.testing.entity.SimCard;
import com.cmit.testing.page.PageBean;
import com.cmit.testing.service.OrderRelationshipService;
import com.cmit.testing.service.ProductService;
import com.cmit.testing.utils.JsonUtil;
import com.cmit.testing.utils.StringUtils;
import com.cmit.testing.utils.verify.HttpTest;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

@Service
public class OrderRelationshipServiceImpl extends BaseServiceImpl<OrderRelationship> implements OrderRelationshipService {
    //     01-套餐类 02增值业务类 03服务功能类 04营销活动等其他类
    private static String[] buyiTypes = {"01", /*"02",*/ "03", "04"};
    @Autowired
    private OrderRelationshipMapper orderRelationshipMapper;
    @Autowired
    private SimCardMapper simCardMapper;
    @Autowired
    private ProductService productService;
    @Resource(name = "poolExecutor")
    private ThreadPoolExecutor executor;

    @Override
    public PageBean<OrderRelationship> findByPage(PageBean<OrderRelationship> pageBean, OrderRelationship orderRelationship) {
        Page page = PageHelper.startPage(pageBean.getCurrentPage(), pageBean.getPageSize());
        List<OrderRelationship> list = orderRelationshipMapper.findByPage(orderRelationship);
        pageBean.setTotalNum((int) page.getTotal());
        pageBean.setItems(list);
        return pageBean;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateAllLocalData(String simCardIds) {
        Date currentDate = new Date();
        //查询全部的sim卡号
        List<SimCard> simCardList = null;
        if (StringUtils.isEmpty(simCardIds)) {
            simCardList = simCardMapper.getAllRecords();
        } else {
            List<String> ids = Arrays.asList(simCardIds.split(","));
            simCardList = simCardMapper.queryByIds(ids);
        }
        if (simCardList != null) {
            List<Future> list = new ArrayList<>();
            for (SimCard simCard : simCardList) {
                Future<Integer> future = executor.submit(() -> {
                    updateOrderByPhone(simCard.getPhone(), currentDate);
                    return 0;
                });
                list.add(future);
            }
            for (int i = 0; i < list.size(); i++) {
                try {
                    list.get(i).get();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 根据手机号码更新对应的订购关系
     *
     * @param phone
     */
    public void updateOrderByPhone(String phone, Date currentDate) {
        String timestamp = null;
        String authcode = null;
        try {
            timestamp = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
            authcode = HttpTest.getAuthCode(phone, timestamp);
            for (int i = 0; i < buyiTypes.length; i++) {
                List<OrderRelationship> result = new ArrayList<>();
                String resultJson = HttpTest.getOrderbusiness(phone, "01", buyiTypes[i], authcode, timestamp);
                Map<String, Object> map = JsonUtil.parseJsonObject(resultJson);
                String bizCode = String.valueOf(map.get("bizCode"));
                //请求成功
                if ("1".equals(bizCode)) {
                    List<Map<String, Object>> bizInfoList = (List<Map<String, Object>>) map.get("bizInfoList");
                    if (bizInfoList != null && bizInfoList.size() > 0) {
                        for (Map bizInfo : bizInfoList) {
                            String productType = (String) bizInfo.get("productType");
                            if ("01".equals(productType)) {
                                List<Map<String, Object>> productInfoList = (List<Map<String, Object>>) bizInfo.get("productInfo");
                                if (productInfoList == null || productInfoList.size() == 0) {
                                    continue;
                                }
                                for (Map productInfo : productInfoList) {
                                    String orderTime = (String) productInfo.get("orderTime");
                                    String validDate = (String) productInfo.get("validDate");
                                    String expireDate = (String) productInfo.get("expireDate");
                                    List<Map<String, Object>> productList = (List<Map<String, Object>>) productInfo.get("productList");
                                    if (productList == null || productList.size() == 0) {
                                        continue;
                                    }
                                    for (Map product : productList) {
                                        String productId = (String) product.get("productId");
                                        String productName = (String) product.get("productName");
                                        String productDesc = (String) product.get("productDesc");
//                                        System.out.println(String.format("套餐业务%s订购时间%s生效时间%s失效时间%s资费详情%s状态%s", productName, orderTime, validDate, expireDate, productDesc, "0"));
                                        OrderRelationship relationship = new OrderRelationship();
                                        relationship.setPhone(phone);
                                        if (StringUtils.isNotEmpty(orderTime)) {
                                            relationship.setCreateTime(DateUtils.parseDate(orderTime, new String[]{"yyyyMMddHHmmss"}));
                                        }
                                        if (StringUtils.isNotEmpty(expireDate)) {
                                            relationship.setEndTime(DateUtils.parseDate(expireDate, new String[]{"yyyyMMddHHmmss"}));
                                        }
                                        if (StringUtils.isNotEmpty(validDate)) {
                                            relationship.setStartTime(DateUtils.parseDate(validDate, new String[]{"yyyyMMddHHmmss"}));
                                        }
                                        relationship.setBusinessName(productName);
                                        relationship.setExpensesInfo(productDesc);
                                        relationship.setType(buyiTypes[i]);
                                        relationship.setState("0");
                                        //获取手机号对应的省
                                        String proId = getProductId(phone, productId, productName);
                                        relationship.setProductId(proId);
                                        relationship.setSynchronizedTime(currentDate);
                                        result.add(relationship);
                                    }
                                }
                            } else if ("02".equals(productType)) {
                                List<Map<String, Object>> productInfoList = (List<Map<String, Object>>) bizInfo.get("productInfo");
                                if (productInfoList.size() > 0) {

                                }
                            } else if ("03".equals(productType)) {
                                List<Map<String, Object>> productInfoList = (List<Map<String, Object>>) bizInfo.get("productInfo");
                                if (productInfoList.size() > 0) {
                                    for (Map productInfo : productInfoList) {
                                        String servFunId = (String) productInfo.get("servFunId");
                                        String servFunName = (String) productInfo.get("servFunName");
                                        String bizFee = (String) productInfo.get("bizFee");
                                        String orderTime = (String) productInfo.get("orderTime");
                                        String validDate = (String) productInfo.get("validDate");
                                        String expireDate = (String) productInfo.get("expireDate");
//                                        System.out.println(String.format("服务功能%s订购时间%s生效时间%s失效时间%s资费详情%s状态%s", servFunName, orderTime, validDate, expireDate, bizFee, "0"));
                                        OrderRelationship relationship = new OrderRelationship();
                                        relationship.setPhone(phone);
                                        if (StringUtils.isNotEmpty(orderTime)) {
                                            relationship.setCreateTime(DateUtils.parseDate(orderTime, new String[]{"yyyyMMddHHmmss"}));
                                        }
                                        if (StringUtils.isNotEmpty(expireDate)) {
                                            relationship.setEndTime(DateUtils.parseDate(expireDate, new String[]{"yyyyMMddHHmmss"}));
                                        }
                                        if (StringUtils.isNotEmpty(validDate)) {
                                            relationship.setStartTime(DateUtils.parseDate(validDate, new String[]{"yyyyMMddHHmmss"}));
                                        }
                                        relationship.setBusinessName(servFunName);
                                        relationship.setExpensesInfo(bizFee);
                                        relationship.setType(buyiTypes[i]);
                                        relationship.setState("0");
                                        //获取手机号对应的省
                                        String proId = getProductId(phone, servFunId, servFunName);
                                        relationship.setProductId(proId);
                                        relationship.setSynchronizedTime(currentDate);
                                        result.add(relationship);
                                    }
                                }
                            } else if ("04".equals(productType)) {
                                List<Map<String, Object>> productInfoList = (List<Map<String, Object>>) bizInfo.get("productInfo");
                                if (productInfoList != null && productInfoList.size() > 0) {
                                    for (Map productInfo : productInfoList) {
                                        String orderTime = (String) productInfo.get("orderTime");
                                        String validDate = (String) productInfo.get("validDate");
                                        String expireDate = (String) productInfo.get("expireDate");
                                        List<Map<String, Object>> actionList = (List<Map<String, Object>>) productInfo.get("actionList");
                                        if (actionList == null || actionList.size() == 0) {
                                            continue;
                                        }
                                        for (Map action : actionList) {
                                            String actionId = (String) action.get("actionId");
                                            String actionName = (String) action.get("actionName");
                                            String actionDesc = (String) action.get("actionDesc");
                                            OrderRelationship relationship = new OrderRelationship();
                                            relationship.setPhone(phone);
                                            if (StringUtils.isNotEmpty(orderTime)) {
                                                relationship.setCreateTime(DateUtils.parseDate(orderTime, new String[]{"yyyyMMddHHmmss"}));
                                            }
                                            if (StringUtils.isNotEmpty(expireDate)) {
                                                relationship.setEndTime(DateUtils.parseDate(expireDate, new String[]{"yyyyMMddHHmmss"}));
                                            }
                                            if (StringUtils.isNotEmpty(validDate)) {
                                                relationship.setStartTime(DateUtils.parseDate(validDate, new String[]{"yyyyMMddHHmmss"}));
                                            }
                                            relationship.setBusinessName(actionName);
                                            relationship.setExpensesInfo(actionDesc);
                                            relationship.setType(buyiTypes[i]);
                                            relationship.setState("0");
                                            //获取手机号对应的省
                                            String proId = getProductId(phone, actionId, actionName);
                                            relationship.setProductId(proId);
                                            relationship.setSynchronizedTime(currentDate);
                                            result.add(relationship);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                //查询一级能开数据成功
                if (result.size() > 0) {
                    orderRelationshipMapper.removeByPhoneAndType(phone, buyiTypes[i]);
                    for (OrderRelationship line : result) {
                        orderRelationshipMapper.insert(line);
                    }
                }
            }
        } catch (Exception e) {
//                e.printStackTrace();
        }
    }

    private String getProductId(String phone, String productId, String productName) {
        SimCard simCard = simCardMapper.getByPhone(phone);
        Product pro = new Product(productId, productName, simCard.getProvince());
        return productService.save(pro);
    }

    @Override
    public List<OrderRelationship> findByPhone(String phone) {
        return orderRelationshipMapper.findByPhone(phone);
    }
}
