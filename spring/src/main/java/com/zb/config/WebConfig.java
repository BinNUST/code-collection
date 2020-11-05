/*
package com.zb.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.HandlerAdapter;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.util.ArrayList;
import java.util.List;

@Configuration
// 定义Spring MVC扫描的包
@ComponentScan(value = "com.zb.*", includeFilters = {@ComponentScan.Filter(type = FilterType.ANNOTATION, value = Controller.class)})
// 启动Srping MVC配置
//@EnableWebMvc
public class WebConfig {
    */
/**
     * 通过注解@Bean初始化视图解析器
     * @return ViewResolver 视图解析器
     *//*

    @Bean(name = "internalResourceViewResolver")
    public ViewResolver initViewResolver() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/jsp/");
        viewResolver.setSuffix(".jsp");
        return viewResolver;
    }

    */
/**
     * 初始化 RequestMappingHandlerAdapter，并加载HTTP的JSON转换器
     * @return RequestMappingHandlerAdapter 对象
     *//*

    @Bean(name = "requestMappingHandlerAdapter")
    public HandlerAdapter initRequestMappingHandlerAdapter() {
        // 创建 RequestMappingHandlerAdapter 适配器
        RequestMappingHandlerAdapter rmhd = new RequestMappingHandlerAdapter();
        // HTTP JSON 转换器
        MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter();
        // MappingJackson2HttpMessageConverter 接收 JSON 类型消息的转换
        // MediaType mediaType = MediaType.APPLICATION_JSON_UTF8; 原文
        MediaType mediaType = MediaType.APPLICATION_JSON;
        List<MediaType> mediaTypes = new ArrayList<>();
        mediaTypes.add(mediaType);
        // 加入转换器的支持类型
        jsonConverter.setSupportedMediaTypes(mediaTypes);
        // 给适配器加入 JSON 转换器
        rmhd.getMessageConverters().add(jsonConverter);
        return rmhd;
    }
}
*/
