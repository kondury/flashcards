package com.github.kondury.flashcards.cards.common.exception

import com.github.kondury.flashcards.cards.common.models.FcCardLock

class OptimisticLockException(expectedLock: FcCardLock, actualLock: FcCardLock?) :
    RuntimeException("Expected lock is $expectedLock while actual lock in db is $actualLock")
