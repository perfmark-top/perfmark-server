package io.github.perfmarktop.core

import com.dtflys.forest.http.ForestRequest
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.type.Month
import io.github.perfmarktop.forest.CpuPerfMarkClient
import io.github.perfmarktop.forest.GpuPerfMarkClient
import io.github.perfmarktop.mariadb.*
import io.github.sgpublic.kotlin.core.util.fromGson
import io.github.sgpublic.kotlin.util.Loggable
import jakarta.annotation.PostConstruct
import jakarta.transaction.Transactional
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.springframework.data.repository.findByIdOrNull
import org.springframework.scheduling.TaskScheduler
import org.springframework.scheduling.support.CronTrigger
import org.springframework.stereotype.Component
import java.util.LinkedList

/**
 * @author Madray Haven
 * @Date 2023/6/20 下午4:58
 */
@Component
class PerfUpdater(
    private val scheduler: TaskScheduler,

    private val cpuClient: CpuPerfMarkClient,
    private val gpuClient: GpuPerfMarkClient,

    private val update: UpdateLogRepository,
    private val cpuRepo: CpuPerfMarkDao,
    private val gpuRepo: GpuPerfMarkDao,
): Loggable {
    @OptIn(DelicateCoroutinesApi::class)
    @PostConstruct
    fun init() {
        GlobalScope.launch {
            Thread.sleep(5_000)
            if (update.findByIdOrNull(UpdateLogEntity.Type.Cpu) == null) {
                tryUpdateCpu()
            }
            Thread.sleep(10_000)
            if (update.findByIdOrNull(UpdateLogEntity.Type.Gpu) == null) {
                tryUpdateGpu()
            }
            Thread.sleep(36_000_000)
            scheduler.schedule(::task, CronTrigger(Config.UpdateTick))
        }
    }

    private fun task() {
        tryUpdateCpu()
        Thread.sleep(10_000)
        tryUpdateGpu()
    }

    private fun tryUpdateCpu() {
        try {
            updateCpu()
        } catch (e: Exception) {
            log.warn("CPU 跑分榜更新失败", e)
        }
    }

    @Transactional
    fun updateCpu() {
        log.info("开始更新 CPU 跑分榜")
        val remoteTime = findUpdateTime(cpuClient.megaPage().executeAsString())
                ?: throw IllegalStateException("无法获取 CPU 远端更新时间")
        Thread.sleep(5_000)
        val data = cpuClient.data()
                .executeAsJsonObject()
                .getAsJsonArray("data")
                ?: throw IllegalArgumentException("远端数据为空")
        cpuRepo.saveAll(data.mapAsJsonObject { item ->
            CpuPerfMarkEntity(
                    id = item.get("id").asInt,
                    name = item.get("name").asString,
                    price = item.get("price").asPriceDoubleOrNA,
                    cpumark = item.get("cpumark").asIntOrNA!!,
                    thread = item.get("thread").asIntOrNA,
                    value = item.get("value").asDoubleOrNA,
                    threadValue = item.get("threadValue").asDoubleOrNA,
                    tdp = item.get("tdp").asIntOrNA,
                    powerPerf = item.get("powerPerf").asDoubleOrNA,
                    date = item.get("date").asPubDate,
                    socket = item.get("socket").asString,
                    cat = item.get("cat").asString.asCpuCatList,
                    speed = item.get("speed").asIntOrNA,
                    turbo = item.get("turbo").asIntOrNA,
                    cpuCount = item.get("cpuCount").asInt,
                    cores = item.get("cores").asInt,
                    logicals = item.get("logicals").asInt,
                    secondaryCores = item.get("secondaryCores").asInt,
                    secondaryLogicals = item.get("secondaryLogicals").asInt,
                    rank = item.get("rank").asIntOrNA,
                    samples = item.get("samples").asIntOrNA!!,
                    href = item.get("href").asString,
                    output = item.get("output").asBoolean,
            )
        })
        val time = System.currentTimeMillis()
        update.save(UpdateLogEntity(
                UpdateLogEntity.Type.Cpu,
                remoteTime, time
        ))
        log.info("更新 CPU 跑分榜成功，共 ${data.size()} 条记录，更新时间：$remoteTime")
    }

    private fun tryUpdateGpu() {
        try {
            updateGpu()
        } catch (e: Exception) {
            log.warn("GPU 跑分榜更新失败", e)
        }
    }

    @Transactional
    fun updateGpu() {
        log.info("开始更新 GPU 跑分榜")
        val remoteTime = findUpdateTime(gpuClient.megaPage().executeAsString())
                ?: throw IllegalStateException("无法获取 GPU 远端更新时间")
        Thread.sleep(5_000)
        val data = gpuClient.data()
                .executeAsJsonObject()
                .getAsJsonArray("data")
                ?.takeIf { !it.isEmpty }
                ?: throw IllegalArgumentException("远端数据为空")
        gpuRepo.saveAll(data.mapAsJsonObject { item ->
            GpuPerfMarkEntity(
                    id = item.get("id").asInt,
                    name = item.get("name").asString,
                    price = item.get("price").asPriceDoubleOrNA,
                    g2d = item.get("g2d").asIntOrNA,
                    g3d = item.get("g3d").asIntOrNA,
                    value = item.get("value").asDoubleOrNA,
                    tdp = item.get("tdp").asIntOrNA,
                    powerPerf = item.get("powerPerf").asDoubleOrNA,
                    date = item.get("date").asPubDate,
                    cat = item.get("cat").asString.asGpuCatList,
                    bus = item.get("bus").asString?.takeIf { it.lowercase() != "na" },
                    memSize = item.get("memSize").asSizeIntOrNA,
                    coreClk = item.get("coreClk").asClockIntOrNA,
                    memClk = item.get("memClk").asClockIntOrNA,
                    rank = item.get("rank").asIntOrNA,
                    samples = item.get("samples").asIntOrNA!!,
                    href = item.get("href").asString,
                    output = item.get("output").asBoolean,
            )
        })
        val time = System.currentTimeMillis()
        update.save(UpdateLogEntity(
                UpdateLogEntity.Type.Gpu,
                remoteTime, time
        ))
        log.info("更新 GPU 跑分榜成功，共 ${data.size()} 条记录，更新时间：$remoteTime")
    }

    private val datePattern = "\\d{1,2}(?:st|nd|rd|th) of \\w+ \\d{4}".toPattern()
    private fun findUpdateTime(html: String) =
            datePattern.matcher(html)
                    .takeIf { it.find() }
                    ?.group()

    val JsonElement.asPriceDoubleOrNA: Double? get() {
        return takeIf { !it.isJsonNull }
                ?.asString
                ?.takeIf { it.lowercase() != "na" }
                ?.replace(",", "")
                ?.replace("*", "")
                ?.replace("$", "")
                ?.toDouble()
    }

    val JsonElement.asIntOrNA: Int? get() {
        return takeIf { !it.isJsonNull }
                ?.asString
                ?.takeIf { it.lowercase() != "na" }
                ?.replace(",", "")
                ?.toIntOrNull()
    }

    val JsonElement.asSizeIntOrNA: Int? get() {
        return takeIf { !it.isJsonNull }
                ?.asString
                ?.takeIf { it.lowercase() != "na" }
                ?.replace(",", "")
                ?.split(" ")?.get(0)
                ?.toIntOrNull()
    }

    val JsonElement.asClockIntOrNA: Int? get() {
        return takeIf { !it.isJsonNull }
                ?.asString
                ?.takeIf { it.lowercase() != "na" }
                ?.replace(",", "")
                ?.split(" ")?.get(0)
                ?.toIntOrNull()
    }

    val JsonElement.asDoubleOrNA: Double? get() {
        return takeIf { !it.isJsonNull }
                ?.asString
                ?.takeIf { it.lowercase() != "na" }
                ?.replace(",", "")
                ?.toDoubleOrNull()
    }

    val JsonElement.asPubDate: PubDate? get() {
        return asString.takeIf {
            it.lowercase() != "na"
        }?.split(" ")?.let {
            val tmp = it[0].uppercase()
            PubDate(
                    month = Month.values().find { month ->
                        month.number > 0 && month.name.startsWith(tmp)
                    }!!,
                    year = it[1].toInt()
            )
        }
    }

    fun ForestRequest<JsonObject>.executeAsJsonObject(): JsonObject {
        return JsonObject::class.fromGson(executeAsString())
    }

    fun <T: Any> JsonArray.mapAsJsonObject(block: (JsonObject) -> T): List<T> {
        val result = LinkedList<T>()
        for (index in 0 until size()) {
            result.add(block.invoke(get(index).asJsonObject))
        }
        return result
    }
}