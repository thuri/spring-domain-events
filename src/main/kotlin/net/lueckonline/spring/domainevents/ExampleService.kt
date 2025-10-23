package net.lueckonline.spring.domainevents

import jakarta.persistence.EntityManager
import net.lueckonline.spring.domainevents.entities.BadEntity
import net.lueckonline.spring.domainevents.entities.BadEntityRepository
import net.lueckonline.spring.domainevents.events.ExampleCreated
import net.lueckonline.spring.domainevents.events.ExampleRemoved
import org.apache.logging.log4j.kotlin.logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ExampleService (
    @Autowired val repository: BadEntityRepository,
    @Autowired val entityManager: EntityManager,
){

    @Transactional
    fun runAsOnTransaction(id: Int, name: String) {
        logger.info("Running example")

        logger.info("creating example")
        val example1 = this.createExample(id, name)
        logger.info("Created example: $example1")

        logger.info("deleting example: $example1")
        this.deleteExample(example1)
        logger.info("deleted example: $example1")

        logger.info("Ran example")
    }

//    @Transactional
    fun createExample(id: Int, name: String): BadEntity{

        val entityToSave = BadEntity(id, name)
        // if we don't call this directly but depend on the @PrePersist annotation
        // the created event is not evaluated during the save operation
        entityToSave.addCreatedEvent()
        logger.info("saving entity $entityToSave")
        val savedEntity = repository.saveAndFlush(entityToSave)
        logger.info("saved entity $savedEntity")
        return savedEntity
    }

//    @Transactional(propagation = Propagation.REQUIRES_NEW)
    fun deleteExample(example: BadEntity) {
        logger.info("deleting entity")
        logger.info("entitymanager contains entity to delete: ${entityManager.contains(example)}")
//        example.addRemovedEvent()
        repository.delete(example)
        logger.info("deleted entity")
    }

    @EventListener
    fun handle(event: ExampleCreated) {
        logger.info("Example created")
    }

    @EventListener
    fun handle(event: ExampleRemoved) {
        logger.info("Example removed")
    }
}