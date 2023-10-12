package com.naown.excel.util;

import lombok.experimental.UtilityClass;
import org.springframework.lang.NonNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @date: 2023/8/8 15:32
 * @author: chenjian
 */
@UtilityClass
public class StringUtils {

    public static int length(CharSequence cs) {
        return cs == null ? 0 : cs.length();
    }

    public boolean isBlank(CharSequence cs) {
        int strLen = length(cs);
        if (strLen != 0) {
            for (int i = 0; i < strLen; ++i) {
                if (!Character.isWhitespace(cs.charAt(i))) {
                    return false;
                }
            }

        }
        return true;
    }

    public boolean isNotBlank(CharSequence cs) {
        return !isBlank(cs);
    }

    public List<String> split(String str, @NonNull String delimiter) {
        if (isBlank(str)) {
            return Collections.emptyList();
        }
        return Arrays.asList(str.split(delimiter));
    }

    public List<String> split(String str) {
        return split(str, ",");
    }
}
