package com.intuit.demo.repository

import org.slf4j.Logger
import com.intuit.demo.exception.DatabaseException
import com.intuit.demo.model.schema.BusinessAddress
import com.intuit.demo.model.schema.BusinessProfile
import com.intuit.demo.model.schema.TaxIdentifiers
import com.mongodb.MongoException
import io.mockk.unmockkAll
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.springframework.dao.DataAccessException
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.test.util.ReflectionTestUtils

class BusinessProfileTemplateRepositoryTest {

    @MockK
    private lateinit var mongoTemplate: MongoTemplate

    private lateinit var repository: BusinessProfileTemplateRepository

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        repository = BusinessProfileTemplateRepository(mongoTemplate)
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    private val businessAddress = BusinessAddress("123 Main St", "Apt 456", "Cityville", "CA", "12345", "USA")
    private val taxIdentifiers = TaxIdentifiers("ABCDE1234F", "123456789")

    private val businessProfile = BusinessProfile(
        email = "test@example.com",
        companyName = "ABC Company",
        legalName = "ABC Legal",
        businessAddress = businessAddress,
        legalAddress = "789 Legal St, Legal City, Legal State, 67890, USA",
        taxIdentifiers = taxIdentifiers,
        website = "http://www.example.com"
    )

    @Test
    fun `createBusinessProfile should insert business profile and return it`() {
        // Arrange
        every { mongoTemplate.insert(businessProfile) } returns businessProfile

        // Act
        val result = repository.createBusinessProfile(businessProfile)

        // Assert
        assertEquals(businessProfile, result)
        verify(exactly = 1) { mongoTemplate.insert(businessProfile) }
    }

    @Test
    fun `createBusinessProfile should throw DatabaseException when insert fails`() {
        // Arrange
        every { mongoTemplate.insert(businessProfile) } throws mockk<MongoException>()

        // Act & Assert
        assertThrows(DatabaseException::class.java) {
            repository.createBusinessProfile(businessProfile)
        }

        verify(exactly = 1) { mongoTemplate.insert(businessProfile) }
    }

    @Test
    fun `updateBusinessProfile should update business profile and return it`() {
        // Arrange
        val querySlot = slot<Query>()
        val updateSlot = slot<Update>()
        every {
            mongoTemplate.findAndModify(
                capture(querySlot),
                capture(updateSlot),
                any(),
                BusinessProfile::class.java
            )
        } returns businessProfile

        // Act
        val result = repository.updateBusinessProfile(businessProfile)

        // Assert
        assertEquals(businessProfile, result)
        verify(exactly = 1) {
            mongoTemplate.findAndModify(
                any(),
                any(),
                any(),
                BusinessProfile::class.java
            )
        }
        // You can add more specific assertions on the captured query and update if needed.
    }

    @Test
    fun `updateBusinessProfile should throw DatabaseException when update fails`() {
        // Arrange
        every {
            mongoTemplate.findAndModify(
                any(),
                any(),
                any(),
                BusinessProfile::class.java
            )
        } throws mockk<MongoException>()

        // Act & Assert
        assertThrows(DatabaseException::class.java) {
            repository.updateBusinessProfile(businessProfile)
        }

        verify(exactly = 1) {
            mongoTemplate.findAndModify(
                any(),
                any(),
                any(),
                BusinessProfile::class.java
            )
        }
    }


    @Test
    fun `test recoverAfterMaxRetries`() {
        // Arrange
        val dataAccessException = mock(DataAccessException::class.java)

        // Mock the logger to capture log events
        val logger = mock(Logger::class.java)
        Mockito.`when`(logger.error(anyString())).then { invocation ->
            println("Logging: ${invocation.arguments[0]}")
            null
        }
        ReflectionTestUtils.setField(repository, "log", logger)

        // Act and Assert
        assertThrows<DatabaseException> {
            repository.recoverAfterMaxRetries(dataAccessException, businessProfile)
        }
    }
}
