package com.github.kondury.flashcards.placedcards.repository.inmemory.model

import com.github.kondury.flashcards.placedcards.common.models.*
import kotlinx.datetime.Instant

data class PlacedCardEntity(
    val id: String? = null,
    val lock: String? = null,
    val ownerId: String? = null,
    val box: String? = null,
    val cardId: String? = null,
    val createdAt: String,
    val updatedAt: String,
) {
    constructor(model: PlacedCard) : this(
        id = model.id.asStringOrNull(),
        lock = model.lock.asStringOrNull(),
        ownerId = model.ownerId.asStringOrNull(),
        box = model.box.asStringOrNull(),
        cardId = model.cardId.asStringOrNull(),
        createdAt = model.createdAt.toString(),
        updatedAt = model.updatedAt.toString(),
    )

    fun toInternal() = PlacedCard(
        id = id.mapOrDefault(::PlacedCardId, PlacedCardId.NONE),
        lock = lock.mapOrDefault(::FcPlacedCardLock, FcPlacedCardLock.NONE),
        ownerId = ownerId.mapOrDefault(::UserId, UserId.NONE),
        box = box.mapOrDefault(FcBox::valueOf, FcBox.NONE),
        cardId = cardId.mapOrDefault(::CardId, CardId.NONE),
        createdAt = Instant.parse(createdAt),
        updatedAt = Instant.parse(updatedAt),
    )
}

internal fun <T> String?.mapOrDefault(create: (String) -> T, defaultValue: T) =
    this?.let(create) ?: defaultValue

internal fun PlacedCardId.asStringOrNull() = this.asString().takeNonBlankOrNull()
internal fun FcPlacedCardLock.asStringOrNull() = this.asString().takeNonBlankOrNull()
internal fun UserId.asStringOrNull() = this.asString().takeNonBlankOrNull()
internal fun CardId.asStringOrNull() = this.asString().takeNonBlankOrNull()

internal fun FcBox.asStringOrNull() = this.takeIf { it.isNotEmpty() }?.name

private fun String.takeNonBlankOrNull() = this.takeIf { it.isNotBlank() }

