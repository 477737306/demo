package com.cmit.testing.web;

import com.cmit.testing.common.annotion.Permission;
import com.cmit.testing.service.ProductService;
import com.cmit.testing.utils.JsonResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author weiBin
 * @date 2018/9/20
 */
@RestController
@RequestMapping("/api/v1/product")
@Permission
public class ProductController extends BaseController{




}
