package com.github.kondury.flashcards.placedcards.mappers.v1.exceptions

import com.github.kondury.flashcards.placedcards.common.models.PlacedCardCommand

class UnknownFcCommand(command: PlacedCardCommand) : Throwable("Wrong command $command at mapping toTransport stage")
