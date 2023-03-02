package com.dfds.demolyy.ValidatorTest.config;

import com.dfds.demolyy.ValidatorTest.annotation.IsMobile;
import com.dfds.demolyy.ValidatorTest.annotation.RegexValid;
import com.dfds.demolyy.utils.otherUtils.CheckRegexUtil;
import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.Range;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 从 <code>prop/random.properties</code> 文件中加载随机属性，使用默认校验器对使用注解标记校验规则的属性进行校验
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2019-11-20
 */
@Data
@ToString
//@Validated
//@Component
//@ConfigurationProperties(prefix = "io.github.dunwu")
public class DunwuProperties {

    private String profile;

    @Range(min = 0, max = 100)
    private int id;

    @Value("io.github.dunwu.author")
    private String author;

    private GenderEnum sex;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date date;

    @IsMobile(message = "手机号不正确")
    private String mobile;

    @RegexValid(regexp = CheckRegexUtil.URL)
    private String host;

    @Email
    private String mail;

    @Size(min = 3, max = 4)
    private String[] address;

    @NotEmpty
    private List<String> interestList;

    private Map<String, String> infoMap;

    private Map<String, List<String>> skillMap;

}
