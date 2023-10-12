package com.naown.excel.config;

import com.alibaba.excel.converters.Converter;
import com.naown.excel.aop.ExcelAspect;
import com.naown.excel.aop.ResponseExcelReturnValueHandler;
import com.naown.excel.hanbler.ManySheetWriteHandler;
import com.naown.excel.hanbler.SheetWriteHandler;
import com.naown.excel.hanbler.SingleSheetWriteHandler;
import com.naown.excel.head.EmptyHeadGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

import java.util.List;

/**
 * @author Hccake 2020/10/28
 * @version 1.0
 */
@RequiredArgsConstructor
public class ExcelHandlerConfiguration {

    private final ObjectProvider<List<Converter<?>>> converterProvider;

    /**
     * 单sheet 写入处理器
     */
    @Bean
    @ConditionalOnMissingBean(SingleSheetWriteHandler.class)
    public SingleSheetWriteHandler singleSheetWriteHandler() {
        return new SingleSheetWriteHandler(converterProvider);
    }

    @Bean
    @ConditionalOnMissingBean(ManySheetWriteHandler.class)
    public ManySheetWriteHandler manySheetWriteHandler() {
        return new ManySheetWriteHandler(converterProvider);
    }

    @Bean
    @ConditionalOnMissingBean(ExcelAspect.class)
    public ExcelAspect excelAspect() {
        return new ExcelAspect();
    }

    /**
     * 空的 Excel 头生成器
     *
     * @return EmptyHeadGenerator
     */
    @Bean
    @ConditionalOnMissingBean(EmptyHeadGenerator.class)
    public EmptyHeadGenerator emptyHeadGenerator() {
        return new EmptyHeadGenerator();
    }

    /**
     * 返回Excel文件的 response 处理器
     *
     * @param sheetWriteHandlers 页签写入处理器集合
     * @return ResponseExcelReturnValueHandler
     */
    @Bean
    @ConditionalOnMissingBean(ResponseExcelReturnValueHandler.class)
    public ResponseExcelReturnValueHandler excelReturnValueHandler(List<SheetWriteHandler> sheetWriteHandlers) {
        return new ResponseExcelReturnValueHandler(sheetWriteHandlers);
    }

}
