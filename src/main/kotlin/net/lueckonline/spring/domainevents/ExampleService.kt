package net.lueckonline.spring.domainevents

import org.apache.logging.log4j.kotlin.logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import kotlin.random.Random

@Service
class ExampleService (
    @Autowired val repository: ExampleRepository
){

    @Transactional
    fun runExampleAsOneTransaction(id: Int, name: String) {
        logger.info("creating example")
        val example1 = this.createExample(Random.nextInt(), "Test")
        logger.info("Created example: $example1")
        logger.info("deleting example: $example1")
        this.deleteExample(example1)
        logger.info("deleted example: $example1")
    }

    fun createExample(id: Int, name: String): ExampleEntity{

        val entityToSave = ExampleEntity(id, name)
        // if we don't call this directly but depend on the @PrePersist annotation
        // the created event is not evaluated during the save operation
        //entityToSave.addCreatedEvent()
        logger.info("saving entity $entityToSave")
        val savedEntity = repository.saveAndFlush(entityToSave)
        logger.info("saved entity $savedEntity")
        return savedEntity
    }

    fun deleteExample(example: ExampleEntity) {
        logger.info("deleting entity")
        repository.delete(example)
        logger.info("deleted entity")
    }

    @EventListener(classes = [ExampleCreated::class])
    fun handle(event: ExampleCreated){
        logger.info("Example created")
    }

    @EventListener(classes = [ExampleRemoved::class])
    fun handle(event: ExampleRemoved){
        logger.info("Example removed")
    }
}