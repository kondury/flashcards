package com.github.kondury.flashcards.logging.common

import kotlinx.datetime.Clock
import kotlin.time.measureTimedValue

@Suppress("unused")
interface AppLogger {
    val loggerId: String

    fun log(
        msg: String = "",
        level: Level = Level.TRACE,
        marker: String = "DEV",
        e: Throwable? = null,
        data: Any? = null,
        objs: Map<String, Any>? = null,
    )

    fun error(
        msg: String = "",
        marker: String = "DEV",
        e: Throwable? = null,
        data: Any? = null,
        objs: Map<String, Any>? = null,
    ) = log(msg, Level.ERROR, marker, e, data, objs)

    fun warn(
        msg: String = "",
        marker: String = "DEV",
        e: Throwable? = null,
        data: Any? = null,
        objs: Map<String, Any>? = null,
    ) = log(msg, Level.WARN, marker, e, data, objs)

    fun info(
        msg: String = "",
        marker: String = "DEV",
        data: Any? = null,
        objs: Map<String, Any>? = null,
    ) = log(msg, Level.INFO, marker, null, data, objs)

    fun debug(
        msg: String = "",
        marker: String = "DEV",
        data: Any? = null,
        objs: Map<String, Any>? = null,
    ) = log(msg, Level.DEBUG, marker, null, data, objs)

    /**
     * Функция обертка для выполнения прикладного кода с логированием перед выполнением и после
     */
    suspend fun <T> withLogging(
        id: String = "",
        level: Level = Level.INFO,
        block: suspend () -> T,
    ): T = try {

        log("Started $loggerId $id", level)
        val (res, diffTime) = measureTimedValue { block() }

        log(
            msg = "Finished $loggerId $id",
            level = level,
            objs = mapOf("metricHandleTime" to diffTime.toIsoString())
        )
        res
    } catch (e: Throwable) {
        log(
            msg = "Failed $loggerId $id",
            level = Level.ERROR,
            e = e
        )
        throw e
    }

    /**
     * Функция обертка для выполнения прикладного кода с логированием ошибки
     */
    suspend fun <T> withErrorLogging(
        id: String = "",
        throwRequired: Boolean = true,
        block: suspend () -> T,
    ): T? = try {
        val result = block()
        result
    } catch (e: Throwable) {
        log(
            msg = "Failed $loggerId $id",
            level = Level.ERROR,
            e = e
        )
        if (throwRequired) throw e else null
    }

    companion object {
        val DEFAULT = object: AppLogger {
            override val loggerId: String = "NONE"

            override fun log(
                msg: String,
                level: Level,
                marker: String,
                e: Throwable?,
                data: Any?,
                objs: Map<String, Any>?,
            ) {
                val markerString = marker
                    .takeIf { it.isNotBlank() }
                    ?.let { " ($it)" }
                val args = listOfNotNull(
                    "${Clock.System.now().toString()} [${level.name}]$markerString: $msg",
                    e?.let { "${it.message ?: "Unknown reason"}:\n${it.stackTraceToString()}" },
                    data.toString(),
                    objs.toString(),
                )
                println(args.joinToString("\n"))
            }

        }
    }
}
