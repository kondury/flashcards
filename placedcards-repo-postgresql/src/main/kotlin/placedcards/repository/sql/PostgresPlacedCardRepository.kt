package com.github.kondury.flashcards.placedcards.repository.sql

import com.benasher44.uuid.uuid4
import com.github.kondury.flashcards.placedcards.common.helpers.asFcError
import com.github.kondury.flashcards.placedcards.common.models.*
import com.github.kondury.flashcards.placedcards.common.repository.*
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

class PostgresPlacedCardRepository(
    properties: SqlProperties,
    initObjects: Collection<PlacedCard> = emptyList(),
    val randomUuid: () -> String = { uuid4().toString() },
) : PlacedCardRepository {
    private val placedCardTable = PlacedCardTable(properties.table)

    private val driver = when {
        properties.url.startsWith("jdbc:postgresql://") -> "org.postgresql.Driver"
        else -> throw IllegalArgumentException("Unknown driver for url ${properties.url}")
    }

    private val connection = Database.connect(
        properties.url, driver, properties.user, properties.password
    )

    private fun getPostgresNow(): Instant = Clock.System.now().let {
        val sec = it.epochSeconds
        val nanoSec = it.nanosecondsOfSecond / 1000 * 1000
        Instant.fromEpochSeconds(sec, nanoSec)
    }

    init {
        transaction(connection) {
            addLogger(StdOutSqlLogger)
            SchemaUtils.create(placedCardTable)
            initObjects.forEach { create(it) }
        }
    }

    override suspend fun create(request: PlacedCardDbRequest): PlacedCardDbResponse =
        transactionWrapper {
            val timestamp = getPostgresNow()
            val newPlacedCard = request.placedCard.copy(createdAt = timestamp, updatedAt = timestamp)
            create(newPlacedCard)
        }

    override suspend fun read(request: PlacedCardIdDbRequest): PlacedCardDbResponse =
        transactionWrapper { getById(request.id) }

    override suspend fun delete(request: PlacedCardIdDbRequest): PlacedCardDbResponse =
        transactionWrapper {
            updateOptimistic(request.id, request.lock) {
                placedCardTable.deleteWhere { placedCardTable.id eq request.id.asString() }
                PlacedCardDbResponse.SUCCESS_EMPTY
            }
        }

    override suspend fun move(request: PlacedCardMoveDbRequest): PlacedCardDbResponse =
        transactionWrapper {
            updateOptimistic(request.id, request.lock) { storedValue ->
                val timestamp = getPostgresNow()
                val updatedPlacedCard = storedValue.copy(
                    box = request.box,
                    lock = FcPlacedCardLock(randomUuid()),
                    updatedAt = timestamp,
                )
                placedCardTable.update(where = { placedCardTable.id eq request.id.asString() }) {
                    toRow(it = it, placedCard = updatedPlacedCard, randomUuid = randomUuid)
                }
                getById(request.id)
            }
        }

    override suspend fun select(request: PlacedCardSelectDbRequest): PlacedCardDbResponse =
        transactionWrapper {
            val (ownerId, strategy, box) = request

            val timeSelector = when (strategy) {
                FcSearchStrategy.EARLIEST_CREATED -> placedCardTable.createdAt
                FcSearchStrategy.EARLIEST_REVIEWED, FcSearchStrategy.NONE -> placedCardTable.updatedAt
            }

            val query = placedCardTable
                .select {
                    buildList {
                        add(placedCardTable.ownerId eq ownerId.asString())
                        add(placedCardTable.box eq box)
                    }.fold(Op.TRUE as Op<Boolean>) { a, b -> a and b }
                }.orderBy(timeSelector to SortOrder.ASC)
                .limit(1)

            query.mapSingleOrNull { PlacedCardDbResponse.success(it) }
                ?: PlacedCardDbResponse.notFoundErrorResponse
        }

    private fun transactionWrapper(block: () -> PlacedCardDbResponse): PlacedCardDbResponse =
        try {
            transaction(connection) {
                addLogger(StdOutSqlLogger)
                block()
            }
        } catch (e: Exception) {
            PlacedCardDbResponse.error(e.asFcError())
        }

    private fun create(requestCard: PlacedCard): PlacedCardDbResponse =
        placedCardTable.insert { toRow(it, requestCard, randomUuid) }.resultedValues
            ?.mapSingleOrNull { PlacedCardDbResponse.success(it) }
            ?: throw RuntimeException("DB error: insert statement returned empty result")

    private fun updateOptimistic(
        id: PlacedCardId,
        lock: FcPlacedCardLock,
        updateBlock: (PlacedCard) -> PlacedCardDbResponse
    ): PlacedCardDbResponse =
        getById(id) {
            if (it.lock != lock) PlacedCardDbResponse.errorConcurrent(lock, it)
            else updateBlock(it)
        }

    private fun getById(
        id: PlacedCardId,
        postProcess: (PlacedCard) -> PlacedCardDbResponse = { PlacedCardDbResponse.success(it) }
    ): PlacedCardDbResponse =
        if (id.isEmpty()) PlacedCardDbResponse.emptyIdErrorResponse
        else {
            placedCardTable.select { placedCardTable.id eq id.asString() }
                .mapSingleOrNull { postProcess(it) }
                ?: PlacedCardDbResponse.notFoundErrorResponse
        }

    private fun Iterable<ResultRow>.mapSingleOrNull(
        postProcess: (PlacedCard) -> PlacedCardDbResponse
    ): PlacedCardDbResponse? =
        this.singleOrNull()
            ?.let { placedCardTable.fromRow(it) }
            ?.let { postProcess(it) }
}