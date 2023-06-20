package io.github.perfmarktop.forest

import com.dtflys.forest.annotation.Get
import com.dtflys.forest.annotation.Query
import com.dtflys.forest.callback.OnLoadCookie
import com.dtflys.forest.callback.OnSaveCookie
import com.dtflys.forest.http.ForestCookies
import com.dtflys.forest.http.ForestRequest
import com.google.gson.JsonObject

/**
 * @author Madray Haven
 * @Date 2023/6/20 下午5:04
 */
interface GpuPerfMarkClient {
    @Get("/GPU_mega_page.html")
    fun cpuMegaPage(
            cookie: OnSaveCookie = GpuMegaPageCookie,
    ): ForestRequest<String>
    @Get("/data/")
    fun data(
            @Query("_") ts: Long = System.currentTimeMillis(),
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
