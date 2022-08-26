package com.reggie.Controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.reggie.Common.R;
import com.reggie.Entity.User;
import com.reggie.Service.UserService;
import com.reggie.Utils.SmsUtils;
import com.reggie.Utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession httpSession) {
        String phone = user.getPhone();
        if (StringUtils.isNotEmpty(phone)){
            String code = ValidateCodeUtils.generateValidateCode4String(6);
            //手机号 验证码 过期时间（还没做）
            SmsUtils.singleSend(phone,code,"10");

            httpSession.setAttribute(phone,code);

            R.success("手机验证码短信发送成功");

        }
        return R.error("验证码生成失败");
    }

    @PostMapping("/login")
    public R<User> login(@RequestBody Map map,HttpSession httpSession){
        String phone = (String) map.get("phone");
        String code = (String) map.get("code");
        String Scode = httpSession.getAttribute(phone).toString();
        if ( StringUtils.isNotEmpty(Scode) && code.equals(Scode)) {

            LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(User::getPhone, phone);

            User user = userService.getOne(lambdaQueryWrapper);
            //判断用户是否存在，如果不存在则为新用户，自动注册
            if (user == null){
                user = new User();
                user.setPhone(phone);
                user.setStatus(1);
                userService.save(user);
            }
            httpSession.setAttribute("user",user.getId());
            return R.success(user);

        }
        return R.error("登录失败");
    }
}
