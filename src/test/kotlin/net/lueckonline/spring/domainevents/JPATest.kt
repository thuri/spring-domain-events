package net.lueckonline.spring.domainevents

import net.lueckonline.spring.domainevents.entities.AssigendIdWorkaroundEntity
import net.lueckonline.spring.domainevents.entities.AssignedIdWorkaroundEntityRepository
import net.lueckonline.spring.domainevents.entities.BadEntity
import net.lueckonline.spring.domainevents.entities.BadEntityRepository
import net.lueckonline.spring.domainevents.entities.GeneratedIdEntityRepository
import net.lueckonline.spring.domainevents.entities.GeneratedIdEntity
import net.lueckonline.spring.domainevents.events.ExampleCreated
import org.assertj.core.api.Assertions.assertThat
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.event.ApplicationEvents
import org.springframework.test.context.event.RecordApplicationEvents
import org.springframework.test.context.transaction.TestTransaction
import java.util.Random
import kotlin.test.Test

@DataJpaTest
@RecordApplicationEvents
class JPATest(
    @param:Autowired private val generatedIdEntityRepository: GeneratedIdEntityRepository,
    @param:Autowired private val badEntityRepository: BadEntityRepository,
    @param:Autowired private val assignedIdWorkaroundEntityRepository: AssignedIdWorkaroundEntityRepository
) {

    @Test
    fun shouldEmitCreatedEventForBadEntity(events: ApplicationEvents) {

        val parent = badEntityRepository.save(
            BadEntity(Random().nextInt(), "Example Entity")
        )

        assertThat(events.stream(ExampleCreated::class.java))
            .hasSize(1)
            .allSatisfy {
                assertThat(it.exampleEntity).isSameAs(parent)
            }

        TestTransaction.flagForCommit()
    }

    @Test
    fun shouldEmitCreatedEventForGeneratedIdEntity(events: ApplicationEvents) {

        val parent = generatedIdEntityRepository.save(
            GeneratedIdEntity("Example Entity")
        )

        assertThat(events.stream(ExampleCreated::class.java))
            .hasSize(1)
            .allSatisfy {
                assertThat(it.exampleEntity).isSameAs(parent)
            }

        TestTransaction.flagForCommit()
    }

    @Test
    fun shouldEmitCreatedEventForAssignedIdWorkaround(events: ApplicationEvents) {

        val parent = assignedIdWorkaroundEntityRepository.save(
            AssigendIdWorkaroundEntity(Random().nextInt(), "Example Entity")
        )

        assertThat(events.stream(ExampleCreated::class.java))
            .hasSize(1)
            .allSatisfy {
                assertThat(it.exampleEntity).isSameAs(parent)
            }

        TestTransaction.flagForCommit()
    }
}