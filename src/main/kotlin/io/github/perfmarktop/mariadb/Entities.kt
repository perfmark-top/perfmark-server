package io.github.perfmarktop.mariadb

import com.google.type.Month
import jakarta.persistence.*
import java.io.Serializable

/**
 * @author Madray Haven
 * @Date 2023/6/21 上午11:34
 */

data class PubDate(
        val year: Int,
        val month: Month,
): Serializable {
    override fun toString(): String {
        return "$year ${month.number}"
    }
}

val String.asPubDate: PubDate get() {
    split(" ").let {
        return PubDate(
                year = it[0].toInt(),
                month = Month.forNumber(it[1].toInt()),
        )
    }
}

@Entity(name = "cpu_mega")
@IdClass(CpuPerfMarkEntity.Id::class)
data class CpuPerfMarkEntity(
        /** CPU ID，似乎是数字字符串 */
        @jakarta.persistence.Id
        @Column(name = "id")
        val id: Int,

        /** 完整名称 */
        @Column(name = "name")
        val name: String,

        /** 价格，单位：美元，包含$前导 */
        @Column(name = "price")
        val price: Double?,

        /** 跑分 */
        @Column(name = "cpu_mark")
        val cpumark: Int,

        /** 单线程，单位：MOps/Sec */
        @Column(name = "thread")
        val thread: Int?,

        /** 性价比值，跑分/价格 */
        @Column(name = "value")
        val value: Double?,

        /** 单线程性价比值，单线程/价格 */
        @Column(name = "thread_value")
        val threadValue: Double?,

        /** TDP，单位：W */
        @Column(name = "tdp")
        val tdp: Int?,

        /** 疑似是峰值功耗 */
        @Column(name = "power_perf")
        val powerPerf: Double?,

        /** 首次出现时间 */
        @Column(name = "date")
        val date: PubDate?,

        /** 平台类型 */
        @Column(name = "cat")
        val cat: List<Cat>,

        /** 默频 */
        @Column(name = "speed")
        val speed: Int?,

        /** 接口类型 */
        @Column(name = "socket")
        val socket: String,

        /** 睿频 */
        @Column(name = "turbo")
        val turbo: Int?,

        /** 支持多路，单路的时候是数字1，多路的时候是字符串数字（好像是这样 */
        @jakarta.persistence.Id
        @Column(name = "cpu_count")
        val cpuCount: Int,

        /** 核心数，当 CPU 为大小核设计时，此字段表示大核核心数 */
        @Column(name = "cores")
        val cores: Int,

        /** 线程数，当 CPU 为大小核设计时，此字段表示大核线程数 */
        @Column(name = "logicals")
        val logicals: Int,

        /** 小核核心数，当 CPU 为大小核设计时，此字段大于 0（有些是数字有些是字符串数字mmp */
        @Column(name = "secondary_cores")
        val secondaryCores: Int,

        /** 小核线程数，当 CPU 为大小核设计时，此字段大于 0（有些是数字有些是字符串数字mmp */
        @Column(name = "secondary_logicals")
        val secondaryLogicals: Int,

        /** 排名 */
        @Column(name = "rank")
        val rank: Int?,

        /** 样本数量，有些是数字有些是字符串数字（mmp */
        @Column(name = "samples")
        val samples: Int,

        /** 拼接到 https://www.cpubenchmark.net/cpu.php?cpu= 后面即可打开 CPU 详情页 */
        @Column(name = "href")
        val href: String,

        /** 用途未知 */
        @Column(name = "output")
        val output: Boolean,
) {
    /** 平台类型 */
    enum class Cat(
            private val realName: String? = null
    ) {
        Desktop,
        Laptop,
        MobileEmbedded("Mobile/Embedded"),
        Server,
        Unknown;

        override fun toString(): String {
            return realName ?: name
        }
    }

    data class Id(
            @Column(name = "id")
            val id: Int = -1,

            @Column(name = "cpuCount")
            val cpuCount: Int = 1,
    ): Serializable
}

val String.asCpuCatList: List<CpuPerfMarkEntity.Cat> get() {
    return replace(" ", "")
            .split(",")
            .map { CpuPerfMarkEntity.Cat.valueOf(it.replace("/", "")) }
}

val String.asGpuCatList: List<GpuPerfMarkEntity.Cat> get() {
    return replace(" ", "")
            .split(",")
            .map { GpuPerfMarkEntity.Cat.valueOf(it) }
}

@Entity(name = "gpu_mega")
data class GpuPerfMarkEntity(
        /** CPU ID，似乎是数字字符串 */
        @Id
        @Column(name = "id")
        val id: Int,

        /** 完整名称 */
        @Column(name = "name")
        val name: String,

        /** 价格，单位：美元，包含$前导 */
        @Column(name = "price")
        val price: Double?,

        /** 2D 跑分 */
        @Column(name = "g2d")
        val g2d: Int?,

        /** 3D 跑分 */
        @Column(name = "g3d")
        val g3d: Int?,

        /** 性价比值，跑分/价格 */
        @Column(name = "value")
        val value: Double?,

        /** TDP，单位：W */
        @Column(name = "tdp")
        val tdp: Int?,

        /** 疑似是峰值功耗 */
        @Column(name = "power_perf")
        val powerPerf: Double?,

        /** 首次出现时间 */
        @Column(name = "date")
        val date: PubDate?,

        /** 平台类型 */
        @Column(name = "cat")
        val cat: List<Cat>,

        /** 总线 */
        @Column(name = "bus")
        val bus: String?,

        /** 显存大小 */
        @Column(name = "mem_size")
        val memSize: Int?,

        /** 核心频率 */
        @Column(name = "core_clock")
        val coreClk: Int?,

        /** 显存频率 */
        @Column(name = "mem_clock")
        val memClk: Int?,

        /** 排名 */
        @Column(name = "rank")
        val rank: Int?,

        /** 样本数量，有些是数字有些是字符串数字（mmp */
        @Column(name = "samples")
        val samples: Int,

        /** 拼接到 https://www.cpubenchmark.net/cpu.php?cpu= 后面即可打开 CPU 详情页 */
        @Column(name = "href")
        val href: String,

        /** 用途未知 */
        @Column(name = "output")
        val output: Boolean,
) {
    /** 平台类型 */
    enum class Cat {
        Desktop,
        Mobile,
        Workstation,
        Unknown;
    }
}


@Entity(name = "update_log")
data class UpdateLogEntity(
        @Id
        @Column(name = "id")
        val id: Type,

        @Column(name = "remote_time")
        val remoteTime: String,

        @Column(name = "update_time")
        val updateTime: Long,
) {
    enum class Type {
        Gpu, Cpu;
    }
}
