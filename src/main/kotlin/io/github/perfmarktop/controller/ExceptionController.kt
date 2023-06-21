package io.github.perfmarktop.controller

import io.github.perfmarktop.core.FailedRespResult
import io.github.perfmarktop.core.FailedResult
import io.github.sgpublic.kotlin.util.Loggable
import org.springframework.http.HttpStatus
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.servlet.NoHandlerFoundException

/**
 * @author Madray Haven
 * @Date 2023/6/21 下午1:35
 */
@ControllerAdvice
class ExceptionController: Loggable {
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(FailedResult::class)
    fun handleFailedResult(e: FailedResult): FailedRespResult {
        log.debug("响应错误信息", e)
        return e.resp()
    }

    @ResponseStatus(HttpStatus.NOT_IMPLEMENTED)
    @ExceptionHandler(NotImplementedError::class)
    fun handleNotImplementedError(e: NotImplementedError): FailedRespResult {
        return FailedResult.NotImplementationError.resp()
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoHandlerFoundException::class)
    fun handleNoHandlerFoundException(e: NoHandlerFoundException): FailedRespResult {
        return FailedResult.NotFound.resp()
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleHttpMessageNotReadableException(e: HttpMessageNotReadableException): FailedRespResult {
        return FailedResult.MissingBody.resp()
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingServletRequestParameterException::class)
    fun handleMissingServletRequestParameterException(e: MissingServletRequestParameterException): FailedRespResult {
        return FailedResult.MissingBody(e.parameterName, e.parameterType).resp()
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception::class)
    fun handleException(e: Exception): FailedRespResult {
        log.warn("未处理的错误", e)
        return FailedResult.InternalServerError.resp()
    }
}