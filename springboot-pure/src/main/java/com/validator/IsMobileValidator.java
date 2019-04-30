package com.validator;


import com.annotation.IsMobile;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IsMobileValidator implements ConstraintValidator<IsMobile, String> {

    private boolean required = false;

    @Override
    public void initialize(IsMobile constraintAnnotation) {
        required = constraintAnnotation.required();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (required == false) {
            return isMobile(value);
        } else {
            if (StringUtils.isEmpty(value)) {
                return true;
            }
            return isMobile(value);
        }
    }


    private static final Pattern mobile_pattern = Pattern.compile("1\\d{10}");

    /**
     * 校验src是否为指定的手机号格式
     *
     * @param src 需要校验的手机号
     * @return 是指定的手机号返回true, 否则false
     */
    public static boolean isMobile(String src) {
        if (StringUtils.isEmpty(src)) {
            return false;
        }
        Matcher m = mobile_pattern.matcher(src);
        return m.matches();
    }

}

