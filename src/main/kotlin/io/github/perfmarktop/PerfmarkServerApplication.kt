package io.github.perfmarktop

import io.github.perfmarktop.core.Config
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.ApplicationContext
import org.springframework.scheduling.annotation.EnableScheduling
import java.util.*
import kotlin.collections.HashMap

@EnableScheduling
@SpringBootApplication
class Application {
    companion object {
        const val APPLICATION_BASE_NAME = "PerfMark"
        const val APPLICATION_BASE_NAME_LOWER = "perfmark"

        @JvmStatic
        fun main(args: Array<String>) {
            Bootstrap(Application::class.java)
                    .setDebug(Config.Debug)
                    .setPort(Config.Port)
                    .setLogPath(Config.LogPath)
                    .setDatasource(
                            Config.SqlDatabaseHost,
                            Config.SqlDatabaseName,
                            Config.SqlUsername,
                            Config.SqlPassword,
                            Config.SqlDatabasePlatform,
                    )
                    .run(args)
        }
    }
}



private class Bootstrap(clazz: Class<*>) {
    private val application: SpringApplication = SpringApplication(clazz)
    private val properties: MutableMap<String, Any> = HashMap()

    fun setPort(port: Int): Bootstrap {
        properties["server.port"] = port
        return this
    }

    fun setDatasource(
            host: String, name: String,
            username: String, password: String,
            dialect: String,
    ): Bootstrap {
        properties["spring.datasource.username"] = username
        properties["spring.datasource.password"] = password
        properties["spring.datasource.url"] = "jdbc:mariadb://$host/$name"
        properties["spring.jpa.database-platform"] = dialect
        return this
    }

    fun setDebug(isDebug: Boolean): Bootstrap {
        if (isDebug) {
            properties["spring.profiles.active"] = "dev"
        } else {
            properties["spring.profiles.active"] = "prod"
        }
        return this
    }

    fun setLogPath(path: String): Bootstrap {
        properties["logging.path"] = path
        return this
    }

    fun run(args: Array<String>): ApplicationContext {
        application.setDefaultProperties(properties)
        return application.run(*args)
    }
}
