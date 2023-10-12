package com.naown.excel.hanbler;

import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.naown.excel.annotation.ResponseExcel;
import com.naown.excel.annotation.Sheet;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.core.annotation.Order;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 默认使用单Sheet导出
 *
 * @author lengleng
 * @author L.cm
 * @author Hccake
 * @date 2020/3/29
 * <p>
 * 处理单sheet 页面
 */
@Order(-1)
public class SingleSheetWriteHandler extends AbstractSheetWriteHandler {
    public SingleSheetWriteHandler(ObjectProvider<List<Converter<?>>> converterProvider) {
        super(converterProvider);
    }

    /**
     * obj 是List 且list不为空同时list中的元素不是是List 才返回true
     *
     * @param obj 返回对象
     * @return boolean
     */
    @Override
    public boolean supportsReturnType(Object obj) {
        if (obj instanceof List) {
            List<?> objList = (List<?>) obj;
            return !objList.isEmpty() && !(objList.get(0) instanceof List);
        } else {
            throw new IllegalArgumentException("@ResponseExcel 返回值必须为List类型");
        }
    }

    @Override
    public void write(Object obj, HttpServletResponse response, ResponseExcel responseExcel) {
        List<?> eleList = (List<?>) obj;
        ExcelWriter excelWriter = super.getExcelWriter(response, responseExcel);

        // 获取 Sheet 配置
        Sheet sheetProperties = responseExcel.sheets();

        // 模板信息
        String template = responseExcel.template();

        // 创建sheet
        WriteSheet sheet;
        if (eleList.isEmpty()) {
            sheet = this.emptySheet(sheetProperties, template);
        } else {
            Class<?> dataClass = eleList.get(0).getClass();
            sheet = this.emptySheet(sheetProperties, dataClass, template, responseExcel.headGenerator());
        }

        if (responseExcel.fill()) {
            // 填充 sheet
            excelWriter.fill(eleList, sheet);
        } else {
            // 写入 sheet
            excelWriter.write(eleList, sheet);
        }

        excelWriter.finish();
    }

}
