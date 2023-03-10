package com.ccie.dlgs.point.utils;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ccie.dlgs.point.vo.PageVo;
import com.google.common.base.CaseFormat;
import com.google.common.base.Converter;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Exrickx
 */
public class PageUtil {

    /**
     * JPA分页封装
     * @param page
     * @return
     */
    public static Pageable initPage(PageVo page){

        Pageable pageable = null;
        int pageNumber = page.getPageNum();
        int pageSize = page.getPageSize();
        String sort = page.getOrderByColumn();
        String order = page.getIsAsc();

        if(pageNumber<1){
            pageNumber = 1;
        }
        if(pageSize<1){
            pageSize = 10;
        }
        if(pageSize>100){
            pageSize = 100;
        }
        if(StrUtil.isNotBlank(sort)) {
            Sort.Direction d;
            if(StrUtil.isBlank(order)) {
                d = Sort.Direction.DESC;
            } else {
                d = Sort.Direction.valueOf(order.toUpperCase());
            }
            Sort s = Sort.by(d, sort);
            pageable = PageRequest.of(pageNumber-1, pageSize, s);
        } else {
            pageable = PageRequest.of(pageNumber-1, pageSize);
        }
        return pageable;
    }

    /**
     * Mybatis-Plus分页封装
     * @param page
     * @return
     */
    public static Page initMpPage(PageVo page){

        Page p = null;
        int pageNumber = page.getPageNum();
        int pageSize = page.getPageSize();
        String sort = page.getOrderByColumn();
        String order = page.getIsAsc();

        if(pageNumber<1){
            pageNumber = 1;
        }
        if(pageSize<1){
            pageSize = 10;
        }
        if(pageSize>100){
            pageSize = 100;
        }
        if(StrUtil.isNotBlank(sort)) {
            Converter<String, String> converter = CaseFormat.LOWER_CAMEL.converterTo(CaseFormat.LOWER_UNDERSCORE);
            sort = converter.convert(sort);
            Boolean isAsc = false;
            if(StrUtil.isBlank(order)) {
                isAsc = false;
            } else {
                if("desc".equals(order.toLowerCase())){
                    isAsc = false;
                } else if("asc".equals(order.toLowerCase())){
                    isAsc = true;
                }
            }
            p = new Page(pageNumber, pageSize);
            if(isAsc){
                p.addOrder(OrderItem.asc(sort));
            } else {
                p.addOrder(OrderItem.desc(sort));
            }

        } else {
            p = new Page(pageNumber, pageSize);
        }
        return p;
    }

    /**
     * List 手动分页
     * @param page
     * @param list
     * @return
     */
    public static List listToPage(PageVo page, List list) {

        int pageNumber = page.getPageNum() - 1;
        int pageSize = page.getPageSize();

        if(pageNumber<0){
            pageNumber = 0;
        }
        if(pageSize<1){
            pageSize = 10;
        }
        if(pageSize>100){
            pageSize = 100;
        }

        int fromIndex = pageNumber * pageSize;
        int toIndex = pageNumber * pageSize + pageSize;

        if(fromIndex > list.size()){
            return new ArrayList();
        } else if(toIndex >= list.size()) {
            return list.subList(fromIndex, list.size());
        } else {
            return list.subList(fromIndex, toIndex);
        }
    }
    /**
     * mybatis page to jpa page
     * @param <D>
     * @param page
     * @return
     */
    public static <D> org.springframework.data.domain.Page<D> getJPAPage(IPage<D> page) {
        Pageable pageable = PageRequest.of((int)page.getCurrent()-1, (int)page.getSize());
        return new PageImpl<D>(page.getRecords(), pageable, page.getTotal());
    }

}
