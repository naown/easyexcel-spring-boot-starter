package com.naown.excel.converters;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * LocalDate and string converter
 *
 * @author L.cm
 */
public enum LocalDateStringConverter implements Converter<LocalDate> {

    /**
     * 实例
     */
    INSTANCE;

    @Override
    public Class<?> supportJavaTypeKey() {
        return LocalDate.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.STRING;
    }

    @Override
    public LocalDate convertToJavaData(ReadCellData cellData, ExcelContentProperty contentProperty,
                                       GlobalConfiguration globalConfiguration) throws ParseException {
        if (contentProperty == null || contentProperty.getDateTimeFormatProperty() == null) {
            return LocalDate.parse(cellData.getStringValue());
        }
        return LocalDate.parse(cellData.getStringValue(), DateTimeFormatter
                .ofPattern(contentProperty.getDateTimeFormatProperty().getFormat()));
    }

    @Override
    public WriteCellData<String> convertToExcelData(LocalDate value, ExcelContentProperty contentProperty,
                                                    GlobalConfiguration globalConfiguration) {
        if (contentProperty == null || contentProperty.getDateTimeFormatProperty() == null) {
            return new WriteCellData<>(value.format(DateTimeFormatter.ISO_LOCAL_DATE));
        }
        return new WriteCellData<>(value.format(DateTimeFormatter
                .ofPattern(contentProperty.getDateTimeFormatProperty().getFormat())));
    }

}
