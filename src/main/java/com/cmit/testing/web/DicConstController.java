package com.cmit.testing.web;

import java.util.List;
import java.util.Map;

import javax.ws.rs.QueryParam;

import com.cmit.testing.common.annotion.SystemLog;
import com.cmit.testing.entity.SysUser;
import com.cmit.testing.security.shiro.ShiroUser;
import com.cmit.testing.service.impl.DicConstServiceImpl;
import com.cmit.testing.utils.MyBeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.cmit.testing.common.annotion.Permission;
import com.cmit.testing.entity.DicConst;
import com.cmit.testing.service.DicConstService;
import com.cmit.testing.utils.JsonResultUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@RestController
@RequestMapping(value = "/api/v1/dicconst")
@SystemLog("字典模块")
@Permission
public class DicConstController extends BaseController {

	@Autowired
	private DicConstService constService;
	
	/**
	 * 字典新增
	 * @param map
	 * @return
	 */

	@RequestMapping(value = "/add",method = RequestMethod.POST)
	public JsonResultUtil insert(@RequestBody Map<String,String> map){
		if(constService.insert(map) > 0)
		return new JsonResultUtil(200,"新增成功");
		return new JsonResultUtil(300,"新增失败,请重试或联系管理员");
	}
	
	/**
	 * 字典修改
	 * @param record
	 * @return
	 */
	@RequestMapping(value = "/update/{id}" ,method = RequestMethod.PUT)
	public JsonResultUtil update(@PathVariable("id") Integer id,@RequestBody DicConst record){
		record.setId(id);
		if(constService.updateByPrimaryKey(record) > 0)
			return new JsonResultUtil(200,"修改成功");
		return new JsonResultUtil(300,"修改失败,请重试或联系管理员");
	}
	
	/**
	 * 字典批量删除
	 * @param map
	 * @return
	 */

	@RequestMapping(value = "/delete" ,method = RequestMethod.POST)
	public JsonResultUtil deleteByIds(@RequestBody Map<String,Object> map){
		List<Integer> ids = (List<Integer>)map.get("ids");
		constService.deleteByIds(ids);
		return new JsonResultUtil(200,"操作成功");
	}
	
	/**
	 * 字典分页
	 * @param currentPage
	 * @param pageSize
	 * @param dvale
	 * @param dname
	 * @param dtype
	 * @param dparentCode
	 * @param status
	 * @param remark
	 * @return
	 */

    @RequestMapping(value = "/findPage", method = RequestMethod.GET)
	public JsonResultUtil findPage(@QueryParam("currentPage")Integer currentPage,
            @QueryParam("pageSize")Integer pageSize,
            @QueryParam("dvale")String dvale,
            @QueryParam("dname")String dname,
            @QueryParam("dtype")String dtype,
            @QueryParam("dparentCode")String dparentCode,
			@QueryParam("status")Integer status,
			@QueryParam("remark")String remark){
		return new JsonResultUtil(200,"操作成功",constService.findByPage(currentPage, pageSize, dvale, dname, dtype, dparentCode, status, remark));
	}

	/**
	 * 根据主键Id获取数据
	 * @return
	 */
	@RequestMapping(value = "/findById", method = RequestMethod.GET)
	public JsonResultUtil get(Integer id){
		return new JsonResultUtil(200,"操作成功", constService.selectByPrimaryKey(id));
	}

	/**
	 * 根据条件查询  返回json格式数组 下拉框键值对
	 * @param record
	 * @return
	 */

    @RequestMapping(value = "/findDicConst", method = RequestMethod.GET)
	public JsonResultUtil findDicConstByParam(DicConst record){
		List<DicConst> list = constService.findDicConstByParam(record);
		JSONArray jry = new JSONArray();
		if(list!=null){
			for (DicConst ds : list) {
				JSONObject j = new JSONObject();
				j.element("dname", ds.getDname());
				j.element("dvalue", ds.getDvalue());
				jry.add(j);
			}
		}
		return new JsonResultUtil(200,"操作成功",jry);
	}

}
