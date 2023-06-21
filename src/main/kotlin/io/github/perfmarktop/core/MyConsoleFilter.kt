package io.github.perfmarktop.core

import io.github.perfmarktop.Application
import io.github.sgpublic.kotlin.core.logback.filter.ConsoleFilter

/**
 * @author Madray Haven
 * @Date 2023/6/21 下午2:13
 */
class MyConsoleFilter: ConsoleFilter(
        debug = Config.Debug,
        baseName = Application::class.java.packageName
)