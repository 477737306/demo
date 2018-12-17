package com.cmit.testing.dao;

import com.cmit.testing.entity.SimCard;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface SimCardMapper extends BaseMapper<SimCard> {

    List<SimCard> getAllRecords();

    /**
     * 根据 SimCard查询对应的数据
     *
     * @param record
     * @return
     */
    List<SimCard> getAllRecordsBySimCard(SimCard record);

    SimCard getSimCardByImsi(@Param("imsi") String imsi);

    int updateStatusByImsi(SimCard simCard);

    /**
     * 分页
     *
     * @param record
     * @return
     */
    List<SimCard> findByPage(SimCard record);


    /**
     * sim卡数据统计
     *
     * @return free：空闲，takeUp：占用，normal：正常，shutdown：停机
     */
    Map<String, Long> simCardCount();


    /**
     * 根据 phone查询对应的数据
     *
     * @param phone
     * @return
     */
    SimCard getByPhone(@Param("phone") String phone);

    List<SimCard> queryByIds(@Param("ids") List<String> ids);
}