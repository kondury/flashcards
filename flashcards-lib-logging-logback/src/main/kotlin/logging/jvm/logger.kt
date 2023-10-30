package com.github.kondury.flashcards.logging.jvm

import ch.qos.logback.classic.Logger
import com.github.kondury.flashcards.logging.common.AppLogger
import org.slf4j.LoggerFactory
import kotlin.reflect.KClass

/**
 * Generate internal MpLogContext logger
 *
 * @param logger Logback instance from [LoggerFactory.getLogger()]
 */
fun getLogbackLogger(logger: Logger): AppLogger = LogbackWrapper(
    logger = logger,
    loggerId = logger.name,
)

fun getLogbackLogger(clazz: KClass<*>): AppLogger = getLogbackLogger(LoggerFactory.getLogger(clazz.java) as Logger)

@Suppress("unused")
fun getLogbackLogger(loggerId: String): AppLogger = getLogbackLogger(LoggerFactory.getLogger(loggerId) as Logger)
