package io.github.perfmarktop.forest

import com.dtflys.forest.annotation.*
import com.dtflys.forest.callback.OnLoadCookie
import com.dtflys.forest.callback.OnSaveCookie
import com.dtflys.forest.http.ForestCookies
import com.dtflys.forest.http.ForestRequest
import com.google.gson.JsonObject
import org.springframework.stereotype.Component

/**
 * @author Madray Haven
 * @Date 2023/6/20 下午5:04
 */
@ForestClient
@Component
@Address(
        scheme = "https",
        host = "www.videocardbenchmark.net"
)
interface GpuPerfMarkClient {
    @Get("/GPU_mega_page.html")
    fun megaPage(
            cookie: OnSaveCookie = GpuMegaPageCookie,
    ): ForestRequest<String>
    @Get("/data/")
    fun data(
            @Query("_") ts: Long = System.currentTimeMillis(),
            @Header("Referer") referer: String = "https://www.videocardbenchmark.net/GPU_mega_page.html",
            @Header("X-Requested-With") reqWith: String = "XMLHttpRequest",
            cookie: OnLoadCookie = GpuMegaPageCookie,
    ): ForestRequest<JsonObject>
}

object GpuMegaPageCookie: OnSaveCookie, OnLoadCookie {
    private var cookies: ForestCookies? = null

    override fun onSaveCookie(req: ForestRequest<*>, cookies: ForestCookies) {
        this.cookies = cookies
    }

    override fun onLoadCookie(req: ForestRequest<*>, cookies: ForestCookies) {
        cookies.addAllCookies(this.cookies?.allCookies() ?: return)
    }
}
