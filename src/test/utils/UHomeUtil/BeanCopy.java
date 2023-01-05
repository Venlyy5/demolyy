package com.uhome.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author FYX day-activity tools 2F
 */
@Slf4j
public class BeanCopy {

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
