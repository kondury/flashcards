package com.github.kondury.flashcards.blackbox.fixture.docker

import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.http.*
import org.testcontainers.containers.DockerComposeContainer
import java.io.File

private val logger = KotlinLogging.logger {}

/**
 * apps - список приложений в docker-compose. Первое приложение - "главное", его url возвращается как inputUrl
 * (например ваш сервис при работе по rest или брокер сообщений при работе с брокером)
 * dockerComposeName - имя docker-compose файла (относительно ok-marketplace-acceptance/docker-compose)
 */
abstract class AbstractDockerCompose(
    private val apps: List<AppInfo>,
    private val dockerComposeName: String)
: DockerCompose {

    constructor(service: String, port: Int, dockerComposeName: String)
        : this(listOf(AppInfo(service, port)), dockerComposeName)

    private fun getComposeFile(): File {
        val file = File("docker-compose/$dockerComposeName")
        if (!file.exists()) throw IllegalArgumentException("file $dockerComposeName not found!")
        return file
    }

    private val compose =
        DockerComposeContainer(getComposeFile()).apply {
            withOptions("--compatibility")
            apps.forEach { (service, port) ->
                withExposedService(
                    service,
                    port,
                )
            }
            withLocalCompose(true)
        }

    override fun start() {
        compose.start()

        logger.warn { "\n=========== $dockerComposeName started =========== \n" }
        apps.forEachIndexed { index, _ ->
            logger.info { "Started docker-compose with App at: ${getUrl(index)}" }
        }
    }

    override fun stop() {
        compose.close()
        logger.warn { "\n=========== $dockerComposeName closed =========== \n" }
    }

    override fun clearDb() {
        logger.warn { "===== clearDb =====" }
        // TODO сделать очистку БД, когда до этого дойдет
    }

    override val inputUrl: URLBuilder
        get() = getUrl(0)

    fun getUrl(no: Int) = URLBuilder(
        protocol = URLProtocol.HTTP,
        host = apps[no].let { compose.getServiceHost(it.service, it.port) },
        port = apps[no].let { compose.getServicePort(it.service, it.port) },
    )

    data class AppInfo(
        val service: String,
        val port: Int,
    )

}
