# Demo Project Hibernate Events + Spring Domain Events

This project hast been created to demonstrate an issue when combining hibernate events
with spring domain events. The project was needed to explain the problem on
[Stackoverflow](https://stackoverflow.com/questions/29177582/how-to-explictly-state-that-an-entity-is-new-transient-in-jpa)

The basic idea was to use the hibernate event listeners like `@PrePersist` and `@PreRemove` to create Spring Domain
events that indicate the creation or removal of domain objects, e.g. `EntityCreated` or `EntityRemoved`.

But during implementation I had the problem that the Domain events won't be fired and the appropriate handlers were 
not called.

## project structure

The project contains 3 Entity classes which represent 3 different ways how an Id may be defined.
The `BadEntity` version is the one which doesn't work while the other two are ok.
(More on the three types later)

Currently, the issue is best seen in the [JPATest](src/main/kotlin/net/lueckonline/spring/domainevents/JPATest.kt).
The tests are basically identical but use different Entity classes. The only failing test is the one for BadEntity.

## Reason

The difference between the three entities is how the id fields gets it's value:

* `BadEntity` - the id value is assigned by the calling code via it's constructor
* `GeneratedIdEntity` - the id value is assigned by a sequence (could be another generator type)
* `AssignedIdWorkaroundEntity` - the entity implements `Persistable` interface in order to tell Spring Data whether the
  object represents a new database entry or not
* `AssignedIdWithVersionEntity` - the entity defines a `@Version` attribute which is used to evaluate whether the entity 
  is new or not

The test works for the later three entities because Spring Data knows whether the object is new or not and
can call the EntityManagers `persist` method. If it thinks that the entity is not new it calls the EntityManagers
`merge` method.

When it calls the `merge` method than the entity manager (hibernate) will handle the object as `DETACHED` object because
it already has an Id and has no `@Version` annotation. It will then detect that the object has no corresponding row
in the database and will handle it as a `TRANSIENT` entity. When doing this it will copy the object and return the copy
instead of the passed object (`org.hibernate.event.internal.DefaultMergeEventListener`)

It will then call the `PrePersist` eventhandler on the copy and not on the object that has been passed to the `save` 
method. 

But the `org.springframework.data.repository.core.support.EventPublishingRepositoryProxyPostProcessor.EventPublishingMethodInterceptor.invoke`
method will fetch the domain events from the object passed to the save method. But that object has no registered 
domain events and therefor no event listeners are called by Spring.
