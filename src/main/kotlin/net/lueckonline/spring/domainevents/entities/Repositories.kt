package net.lueckonline.spring.domainevents.entities

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface GeneratedIdEntityRepository : JpaRepository<GeneratedIdEntity, Int>

@Repository
interface BadEntityRepository : JpaRepository<BadEntity, Int>

@Repository
interface AssignedIdWorkaroundEntityRepository : JpaRepository<AssigendIdWorkaroundEntity, Int>

@Repository
interface AssignedIdWithVersionEntityRepository : JpaRepository<AssignedIdWithVersionEntity, Int>