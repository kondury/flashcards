package com.github.kondury.flashcards.cards.mappers.v1.exceptions

import com.github.kondury.flashcards.cards.common.models.CardCommand

class UnknownFcCommand(command: CardCommand) : Throwable("Wrong command $command at mapping toTransport stage")
