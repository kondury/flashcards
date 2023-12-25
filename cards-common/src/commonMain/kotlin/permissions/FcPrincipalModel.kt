package com.github.kondury.flashcards.cards.common.permissions

import com.github.kondury.flashcards.cards.common.models.UserId

data class FcPrincipalModel(
    val id: UserId = UserId.NONE,
    val fname: String = "",
    val mname: String = "",
    val lname: String = "",
    val groups: Set<FcUserGroups> = emptySet()
) {
    companion object {
        val NONE = FcPrincipalModel()
    }
}
