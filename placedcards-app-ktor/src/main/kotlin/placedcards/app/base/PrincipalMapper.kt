package com.github.kondury.flashcards.placedcards.app.base

import com.github.kondury.flashcards.placedcards.app.common.AuthConfig.Companion.F_NAME_CLAIM
import com.github.kondury.flashcards.placedcards.app.common.AuthConfig.Companion.GROUPS_CLAIM
import com.github.kondury.flashcards.placedcards.app.common.AuthConfig.Companion.ID_CLAIM
import com.github.kondury.flashcards.placedcards.app.common.AuthConfig.Companion.L_NAME_CLAIM
import com.github.kondury.flashcards.placedcards.app.common.AuthConfig.Companion.M_NAME_CLAIM
import com.github.kondury.flashcards.placedcards.common.models.UserId
import com.github.kondury.flashcards.placedcards.common.permissions.FcPrincipalModel
import com.github.kondury.flashcards.placedcards.common.permissions.FcUserGroups
import io.ktor.server.auth.jwt.*


fun JWTPrincipal?.toModel() = this?.run {
    FcPrincipalModel(
        id = payload.getClaim(ID_CLAIM).asString()?.let { UserId(it) } ?: UserId.NONE,
        fname = payload.getClaim(F_NAME_CLAIM).asString() ?: "",
        mname = payload.getClaim(M_NAME_CLAIM).asString() ?: "",
        lname = payload.getClaim(L_NAME_CLAIM).asString() ?: "",
        groups = payload
            .getClaim(GROUPS_CLAIM)
            ?.asList(String::class.java)
            ?.mapNotNull {
                when(it) {
                    "USER" -> FcUserGroups.USER
                    "ADMIN" -> FcUserGroups.ADMIN
                    else -> null
                }
            }?.toSet() ?: emptySet()
    )
} ?: FcPrincipalModel.NONE
