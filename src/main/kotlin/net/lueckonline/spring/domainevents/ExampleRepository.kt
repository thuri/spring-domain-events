package net.lueckonline.spring.domainevents

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ExampleRepository : JpaRepository<ExampleEntity, Int>