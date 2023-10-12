package com.naown.excel.hanbler;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import com.alibaba.excel.write.builder.ExcelWriterSheetBuilder;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.naown.excel.annotation.ResponseExcel;
import com.naown.excel.annotation.Sheet;
import com.naown.excel.converters.LocalDateStringConverter;
import com.naown.excel.converters.LocalDateTimeStringConverter;
import com.naown.excel.head.HeadGenerator;
import com.naown.excel.head.HeadMeta;
import com.naown.excel.util.StringUtils;
import com.naown.excel.util.WebUtils;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

/**
 * @author lengleng
 * @author L.cm
 * @author Hccake
 * @date 2020/3/31
 */
@RequiredArgsConstructor
public abstract class AbstractSheetWriteHandler implements SheetWriteHandler, ApplicationContextAware {

    //private final ExcelConfigProperties configProperties;

    private final ObjectProvider<List<Converter<?>>> converterProvider;

    //private final WriterBuilderEnhancer excelWriterBuilderEnhance;

    private ApplicationContext applicationContext;

    private final Integer DEFAULT_SHEET_NO = 1;

    @Override
    public void export(Object o, HttpServletResponse response, ResponseExcel responseExcel) {
        //check(responseExcel);
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        // TODO 表格名称取值在考虑
        String name = Instant.now().toString();
        String fileName = String.format("%s%s", URLEncoder
                .encode(name, StandardCharsets.UTF_8), responseExcel.suffix().getValue());
        // 根据实际的文件类型找到对应的 contentType
        String contentType = MediaTypeFactory.getMediaType(fileName)
                .map(MediaType::toString).orElse("application/vnd.ms-excel");
        response.setContentType(contentType);
        response.setCharacterEncoding("utf-8");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename*=utf-8''" + fileName);
        write(o, response, responseExcel);
    }

    /**
     * 通用的获取ExcelWriter方法
     *
     * @param response      HttpServletResponse
     * @param responseExcel ResponseExcel注解
     * @return ExcelWriter
     */
    @SneakyThrows(IOException.class)
    public ExcelWriter getExcelWriter(HttpServletResponse response, ResponseExcel responseExcel) {
        ExcelWriterBuilder writerBuilder = EasyExcel.write(response.getOutputStream())
                .registerConverter(LocalDateStringConverter.INSTANCE)
                .registerConverter(LocalDateTimeStringConverter.INSTANCE).autoCloseStream(true)
                .excelType(responseExcel.suffix()).inMemory(responseExcel.inMemory());

        if (StringUtils.isNotBlank(responseExcel.password())) {
            writerBuilder.password(responseExcel.password());
        }

        if (responseExcel.include().length != 0) {
            writerBuilder.includeColumnFieldNames(Arrays.asList(responseExcel.include()));
        }

        if (StringUtils.isNotBlank(responseExcel.exclude())) {
            String exclude = WebUtils.getAttribute("exclude", String.class);
            writerBuilder.excludeColumnFieldNames(StringUtils.split(exclude));
        }

        // TODO 扩展后续添加
        //if (responseExcel.writeHandler().length != 0) {
        //    for (Class<? extends WriteHandler> clazz : responseExcel.writeHandler()) {
        //        writerBuilder.registerWriteHandler(BeanUtils.instantiateClass(clazz));
        //    }
        //}

        // 自定义注入的转换器
        registerCustomConverter(writerBuilder);

        //if (responseExcel.converter().length != 0) {
        //    for (Class<? extends Converter> clazz : responseExcel.converter()) {
        //        writerBuilder.registerConverter(BeanUtils.instantiateClass(clazz));
        //    }
        //}

        //String templatePath = configProperties.getTemplatePath();
        //if (.hasText(responseExcel.template())) {
        //    ClassPathResource classPathResource = new ClassPathResource(
        //            templatePath + File.separator + responseExcel.template());
        //    InputStream inputStream = classPathResource.getInputStream();
        //    writerBuilder.withTemplate(inputStream);
        //}

        //writerBuilder = excelWriterBuilderEnhance.enhanceExcel(writerBuilder, response, responseExcel, templatePath);

        return writerBuilder.build();
    }

