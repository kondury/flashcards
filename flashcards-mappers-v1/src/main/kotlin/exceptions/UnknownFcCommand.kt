package com.github.kondury.flashcards.mappers.v1.exceptions

import com.github.kondury.flashcards.common.models.FcCommand

class UnknownFcCommand(command: FcCommand) : Throwable("Wrong command $command at mapping toTransport stage")
