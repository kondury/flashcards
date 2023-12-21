package com.github.kondury.flashcards.placedcards.app.common

data class AuthConfig(
    val secret: String,
    val issuer: String,
    val audience: String,
    val realm: String,
    val clientId: String,
    val certUrl: String? = null,
) {
    companion object {
        const val ID_CLAIM = "sub"
        const val GROUPS_CLAIM = "groups"
        const val F_NAME_CLAIM = "fname"
        const val M_NAME_CLAIM = "mname"
        const val L_NAME_CLAIM = "lname"

        // todo
        val TEST = AuthConfig(
            secret = "secret",
            issuer = "com.github.kondury",
            audience = "flashcards-users",
            realm = "flashcards-users",
            clientId = "flashcards-users-service",
        )

        val NONE = AuthConfig(
            secret = "",
            issuer = "",
            audience = "",
            realm = "",
            clientId = "",
        )
    }
}
