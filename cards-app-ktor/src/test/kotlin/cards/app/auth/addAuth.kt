package com.github.kondury.flashcards.cards.app.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.github.kondury.flashcards.cards.app.common.AuthConfig
import io.ktor.client.request.*
import io.ktor.http.*


fun HttpRequestBuilder.addAuth(
    id: String = "user-id-1",
    groups: List<String> = listOf("ADMIN", "TEST"),
    config: AuthConfig
) {
    val token = JWT.create()
        .withAudience(config.audience)
        .withIssuer(config.issuer)
        .withClaim(AuthConfig.GROUPS_CLAIM, groups)
        .withClaim(AuthConfig.ID_CLAIM, id)
        .sign(Algorithm.HMAC256(config.secret))

    header(HttpHeaders.Authorization, "Bearer $token")
}
