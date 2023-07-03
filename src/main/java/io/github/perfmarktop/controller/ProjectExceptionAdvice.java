package io.github.perfmarktop.controller;



import com.baomidou.mybatisplus.extension.api.R;
import io.github.perfmarktop.exception.BusinessException;
import io.github.perfmarktop.exception.ResponseResult;
import io.github.perfmarktop.exception.SystemException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author YongJ
 * @date 2022/6/16 9:35
 */
@RestControllerAdvice
public class ProjectExceptionAdvice {
    //@ExceptionHandler用于设置当前处理器类对应的异常类型
    @ExceptionHandler(SystemException.class)
    public ResponseResult doSystemException(SystemException ex){
        return new ResponseResult(ex.getMessage());
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseResult doBusinessException(BusinessException ex){
        return new ResponseResult(ex.getMessage());
    }
    // 空指针异常
    @ExceptionHandler(NullPointerException.class)
    public ResponseResult doIOException(NullPointerException ex){
        return new ResponseResult("null异常请检查参数");
    }
    // 运行时异常
    @ExceptionHandler(RuntimeException.class)
    public ResponseResult doIOException(RuntimeException ex){
        return new ResponseResult(ex.getMessage());
    }
}
