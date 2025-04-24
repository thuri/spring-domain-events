package net.lueckonline.spring.domainevents

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

    val exampleService = context.getBean(ExampleService::class.java)
    logger.info("Running example")
    exampleService.runExampleAsOneTransaction(Random.nextInt(), "Example 1")
    logger.info("Ran example")

//    Thread.currentThread().join()
}