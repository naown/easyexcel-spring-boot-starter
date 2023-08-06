package com.naown.excel.converters;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.alibaba.excel.util.DateUtils;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * LocalDateTime and string converter
 *
 * @author L.cm
 */
public enum LocalDateTimeStringConverter implements Converter<LocalDateTime> {

    /**
     * 实例
     */
    INSTANCE;

    private static final String MINUS = "-";

    @Override
    public Class<?> supportJavaTypeKey() {
        return LocalDateTime.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.STRING;
    }

    @Override
    public LocalDateTime convertToJavaData(ReadCellData cellData, ExcelContentProperty contentProperty,
                                           GlobalConfiguration globalConfiguration) throws ParseException {
        if (contentProperty == null || contentProperty.getDateTimeFormatProperty() == null) {
            return LocalDateTime.parse(cellData.getStringValue(), DateTimeFormatter
                    .ofPattern(switchDateFormat(cellData.getStringValue())));
        }
        return LocalDateTime.parse(cellData.getStringValue(), DateTimeFormatter
                .ofPattern(contentProperty.getDateTimeFormatProperty().getFormat()));
    }

    @Override
    public WriteCellData<String> convertToExcelData(LocalDateTime value, ExcelContentProperty contentProperty,
                                                    GlobalConfiguration globalConfiguration) {
        if (contentProperty == null || contentProperty.getDateTimeFormatProperty() == null) {
            return new WriteCellData<>(value.format(DateTimeFormatter.ofPattern(DateUtils.DATE_FORMAT_19)));
        }
        return new WriteCellData<>(value.format(DateTimeFormatter
                .ofPattern(contentProperty.getDateTimeFormatProperty().getFormat())));
    }

    /**
     * switch date format
     *
     * @param dateString dateString
     * @return pattern
     */
    private static String switchDateFormat(String dateString) {
        int length = dateString.length();
        switch (length) {
            case 19:
                if (dateString.contains(MINUS)) {
                    return DateUtils.DATE_FORMAT_19;
                } else {
                    return DateUtils.DATE_FORMAT_19_FORWARD_SLASH;
                }
            case 17:
                return DateUtils.DATE_FORMAT_17;
            case 14:
                return DateUtils.DATE_FORMAT_14;
            case 10:
                return DateUtils.DATE_FORMAT_10;
            default:
                throw new IllegalArgumentException("can not find date format for：" + dateString);
        }
    }

}