    /**
     * 自定义注入转换器 如果有需要，子类自己重写
     *
     * @param builder ExcelWriterBuilder
     */
    public void registerCustomConverter(ExcelWriterBuilder builder) {
        converterProvider.ifAvailable(converters -> converters.forEach(builder::registerConverter));
    }

    /**
     * 构建一个 空的 WriteSheet 对象
     *
     * @param sheetProperties sheet build 属性
     * @param template        模板信息
     * @return WriteSheet
     */
    public WriteSheet emptySheet(Sheet sheetProperties, String template) {
        // Sheet 编号和名称
        Integer sheetNo = sheetProperties.sheetNo() >= 0 ? sheetProperties.sheetNo() : DEFAULT_SHEET_NO;

        // 是否模板写入
        ExcelWriterSheetBuilder writerSheetBuilder = StringUtils.isNotBlank(template) ? EasyExcel.writerSheet(sheetNo)
                : EasyExcel.writerSheet(sheetNo, sheetProperties.sheetName());

        return writerSheetBuilder.build();
    }

    /**
     * 获取 WriteSheet 对象
     *
     * @param sheetProperties       sheet annotation info
     * @param dataClass             数据类型
     * @param template              模板
     * @param bookHeadEnhancerClass 自定义头处理器
     * @return WriteSheet
     */
    public WriteSheet emptySheet(Sheet sheetProperties, Class<?> dataClass, String template,
                                 Class<? extends HeadGenerator> bookHeadEnhancerClass) {

        // Sheet 编号和名称
        Integer sheetNo = sheetProperties.sheetNo() >= 0 ? sheetProperties.sheetNo() : DEFAULT_SHEET_NO;
        String sheetName = sheetProperties.sheetName();

        // 是否模板写入
        ExcelWriterSheetBuilder writerSheetBuilder = StringUtils.isNotBlank(template) ? EasyExcel.writerSheet(sheetNo)
                : EasyExcel.writerSheet(sheetNo, sheetName);

        // 头信息增强 1. 优先使用 sheet 指定的头信息增强 2. 其次使用 @ResponseExcel 中定义的全局头信息增强
        Class<? extends HeadGenerator> headGenerateClass = null;
        //if (isNotInterface(sheetProperties.getHeadGenerateClass())) {
        //    headGenerateClass = sheetBuildProperties.getHeadGenerateClass();
        //} else if (isNotInterface(bookHeadEnhancerClass)) {
        //    headGenerateClass = bookHeadEnhancerClass;
        //}
        // 定义头信息增强则使用其生成头信息，否则使用 dataClass 来自动获取
        if (headGenerateClass != null) {
            fillCustomHeadInfo(dataClass, bookHeadEnhancerClass, writerSheetBuilder);
        } else if (dataClass != null) {
            writerSheetBuilder.head(dataClass);
            if (sheetProperties.excludes().length > 0) {
                writerSheetBuilder.excludeColumnFieldNames(Arrays.asList(sheetProperties.excludes()));
            }
            if (sheetProperties.includes().length > 0) {
                writerSheetBuilder.includeColumnFieldNames(Arrays.asList(sheetProperties.includes()));
            }
        }

        // sheetBuilder 增强
        //writerSheetBuilder = excelWriterBuilderEnhance.enhanceSheet(writerSheetBuilder, sheetNo, sheetName, dataClass,
        //template, headGenerateClass);

        return writerSheetBuilder.build();
    }

    private void fillCustomHeadInfo(Class<?> dataClass, Class<? extends HeadGenerator> headEnhancerClass,
                                    ExcelWriterSheetBuilder writerSheetBuilder) {
        HeadGenerator headGenerator = this.applicationContext.getBean(headEnhancerClass);
        Assert.notNull(headGenerator, "The header generated bean does not exist.");
        HeadMeta head = headGenerator.head(dataClass);
        writerSheetBuilder.head(head.getHead());
        writerSheetBuilder.excludeColumnFieldNames(head.getIgnoreHeadFields());
    }

    /**
     * 是否为Null Head Generator
     *
     * @param headGeneratorClass 头生成器类型
     * @return true 已指定 false 未指定(默认值)
     */
    private boolean isNotInterface(Class<? extends HeadGenerator> headGeneratorClass) {
        return !Modifier.isInterface(headGeneratorClass.getModifiers());
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

}
