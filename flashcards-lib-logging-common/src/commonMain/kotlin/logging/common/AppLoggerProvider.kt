package com.github.kondury.flashcards.logging.common

import kotlin.reflect.KClass

class AppLoggerProvider(
    private val provider: (String) -> AppLogger = { AppLogger.DEFAULT }
) {
    fun logger(loggerId: String) = provider(loggerId)
    fun logger(clazz: KClass<*>) = provider(clazz.qualifiedName ?: clazz.simpleName ?: "(unknown)")
}
