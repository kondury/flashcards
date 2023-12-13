package com.github.kondury.flashcards.placedcards.repository.sql

import com.github.kondury.flashcards.placedcards.common.models.*
import kotlinx.datetime.toJavaInstant
import kotlinx.datetime.toKotlinInstant
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.timestamp
import org.jetbrains.exposed.sql.statements.UpdateBuilder

class PlacedCardTable(tableName: String = "placed_cards") : Table(tableName) {
    val id = varchar("placed_card_id", 128)
    val lock = varchar("lock", 50)
    val ownerId = varchar("owner_id", 128)
    val cardId = varchar("card_id", 128)
    val box = enumeration("box", FcBox::class)
    val createdAt = timestamp("created_at")
    val updatedAt = timestamp("updated_at")


    override val primaryKey = PrimaryKey(id)

    fun fromRow(row: ResultRow) = PlacedCard(
        id = PlacedCardId(row[id].toString()),
        lock = FcPlacedCardLock(row[lock]),
        ownerId = UserId(row[ownerId].toString()),
        cardId = CardId(row[cardId].toString()),
        box = row[box],
        createdAt = row[createdAt].toKotlinInstant(),
        updatedAt = row[updatedAt].toKotlinInstant()
    )

    fun toRow(it: UpdateBuilder<*>, placedCard: PlacedCard, randomUuid: () -> String) {
        it[id] = placedCard.id.takeIf { it.isNotEmpty() }?.asString() ?: randomUuid()
        it[lock] = placedCard.lock.takeIf { it.isNotEmpty() }?.asString() ?: randomUuid()
        it[ownerId] = placedCard.ownerId.asString()
        it[cardId] = placedCard.cardId.asString()
        it[box] = placedCard.box
        it[createdAt] = placedCard.createdAt.toJavaInstant()
        it[updatedAt] = placedCard.updatedAt.toJavaInstant()
    }
}