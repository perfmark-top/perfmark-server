package io.github.perfmarktop

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class PerfmarkServerApplication

fun main(args: Array<String>) {
    runApplication<PerfmarkServerApplication>(*args)
}
