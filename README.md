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

The test works for the later two because Spring Data knows whether the object is new or not and doesn't need
to create a completely different object when the `save` method is called on the repositories.

Because in case a new object is created the `PrePersist` handler is called on the object that is added to persistence
context which is not the object that has been passed to the `save` method. But the domain events are only evaluated on
the object that has been passed to the `save` method not the one that has been passed to `save`

I'm not quite sure when the `save` method will return another object than the passed one and that the domain events
will always be executed on the correct object in all other circumstances but at least the Id generation and implementing 
`Persistable` are a way in the wanted direction.
