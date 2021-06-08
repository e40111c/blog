package com.example.blog.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice //攔截錯誤
public class ControllerExceptionHandler {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler(Exception.class) // 第一個註解表示這是攔截用的handler , 第二所有Exception類型的class
    public ModelAndView exceptionHandler(HttpServletRequest request , Exception e) throws Exception {
        logger.error("Request URL :  {},Exception : {}",request.getRequestURI(),e); //後面的get會傳到前面的request {}

        //判斷丟出的Exception是否含有標注ResponseStatus，有的話另外處理
        if (AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class) != null) {
            throw e;
        }

        ModelAndView mv = new ModelAndView(); //建立顯示畫面物件
        mv.addObject("url",request.getRequestURI());
        mv.addObject("exception",e);
        mv.setViewName("error/error"); //返回至error底下的error頁面顯示
        return mv;
    }
}
