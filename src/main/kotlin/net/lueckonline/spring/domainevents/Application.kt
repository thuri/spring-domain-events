package net.lueckonline.spring.domainevents

import org.apache.logging.log4j.kotlin.logger
import org.apache.logging.log4j.kotlin.loggerOf
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.transaction.annotation.Transactional
import kotlin.random.Random

@SpringBootApplication
class Application

@Transactional
fun main(args: Array<String>) {
    val logger = loggerOf(Application::class.java)

    val context = runApplication<Application>(*args)

    val service = context.getBean(ExampleService::class.java).apply {
        logger.info("Running example")

        logger.info("creating example")
        val example1 = this.createExample(Random.nextInt(), "Test")
        logger.info("Created example: $example1")

        logger.info("deleting example: $example1")
        this.deleteExample(example1)
        logger.info("deleted example: $example1")

        logger.info("Ran example")
    }

    service.runAsOnTransaction(Random.nextInt(), "Test 2")

//    Thread.currentThread().join()
}