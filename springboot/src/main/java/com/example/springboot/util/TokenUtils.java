package com.example.springboot.util;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.springboot.entity.Admin;
import com.example.springboot.service.IAdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/***
 * @description: JWT工具类TokenUtils.java 生成token
 * @author: yk
 **/
@Component
@Slf4j
public class TokenUtils {

    private static IAdminService staticAdminService;

    @Resource
    private IAdminService adminService;

    @PostConstruct
    public void setUserService() {
        //静态对象
        staticAdminService = adminService;
    }
    /**
     * 生成token
     */
    public static String genToken(String adminId, String sign) {
        return JWT.create().withAudience(adminId) // 将 user id 保存到 token 里面,作为载荷
                //可以通过设置token时间
//                .withExpiresAt(DateUtil.offsetSecond(new Date(), 5)) //5秒后token过期，然后跳转登录页
                .withExpiresAt(DateUtil.offsetHour(new Date(), 2)) // 2小时后token过期
                .sign(Algorithm.HMAC256(sign)); // 以 password 作为 token 的密钥
    }
    public static String genToken(String adminId, String sign,int days) {
        return JWT.create().withAudience(adminId) // 将 user id 保存到 token 里面,作为载荷
                .withExpiresAt(DateUtil.offsetDay(new Date(), days)) // 某天后token过期
                .sign(Algorithm.HMAC256(sign)); // 以 password 作为 token 的密钥
    }

    /**
     * 获取当前登录的用户信息
     *
     * @return user对象
     * 获取的参数信息
     *  /admin?token=xxxx
     */
    public static Admin getCurrentAdmin() {
        //定义token
        String token = null;
        try {
            //获取request
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            //第一种方式 从request中获取token
            token = request.getHeader("token");
            //如果上面header没有取到token 那就从参数获取，则采用下面一种方式
            if (StrUtil.isBlank(token)) {
                //第二种方式 从request中获取token
                token = request.getParameter("token");
            }
            //如果上面都没有获取到
            if (StrUtil.isBlank(token)) {
                //打印失败日志
                log.error("获取当前登录的token失败， token: {}", token);
                return null;
            }
            //通过JWT解开token，放在adminId中
            String adminId = JWT.decode(token).getAudience().get(0);
            //拿到adminId 最终返回admin信息
            return staticAdminService.getById(Integer.valueOf(adminId));
        } catch (Exception e) {
            log.error("获取当前登录的管理员信息失败, token={}", token,  e);
            return null;
        }
    }
}

