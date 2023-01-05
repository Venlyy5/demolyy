package com.xuecheng.mybatis.config;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.injector.ISqlInjector;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import com.xuecheng.commons.utils.AuthInfoHolder;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.StringValue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class MyBatisPlusConfig {

   // 指定不需要设置租户的数据库表名
   private static String [] tables = new String[]{"tb_dictionary","tb_category", "undo_log",
           "tb_application","tb_company","tb_resource","tb_role_resource","tb_user_role"};

   /**
    * 注册Mybatis-plus的插件
    */
   @Bean
   public MybatisPlusInterceptor mybatisPlusInterceptor() {
      MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();

      // 多租户插件, 必须先注册多租户插件
      interceptor.addInnerInterceptor(tenantEnterpriseInterceptor());
      // 分页的插件
      interceptor.addInnerInterceptor(paginationInnerInterceptor());

      return interceptor;
   }

   /**
    * 分页的插件配置
    */
   @Bean
   public PaginationInnerInterceptor paginationInnerInterceptor() {
      return new PaginationInnerInterceptor(DbType.MYSQL);
   }


   /**
    * 多租户的插件配置
    *    getTenantId: 获取租户id(从ThreadLocal中获取companyId)
    *    getTenantIdColumn: 拼接的SQL语句的租户字段(拼接SQL:company_id)
    *    ignoreTable: 判断表是否需要租户配置(排除不需要租户配置的表)
    */
   @Bean
   public TenantLineInnerInterceptor tenantEnterpriseInterceptor(){
      return new TenantLineInnerInterceptor(new TenantLineHandler() {
         // 获取当前线程中的租户id(companyId)
         public Expression getTenantId() {
            Long companyId = AuthInfoHolder.getCompanyId();
            if(ObjectUtil.isEmpty(companyId)) {
               return null;
            }else {
               return new StringValue(companyId.toString());
            }
         }

         // 租户的字段,是数据库的列名,不是实体类
         public String getTenantIdColumn() {
            return "company_id";
         }

         // 设置忽略的表,true表示要忽略，false表示需要拼接
         @Override
         public boolean ignoreTable(String tableName) {
            // 是否需要需要过滤某一张表
            if (ArrayUtil.contains(tables, tableName)){
               return true;
            }

            // 拼接的sql多租户字段对应的值不能为空
            Expression tenantId = this.getTenantId();
            // 如果租户id为空,则不拼接
            if (ObjectUtil.isEmpty((tenantId))){
               return true;
            }

            // 过滤表后,如果租户id不为空,则需要拼接
            return false;
         }

      });

   }

}