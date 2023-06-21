package io.github.perfmarktop.core

import io.github.perfmarktop.Application
import io.github.sgpublic.kotlin.util.Loggable
import org.ini4j.Profile
import org.ini4j.Wini
import org.ini4j.spi.BeanTool
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.io.File
import java.io.IOException
import java.util.*
import kotlin.reflect.KProperty

/**
 * @author sgpublic
 * @Date 2022/8/5 11:41
 */
@Component
class Config: Loggable {
    companion object {
        private const val SectionCommon = "common"
        val Port by IniVal.New(SectionCommon, "port", 11451)
        val Debug by IniVal.New(SectionCommon, "debug", false)
        val LogPath by IniVal.New(SectionCommon, "log-dir", "./logs")
        val UpdateTick by IniVal.New(SectionCommon, "update-tick", "0 0 */8 * * *")

        private const val SectionSql = "sql"
        val SqlUsername by IniVal.New(SectionSql, "username", Application.APPLICATION_BASE_NAME_LOWER)
        val SqlPassword by IniVal.New(SectionSql, "password", "")
        val SqlDatabaseHost by IniVal.New(SectionSql, "host", "localhost:3306")
        val SqlDatabaseName by IniVal.New(SectionSql, "name", Application.APPLICATION_BASE_NAME_LOWER)
        val SqlDatabasePlatform by IniVal.New(SectionSql, "dialect", "org.hibernate.dialect.MariaDB106Dialect")



        private var config = File("./config", "${Application.APPLICATION_BASE_NAME_LOWER}.ini")
        private val ini: Wini = Wini()

        init {
            ini.file = config
        }
    }

    init {
        if (!config.exists()) {
            val parent = config.parentFile
            if ((parent.exists() || !parent.mkdirs()) && !config.createNewFile()) {
                log.warn("配置文件创建失败，将使用默认配置！")
            }
        }
        ini.file = config
    }

    @Value("\${config:./config/${Application.APPLICATION_BASE_NAME_LOWER}.ini}")
    fun setConfigFile(path: String) {
        config = File(path)
        if (!config.isFile) {
            throw IllegalAccessException("配置文件不可用：$path")
        }
        log.info("使用配置文件：${
            try {
                config.canonicalPath
            } catch (e: IOException) {
                config.path
            }
        }")
    }

    class IniVar<TypeT>(section: String, key: String, defVal: TypeT, clazz: Class<TypeT>) :
            IniVal<TypeT>(section, key, defVal, clazz) {
        operator fun setValue(companion: Any?, property: KProperty<*>, value: TypeT) {
            getSection(section)?.let {
                it[key] = toIni(value)
                ini[section] = it
            }
            ini.store()
        }

        private fun toIni(value: TypeT): String {
            return value.toString()
        }

        companion object {
            @Suppress("FunctionName")
            inline fun <reified TypeT> New(
                    section: String, key: String, devVal: TypeT
            ): IniVar<TypeT> {
                return IniVar(section, key, devVal, TypeT::class.java)
            }
        }
    }

    open class IniVal<TypeT>(
            protected val section: String,
            protected val key: String,
            private val devVal: TypeT,
            private val clazz: Class<TypeT>
    ) {
        private fun fromIni(origin: String?): TypeT {
            return BeanTool.getInstance().parse(origin, clazz) ?: devVal
        }

        operator fun getValue(companion: Any?, property: KProperty<*>): TypeT {
            return try {
                fromIni(getSection(section)?.get(key))
            } catch (e: Exception) {
                devVal
            }
        }

        companion object {
            @Throws(IOException::class)
            @JvmStatic
            protected fun getSection(sectionName: String?): Profile.Section? {
                ini.load()
                val section: Profile.Section? = ini[sectionName]
                if (section == null) {
                    ini.add(sectionName)
                    ini.store()
                }
                return ini[sectionName]
            }

            @Suppress("FunctionName")
            inline fun <reified TypeT> New(
                    section: String, key: String, devVal: TypeT
            ): IniVal<TypeT> {
                return IniVal(section, key, devVal, TypeT::class.java)
            }
        }
    }
}
