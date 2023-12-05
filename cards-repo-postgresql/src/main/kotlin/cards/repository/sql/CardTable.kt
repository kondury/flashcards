package com.github.kondury.flashcards.cards.repository.sql

import com.github.kondury.flashcards.cards.common.models.Card
import com.github.kondury.flashcards.cards.common.models.CardId
import com.github.kondury.flashcards.cards.common.models.FcCardLock
import com.github.kondury.flashcards.cards.common.models.isNotEmpty
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.statements.UpdateBuilder

class CardTable(tableName: String = "cards") : Table(tableName) {

    val id = varchar("card_id", 128)
    private val front = text("front")
    private val back = text("back")
    private val lock = varchar("lock", 50)

    override val primaryKey = PrimaryKey(id)

    fun fromRow(res : ResultRow) = Card(
        id = CardId(res[id].toString()),
        front = res[front],
        back = res[back],
        lock = FcCardLock(res[lock]),
    )

    fun toRow(it: UpdateBuilder<*>, card: Card, randomUuid: () -> String) {
        it[id] = card.id.takeIf { it.isNotEmpty() }?.asString() ?: randomUuid()
        it[back] = card.back
        it[front] = card.front
        it[lock] = card.lock.takeIf { it.isNotEmpty() }?.asString() ?: randomUuid()
    }
}