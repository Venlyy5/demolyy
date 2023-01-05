package com.dfd.locale.utils;

import com.dfd.locale.constant.ConstantsI18n;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.stereotype.Component;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.ResourceBundle;
/**
 * @author ll
 */
@Slf4j
@Component
public class MessageUtil {

    public static String getMessage(String key){
        String message = "";
        try{
            Locale locale = getLocale();
            ResourceBundle bundle = ResourceBundle.getBundle(ConstantsI18n.I18N_PATH, locale);
            message = bundle.getString(key);
            log.info("Message1-locale=>{}---message=>{}",locale,message);
        }catch(Exception ex){
            log.error(ex.getMessage(), ex);
        }

        return message;
    }

    public static String getMessage(String key,String[] params){
        String message = "";
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename(ConstantsI18n.I18N_PATH);
        messageSource.setCacheSeconds(-1);
        messageSource.setDefaultEncoding(StandardCharsets.UTF_8.name());
        try{
            Locale locale = getLocale();
            message = messageSource.getMessage(key,params,locale);
            log.info("Message2-locale=>{}---message=>{}",locale,message);
        }catch(Exception ex){
            log.error(ex.getMessage(), ex);
        }

        return message;
    }

    public static Locale getLocale(){
        String localeStr = LocaleContextHolder.getLocale().toString();
        return new Locale(localeStr.toLowerCase());
    }
}