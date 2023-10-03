package com.github.kondury.flashcards.cards.mappers.v1.exceptions

class UnknownRequestClass(clazz: Class<*>) :
    RuntimeException("Request class $clazz is unsupported and cannot be mapped to context")
