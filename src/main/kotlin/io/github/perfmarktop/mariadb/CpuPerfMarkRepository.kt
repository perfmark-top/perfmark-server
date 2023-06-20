package io.github.perfmarktop.mariadb

import jakarta.persistence.Column
import jakarta.persistence.Converter
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.springframework.data.jpa.repository.JpaRepository

/**
 * @author Madray Haven
 * @Date 2023/6/20 下午5:21
 */
interface CpuPerfMarkDao: JpaRepository<CpuPerfMarkEntity, Int> {

}

@Table
data class CpuPerfMarkEntity(
    /** CPU ID，似乎是数字字符串 */
    @Id
    @Column(name = "id")
    val id: Int,

    /** 完整名称 */
    @Column(name = "name")
    val name: String,

    /** 平台类型 */
    @Column(name = "id")
    val cat: List<Cat>,

    /** 核心数，当 CPU 为大小核设计时，此字段表示大核核心数 */
    @Column(name = "cores")
    val cores: Int,

    /** 支持多路，单路的时候是数字1，多路的时候是字符串数字（好像是这样 */
    @Column(name = "cpuCount")
    val cpuCount: Int,

    /** 跑分 */
    @Column(name = "cpumark")
    val cpumark: Int,

    /** 性价比值，跑分/价格 */
    @Column(name = "value")
    val value: Double,

    /** 单线程性价比值，单线程/价格 */
    @Column(name = "id")
    val threadValue: Double,

    /** 首次出现时间 */
    @Column(name = "date")
    val date: String,

    /** 拼接到 https://www.cpubenchmark.net/cpu.php?cpu= 后面即可打开 CPU 详情页 */
    @Column(name = "href")
    val href: String,

    /** 线程数，当 CPU 为大小核设计时，此字段表示大核线程数 */
    @Column(name = "logicals")
    val logicals: Int,

    /** 疑似是峰值功耗 */
    @Column(name = "powerPerf")
    val powerPerf: Int,

    /** 价格，单位：美元，包含$前导 */
    @Column(name = "price")
    val price: Int,

    /** 排名 */
    @Column(name = "id")
    val rank: Long,

    /** 样本数量，有些是数字有些是字符串数字（mmp */
    @Column(name = "id")
    val samples: Int,

    /** 小核核心数，当 CPU 为大小核设计时，此字段大于 0（有些是数字有些是字符串数字mmp */
    @Column(name = "id")
    val secondaryCores: Int,

    /** 小核线程数，当 CPU 为大小核设计时，此字段大于 0（有些是数字有些是字符串数字mmp */
    @Column(name = "id")
    val secondaryLogicals: Int,

    /** 接口类型 */
    @Column(name = "socket")
    val socket: String,

    /** 默频 */
    @Column(name = "speed")
    val speed: Int?,

    /** 睿频 */
    @Column(name = "turbo")
    val turbo: Int?,

    /** TDP，单位：W */
    @Column(name = "tdp")
    val tdp: Int?,

    /** 单线程，单位：MOps/Sec */
    @Column(name = "thread")
    val thread: Int,

    /** 用途未知 */
    @Column(name = "output")
    val output: Boolean,
) {
    /** 平台类型 */
    enum class Cat {
        Desktop,
        Laptop,
        MobileEmbedded,
        Server,
        Unknown,
    }
}