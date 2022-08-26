package com.reggie.Controller;

import com.reggie.Common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RestController
public class NoLoginController {

    @PostMapping("/notlogin")
    public R notlogin() {
        return R.error("NOTLOGIN");
    }

    @GetMapping("/notlogin")
    public R notlogin2() {
        return R.error("NOTLOGIN");
    }

    @RequestMapping("/resourceNotFound")
    public R resourceNotFound() {
        return R.error("没有找到资源");
    }

}
