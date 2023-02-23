package com.dfds.demolyy.utils.otherUtils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Slf4j
public class BeanHelper {

    public static <T> T copyProperties(Object source, Class<T> target) {
        try {
            T t = target.newInstance();
            BeanUtils.copyProperties(source, t);
            return t;
        } catch (Exception e) {
            log.error("【数据转换】数据转换出错，目标对象{}构造函数异常", target.getName(), e);
            throw new RuntimeException();
        }
    }

    public static <T> List<T> copyWithCollection(List<?> sourceList, Class<T> target) {
        try {
            return sourceList.stream().map(s -> copyProperties(s, target)).collect(Collectors.toList());
        } catch (Exception e) {
            log.error("【数据转换】数据转换出错，目标对象{}构造函数异常", target.getName(), e);
            throw new RuntimeException();
        }
    }

    public static <T> Set<T> copyWithCollection(Set<?> sourceList, Class<T> target) {
        try {
            return sourceList.stream().map(s -> copyProperties(s, target)).collect(Collectors.toSet());
        } catch (Exception e) {
            log.error("【数据转换】数据转换出错，目标对象{}构造函数异常", target.getName(), e);
            throw new RuntimeException();
        }
    }

    public static <T1, T2> List<T2> listCopy(List<T1> listT1, Class<T2> t2) {
        List<T2> listT2 = new ArrayList<T2>();
        try {
            for(T1 t1 : listT1){
                T2 tx = t2.newInstance();
                BeanUtils.copyProperties(tx, t1);
                listT2.add(tx);
            }
        } catch (Exception e){
            log.info(e.toString());
        }
        return listT2;
    }
}
