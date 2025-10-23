package net.lueckonline.spring.domainevents.entities

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.PrePersist
import jakarta.persistence.PreRemove
import net.lueckonline.spring.domainevents.entities.ExampleEntity
import net.lueckonline.spring.domainevents.events.ExampleCreated
import net.lueckonline.spring.domainevents.events.ExampleRemoved
import org.apache.logging.log4j.kotlin.logger
import org.springframework.data.domain.AbstractAggregateRoot

@Entity
class GeneratedIdEntity(
    val name: String
) : AbstractAggregateRoot<GeneratedIdEntity>(), ExampleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    var id: Int? = null

    @PrePersist
    fun addCreatedEvent() {
        logger.info("Adding created event for $this")
        this.registerEvent(ExampleCreated(this))
    }

    @PreRemove
    fun addRemovedEvent() {
        logger.info("Adding removed event for $this")
        this.registerEvent(ExampleRemoved(this))
    }

    override fun domainEvents(): MutableCollection<Any> {
        val events = super.domainEvents()
        logger.info("Getting domain events for $this. events=${events}")
        return events
    }

    override fun clearDomainEvents() {
        logger.info("Clearing domain events for $this")
        super.clearDomainEvents()
    }
}