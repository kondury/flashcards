package com.github.kondury.flashcards.placedcards.common.permissions

import com.github.kondury.flashcards.placedcards.common.models.UserId


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
