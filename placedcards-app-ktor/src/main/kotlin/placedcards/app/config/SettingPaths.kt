package com.github.kondury.flashcards.placedcards.app.config

object SettingPaths {
    private const val APP_ROOT = "placed-cards"
    const val REPOSITORY_PATH = "$APP_ROOT.repository"
    const val IN_MEMORY_PATH = "$REPOSITORY_PATH.in-memory"
    const val POSTGRES_PATH = "$REPOSITORY_PATH.psql"
    const val JWT_PATH = "$APP_ROOT.jwt"
}