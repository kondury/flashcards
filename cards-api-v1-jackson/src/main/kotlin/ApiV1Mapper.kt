package com.github.kondury.flashcards.cards.api.v1

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.json.JsonMapper

val apiV1Mapper: JsonMapper = JsonMapper.builder().run {
//    configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    enable(MapperFeature.USE_BASE_TYPE_AS_DEFAULT_IMPL)
    serializationInclusion(JsonInclude.Include.NON_NULL)
    build()
}



