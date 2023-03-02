package com.dfds.demolyy.ValidatorTest.config;

import org.springframework.context.annotation.Bean;
import org.springframework.validation.Validator;

public class ValidatedPropertiesValidatorBean {
    /**
     * 注册 Validator Bean 的方法必须为 static 方法，且必须名为 configurationPropertiesValidator
     *
     * @return Validator
     */
    @Bean
    public static Validator configurationPropertiesValidator() {
        return new ValidatedPropertiesValidator();
    }
}
