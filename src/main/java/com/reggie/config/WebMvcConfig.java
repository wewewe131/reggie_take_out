package com.reggie.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reggie.Common.JacksonObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Slf4j
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        log.info("开始进行静态资源映射");
        registry.addResourceHandler("/backend/**").addResourceLocations("classpath:/backend/");
        registry.addResourceHandler("/front/**").addResourceLocations("classpath:/front/");

    }

    /**
     * 拓展mvc框架的消息转换器，处理序列化和反序列化
     * @param converters
     */
    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        log.info("拓展消息转换器...");
        //创建一个消息转换器
        MappingJackson2HttpMessageConverter jackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
        //设置对象转换器,将java对象转换为json字符串
        jackson2HttpMessageConverter.setObjectMapper(new JacksonObjectMapper());//添加自定义的转换器
        //将消息转换器添加到消息转换器列表中 converters.add(设置转换器使用的顺序，设置为0是最优先使用的,jackson2HttpMessageConverter);
        converters.add(0,jackson2HttpMessageConverter);
    }
}
