package com.naown.excel.util;

import lombok.experimental.UtilityClass;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.Method;
import java.util.Optional;

/**
 * spring el表达式解析
 *
 * @author chenjian
 * @date 2021/4/26 10:58
 */
@UtilityClass
public class SpElUtils {
    private final ExpressionParser PARSER = new SpelExpressionParser();
    private final DefaultParameterNameDiscoverer PARAMETER_NAME_DISCOVERER = new DefaultParameterNameDiscoverer();

    public String parseSpEl(Method method, Object[] args, String spEl) {
        //解析参数名
        String[] params = Optional.ofNullable(PARAMETER_NAME_DISCOVERER.getParameterNames(method)).orElse(new String[0]);
        //el解析需要的上下文对象
        EvaluationContext context = new StandardEvaluationContext();
        for (int i = 0; i < params.length; i++) {
            //所有参数都作为原材料扔进去
            context.setVariable(params[i], args[i]);
        }
        return PARSER.parseExpression(spEl).getValue(context, String.class);
    }
}
