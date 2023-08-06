package com.naown.excel.autoconfigure;

import com.naown.excel.config.ExcelHandlerConfiguration;
import com.naown.excel.hanbler.ResponseExcelReturnValueHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @date: 2023/7/24 22:39
 * @author: chenjian
 */
@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
@Import(ExcelHandlerConfiguration.class)
@ConditionalOnBean(RequestMappingHandlerAdapter.class)
public class ExcelAutoConfigure implements InitializingBean {

    private final RequestMappingHandlerAdapter requestMappingHandlerAdapter;

    private final ResponseExcelReturnValueHandler responseExcelReturnValueHandler;

    @Override
    public void afterPropertiesSet() {
        List<HandlerMethodReturnValueHandler> resolverList = new ArrayList<>();
        resolverList.add(responseExcelReturnValueHandler);
        resolverList.addAll(Objects.requireNonNull(requestMappingHandlerAdapter.getReturnValueHandlers()));
        requestMappingHandlerAdapter.setReturnValueHandlers(resolverList);
    }
}