package io.github.perfmarktop.core

import jakarta.annotation.PostConstruct
import org.springframework.scheduling.TaskScheduler
import org.springframework.scheduling.support.CronTrigger
import org.springframework.stereotype.Component
import feign.Contract
import feign.Feign
import feign.codec.Decoder
import feign.codec.Encoder
import io.github.perfmarktop.forest.GpuPerfMarkClient
import org.springframework.cloud.openfeign.FeignClientsConfiguration
import org.springframework.context.annotation.Import

/**
 * @author Madray Haven
 * @Date 2023/6/20 下午4:58
 */
@Import(FeignClientsConfiguration::class)
@Component
class PerfUpdater(
    private val scheduler: TaskScheduler,

    private val encoder: Encoder,
    private val decoder: Decoder,
    private val contract: Contract,
) {
    private val builder: Feign.Builder by lazy {
        Feign.builder()
                .encoder(encoder)
                .decoder(decoder)
                .contract(contract)
    }

    private val OriginGpu: GpuPerfMarkClient by lazy {
        builder.target(GpuPerfMarkClient::class.java, Config.OriginGpu)
    }
    private val OriginCpu: GpuPerfMarkClient by lazy {
        builder.target(GpuPerfMarkClient::class.java, Config.OriginCpu)
    }

    @PostConstruct
    fun init() {
        scheduler.schedule(::task, CronTrigger(Config.UpdateTick))
    }

    private fun task() {

    }
}