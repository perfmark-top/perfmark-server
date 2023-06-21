package io.github.perfmarktop.core

import io.github.sgpublic.kotlin.core.util.toGson
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import java.io.Serializable
import java.nio.charset.StandardCharsets


/**
 * @author Madray Haven
 * @Date 2023/6/21 下午1:38
 */
open class RespResult<T: Any> internal constructor(
        val code: Int = 200,
        val message: String = "success.",
        val data: T? = null
): Serializable

object SampleResult: RespResult<Unit>()

fun <T: Any> T.resp(): RespResult<T> {
    return RespResult(data = this)
}

class FailedResult private constructor(
        val code: Int,
        override val message: String,
        private val params: Map<String, Any>? = null
): RuntimeException(message) {
    companion object {
        val UnsupportedRequest get() = FailedResult(-4001, "不支持的请求方式")
        val MissingBody get() = FailedResult(-4002, "参数缺失")
        fun MissingBody(name: String, type: String) = FailedResult(
                -4002, "参数缺失",
                mapOf("name" to name, "type" to type)
        )
        val AnonymousDenied get() = FailedResult(-4050, "请登陆后再试")
        val NotFound get() = FailedResult(-4040, "您请求的资源不存在")

        val ServiceUnavailable get() = FailedResult(-5001, "服务不可用")
        val InternalServerError get() = FailedResult(-5002, "服务器内部错误")
        val ServerProcessingError get() = FailedResult(-5003, "请求处理出错")
        val NotImplementationError get() = FailedResult(-5004, "别买炒饭了，头发快掉光了(´╥ω╥`)")
    }

    fun resp(): FailedRespResult {
        return FailedRespResult(code, message, params)
    }
}

class FailedRespResult(code: Int, message: String, params: Map<String, Any>?)
    : RespResult<Map<String, Any>>(code, message, params)

fun HttpServletResponse.write(result: Serializable) {
    characterEncoding = StandardCharsets.UTF_8.name()
    addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
    writer.use {
        it.write(result.toGson())
        it.flush()
    }
}