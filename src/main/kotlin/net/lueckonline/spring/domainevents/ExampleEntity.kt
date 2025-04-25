package net.lueckonline.spring.domainevents

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.PrePersist
import jakarta.persistence.PreRemove
import org.apache.logging.log4j.kotlin.logger
import org.springframework.data.domain.AbstractAggregateRoot

@Entity
class ExampleEntity (
    @Id
    val id: Int,
    val name: String
) : AbstractAggregateRoot<ExampleEntity>() {

    // @PrePersist - if the method is called as PrePersist handler, the event is
    // registered on another instance of the entity - the one that is managed
    // by JPA/hibernate and which is returned by the save method of the repository.
    // if we call this on a manually created entity the event is registered exactly that instance.
    // when the domainEvents are fetched during the save operation only the domain events of
    // the manually created object are fetched an passed on to the event handlers
//    @PrePersist
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