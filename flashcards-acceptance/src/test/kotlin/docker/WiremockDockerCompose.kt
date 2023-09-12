package com.github.kondury.flashcards.blackbox.docker

import com.github.kondury.flashcards.blackbox.fixture.docker.AbstractDockerCompose

object WiremockDockerCompose : AbstractDockerCompose(
    "app-wiremock_1", 8080, "wiremock/docker-compose-wiremock.yml"
)
