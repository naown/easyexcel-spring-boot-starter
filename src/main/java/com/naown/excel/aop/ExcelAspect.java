package com.naown.excel.aop;

import com.naown.excel.annotation.ResponseExcel;
import com.naown.excel.util.SpElUtils;
import com.naown.excel.util.WebUtils;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.util.StringUtils;

/**
 * @date: 2023/8/8 13:45
 * @author: chenjian
 */
@Aspect
@RequiredArgsConstructor
public class ExcelAspect {

    @Before("@annotation(excel)")
    public void before(JoinPoint point, ResponseExcel excel) {
        MethodSignature signature = (MethodSignature) point.getSignature();
        if (StringUtils.hasText(excel.exclude())) {
            String exclude = SpElUtils.parseSpEl(signature.getMethod(), point.getArgs(), excel.exclude());
            WebUtils.setAttribute("exclude", exclude);
        }
    }
}
