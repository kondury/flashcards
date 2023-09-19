package com.github.kondury.flashcards.blackbox.test

import com.github.kondury.flashcards.blackbox.fixture.client.RestClient
import io.kotest.core.annotation.Ignored
import com.github.kondury.flashcards.blackbox.docker.WiremockDockerCompose
import com.github.kondury.flashcards.blackbox.fixture.BaseFunSpec
import com.github.kondury.flashcards.blackbox.fixture.docker.DockerCompose

@Ignored
open class AccRestTestBase(dockerCompose: DockerCompose) : BaseFunSpec(dockerCompose, {
    val client = RestClient(dockerCompose)
    testApiV1(client)
})

class AccRestWiremockTest : AccRestTestBase(WiremockDockerCompose)
// TODO class AccRestSpringTest : AccRestTestBase(SpringDockerCompose)
// TODO class AccRestKtorTest : AccRestTestBase(KtorDockerCompose)
