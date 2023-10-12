package com.naown.excel.hanbler;

import com.alibaba.excel.converters.Converter;
import com.naown.excel.annotation.ResponseExcel;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.core.annotation.Order;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author lengleng
 * @author L.cm
 * @author Hccake
 * @date 2020/3/29
 */
@Order(1)
public class ManySheetWriteHandler extends AbstractSheetWriteHandler {

    public ManySheetWriteHandler(ObjectProvider<List<Converter<?>>> converterProvider) {
        super(converterProvider);
    }

    /**
     * 当且仅当List不为空且List中的元素也是List 才返回true
     *
     * @param obj 返回对象
     * @return boolean
     */
    @Override
    public boolean supportsReturnType(Object obj) {
        if (obj instanceof List) {
            List<?> objList = (List<?>) obj;
            return !objList.isEmpty() && objList.get(0) instanceof List;
        } else {
            throw new IllegalArgumentException("@ResponseExcel 返回值必须为List类型");
        }
    }

    @Override
    public void write(Object obj, HttpServletResponse response, ResponseExcel responseExcel) {
        // TODO 暂无实现
    }

}
