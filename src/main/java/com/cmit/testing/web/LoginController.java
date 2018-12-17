package com.cmit.testing.web;

import com.cmit.testing.entity.SysUser;
import com.cmit.testing.exception.VcodeException;
import com.cmit.testing.security.shiro.MySessionManager;
import com.cmit.testing.service.SysUserService;
import com.cmit.testing.utils.*;
import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.UserAgent;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import sun.misc.BASE64Decoder;

import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 登录相关接口
 *
 * @author YangWanLi
 * @date 2018/7/9 16:29
 */
@Controller
@RequestMapping(value = "/api/v1/auth")
public class LoginController extends BaseController {

    @Autowired
    private SysUserService sysUserService;

    private static final String SKEY = "abcdefgh";//加密key
    private static final Charset CHARSET = Charset.forName("gb2312");//加密编码

    /**
     * 登录
     *
     * @param map
     * @return
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public JsonResultUtil login(@RequestBody Map<String, String> map, HttpServletRequest request) {
        String username = map.get("username");
        String password = map.get("password");
        //解密
        try {
            password = DESUtil.decryption(password.trim());
        } catch (Exception e) {
            e.printStackTrace();
        }
        String vcode = map.get("vcode");
        String pictureId = map.get("pictureId");

        Map<String, Object> result = new HashMap<>();

        SysUser sysUser = new SysUser();
        try {
            UsernamePasswordToken token = new UsernamePasswordToken(username, password);
            Subject subject = getSubJect();
            String session_vcode = (String) RedisUtil.getObject(pictureId);
            //清除验证码的Redis缓存
            RedisUtil.delkeyObject(pictureId);
            if (session_vcode == null || (!vcode.toLowerCase().equals(session_vcode.toLowerCase()))) {
                throw new VcodeException();
            }

            sysUser = sysUserService.getSysUserByAccount(username);
            if (sysUser != null && sysUser.getStatus() >= 5) {
                return new JsonResultUtil(300, "账户已被锁定，请联系管理员进行解锁");
            }

            subject.login(token);
        } catch (DisabledAccountException e) {
            return new JsonResultUtil(300, "账户已被禁用");
        } catch (AuthenticationException e) {
            if (sysUser != null) {
                //更改锁定状态
                sysUser.setStatus(sysUser.getStatus() + 1);
                sysUserService.updateByPrimaryKey(sysUser);
                return new JsonResultUtil(300, "用户名或密码错误,您还有" + (5 - sysUser.getStatus()) + "次尝试机会！");
            }
            return new JsonResultUtil(300, "用户名或密码错误！");
        } catch (VcodeException e) {
            return new JsonResultUtil(300, "验证码错误");
        }

        Session session = getSession();
        String token = session.getId().toString();

        //获取浏览器信息
        Browser browser = UserAgent.parseUserAgentString(request.getHeader("User-Agent")).getBrowser();
        String browserName = browser.getName();
        //获取访问ip
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        logger.info("ip------------------------" + ip);
        session.setAttribute("browserName", browserName);
        session.setAttribute("loginIp", ip);
        MySessionManager mySessionManager = new MySessionManager();
        mySessionManager.deletSession(token);
        System.out.println("跨域："+ip);
        result.put("user", getShiroUser());
        result.put("token", token);
        //更改锁定状态
        sysUser.setStatus(0);
        sysUserService.updateByPrimaryKey(sysUser);
        System.out.println("登录成功");
        // 执行到这里说明用户已登录成功
        return new JsonResultUtil(200, "登陆成功", result);
    }

    /**
     * 秘钥登录
     *
     * @param map
     * @return
     */
    @RequestMapping(value = "/loginWithSecretKey", method = RequestMethod.POST)
    @ResponseBody
    public JsonResultUtil loginNoVcode(@RequestBody Map<String, String> map, HttpServletRequest request) throws Exception {

        //秘钥
        String secretKey = map.get("secretKey");
        String usernameAndPassword = "";
        if (StringUtils.isNotEmpty(secretKey)) {
            secretKey=secretKey.trim();
            //解密
            try {
                usernameAndPassword = DESUtil.decrypt(secretKey, CHARSET, SKEY);
            } catch (IllegalBlockSizeException e) {
                e.printStackTrace();
                return new JsonResultUtil(300, "秘钥格式错误");
            }
        }
        String username = "";
        String password = "";
        if (usernameAndPassword.contains("@")) {
            String[] split = usernameAndPassword.split("@");
            username = split[0];
            password = split[1];
        }

        Map<String, Object> result = new HashMap<>();

        SysUser sysUser = new SysUser();
        try {
            UsernamePasswordToken token = new UsernamePasswordToken(username, password);
            Subject subject = getSubJect();

            sysUser = sysUserService.getSysUserByAccount(username);
            if (sysUser != null && sysUser.getStatus() >= 5) {
                return new JsonResultUtil(300, "账户已被锁定，请联系管理员进行解锁");
            }
            if (sysUser != null && sysUser.getSecretKey()!=null&&!sysUser.getSecretKey().equals(secretKey)) {
                return new JsonResultUtil(300, "秘钥错误");
            }

            subject.login(token);
        } catch (DisabledAccountException e) {
            return new JsonResultUtil(300, "账户已被禁用");
        } catch (AuthenticationException e) {
            if (sysUser != null) {
                //更改锁定状态
                sysUser.setStatus(sysUser.getStatus() + 1);
                sysUserService.updateByPrimaryKey(sysUser);
                return new JsonResultUtil(300, "用户名或密码错误,您还有" + (5 - sysUser.getStatus()) + "次尝试机会！");
            }
            return new JsonResultUtil(300, "用户名或密码错误！");
        }

        Session session = getSession();
        String token = session.getId().toString();

        //获取浏览器信息
        Browser browser = UserAgent.parseUserAgentString(request.getHeader("User-Agent")).getBrowser();
        String browserName = browser.getName();
        //获取访问ip
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        logger.info("ip------------------------" + ip);
        session.setAttribute("browserName", browserName);
        session.setAttribute("loginIp", ip);
//        MySessionManager mySessionManager = new MySessionManager();
//        mySessionManager.deletSession(token);

        result.put("user", getShiroUser());
        result.put("token", token);
        //更改锁定状态
        sysUser.setStatus(0);
        sysUserService.updateByPrimaryKey(sysUser);
        // 执行到这里说明用户已登录成功
        return new JsonResultUtil(200, "登陆成功", result);
    }


