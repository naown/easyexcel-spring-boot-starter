package com.naown.excel.util;

import lombok.experimental.UtilityClass;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.Optional;

/**
 * @date: 2023/8/8 15:51
 * @author: chenjian
 */
@UtilityClass
public class WebUtils {

    public <T> T getAttribute(String attributeName, Class<T> type) {
        if (getRequestAttributes().isPresent()) {
            Object value = getRequestAttributes().get().getAttribute(attributeName, RequestAttributes.SCOPE_REQUEST);
            if (type.isInstance(value)) {
                return type.cast(value);
            }
        }
        throw new ClassCastException("Cannot cast an Object to " + type);
    }

    public void setAttribute(String attributeName, Object attributeValue) {
        if (getRequestAttributes().isPresent()) {
            getRequestAttributes().get().setAttribute(attributeName, attributeValue, RequestAttributes.SCOPE_REQUEST);
        }
    }

    /**
     * 获取 RequestAttributes
     *
     * @return RequestAttributes
     */
    public Optional<RequestAttributes> getRequestAttributes() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            return Optional.empty();
        }
        return Optional.of(requestAttributes);
    }

}
