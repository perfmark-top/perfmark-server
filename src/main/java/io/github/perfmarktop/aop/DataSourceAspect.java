package io.github.perfmarktop.aop;


import io.github.perfmarktop.common.DataSourceContextHolder;
import io.github.perfmarktop.common.DataSourceType;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class DataSourceAspect {
    
    @Before("@annotation(io.github.perfmarktop.annotation.ReadOnly)")
    public void setReadDataSource() {
        DataSourceContextHolder.setDataSourceType(DataSourceType.SLAVE);
    }
    
    @Before("@annotation(io.github.perfmarktop.annotation.ReadWrite)")
    public void setWriteDataSource() {
        DataSourceContextHolder.setDataSourceType(DataSourceType.MASTER);
    }
    
    @After("@annotation(io.github.perfmarktop.annotation.ReadOnly) || @annotation(io.github.perfmarktop.annotation.ReadWrite)")
    public void clearDataSource() {
        DataSourceContextHolder.clearDataSourceType();
    }
}
