package com.cmit.testing.service;

import com.cmit.testing.entity.SimCard;
import com.cmit.testing.page.PageBean;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * Created by Suviky on 2018/7/18 17:33
 */
public interface SimCardService extends BaseService<SimCard> {

    /**
     * 根据sim卡Id批量删除
     *
     * @param ids
     * @return
     */
    int deleteByIds(List<Integer> ids);

    void updateAllLocalData(String simcardIds);

    List<SimCard> getRecords(Integer pageNum, Integer pageSize);

    /**
     * 根据 SimCard查询对应的数据
     *
     * @param record
     * @return
     */
    PageBean<SimCard> findPageRecordsBySimCard(PageBean<SimCard> pageBean, SimCard record);

    /**
     * 根据simCard条件查询
     *
     * @param simCard
     * @return
     */
    List<SimCard> getAllRecordsBySimCard(SimCard simCard);

    /**
     * 导入数据
     *
     * @param multipartFile
     * @param startRow
     * @param endRow
     * @return
     */
    void importExcel(MultipartFile multipartFile, int startRow, int endRow) throws IOException;

    /**
     * 导出数据
     *
     * @param simCard
     * @return
     */
    void exportExcel(SimCard simCard);

    /**
     * 根据 phone查询对应的数据
     *
     * @param phone
     * @return
     */
    SimCard getByPhone(String phone);
}
