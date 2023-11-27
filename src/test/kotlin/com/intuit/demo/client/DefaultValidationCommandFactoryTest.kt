package com.intuit.demo.client

import com.intuit.demo.model.schema.BusinessAddress
import com.intuit.demo.model.schema.BusinessProfile
import com.intuit.demo.model.schema.TaxIdentifiers
import io.mockk.clearAllMocks
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class DefaultValidationCommandFactoryTest {
    private lateinit var defaultValidationCommandFactory: DefaultValidationCommandFactory

    private val validationClient: ValidationClient = mockk()

    @BeforeEach
    fun setUp() {
        defaultValidationCommandFactory = DefaultValidationCommandFactory(validationClient)
    }

    @AfterEach
    fun tearDown() {
        clearAllMocks()
    }

    private val businessAddress = BusinessAddress("123 Main St", "Apt 456", "Cityville", "CA", "12345", "USA")
    private val taxIdentifiers = TaxIdentifiers("ABCDE1234F", "123456789")

    private val request = BusinessProfile(
        email = "test@example.com",
        companyName = "ABC Company",
        legalName = "ABC Legal",
        businessAddress = businessAddress,
        legalAddress = "789 Legal St, Legal City, Legal State, 67890, USA",
        taxIdentifiers = taxIdentifiers,
        website = "http://www.example.com"
    )

    @Test
    fun `createValidationCommand should return ValidationCommand with correct dependencies`() {
        // Arrange
        val validationClient = mockk<ValidationClient>()
        val factory = DefaultValidationCommandFactory(validationClient)

        // Act
        val command = factory.createValidationCommand(request)

        // Assert
        assertTrue(command is ValidationCommand)
    }

}