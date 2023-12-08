package com.github.kondury.flashcards.placedcards.common.exception

import com.github.kondury.flashcards.placedcards.common.models.FcPlacedCardLock

class OptimisticLockException(expectedLock: FcPlacedCardLock, actualLock: FcPlacedCardLock?) :
    RuntimeException("Expected lock is $expectedLock while actual lock in db is $actualLock")