    /**
     * 退出登录
     */
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    @ResponseBody
    public JsonResultUtil logOut() {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return new JsonResultUtil(200, "退出登录");
    }

    /**
     * 验证码
     */
    @RequestMapping(value = "/vcode", method = RequestMethod.POST)
    @ResponseBody
    public JsonResultUtil getVcode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Map<String, Object> result = new HashMap<>();
        byte[] captchaChallengeAsJpeg = null;
        ByteArrayOutputStream jpegOutputStream = new ByteArrayOutputStream();
        //生成随机字串
        String verifyCode = VerifyCodeUtils.generateVerifyCode(4);
        //生成图片
        int w = 200, h = 80;
        VerifyCodeUtils.outputImage(w, h, jpegOutputStream, verifyCode);
        captchaChallengeAsJpeg = jpegOutputStream.toByteArray();
        String pictureId = UUID.randomUUID().toString();
        result.put("verify", captchaChallengeAsJpeg);
        result.put("pictureId", pictureId);
        RedisUtil.setObject(pictureId, verifyCode);
        return new JsonResultUtil(200, "操作成功", result);
    }


    public static String base64Encode(byte[] bytes) {
        return Base64.encodeBase64String(bytes);
    }

    public static byte[] base64Decode(String base64Code) throws Exception {
        return new BASE64Decoder().decodeBuffer(base64Code);
    }

    private static final String ALGORITHMSTR = "AES/ECB/PKCS5Padding";

    public static String aesDecryptByBytes(byte[] encryptBytes, String decryptKey) throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        kgen.init(128);

        Cipher cipher = Cipher.getInstance(ALGORITHMSTR);
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(decryptKey.getBytes(), "AES"));
        byte[] decryptBytes = cipher.doFinal(encryptBytes);

        return new String(decryptBytes);
    }

    public static String aesDecrypt(String encryptStr, String decryptKey) throws Exception {
        return aesDecryptByBytes(base64Decode(encryptStr), decryptKey);
    }

}
