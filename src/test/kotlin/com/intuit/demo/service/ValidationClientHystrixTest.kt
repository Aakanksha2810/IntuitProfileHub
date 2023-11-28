package com.intuit.demo.service

import com.intuit.demo.client.ValidationCommand
import com.intuit.demo.client.ValidationCommandFactory
import com.intuit.demo.model.schema.BusinessProfile
import com.intuit.demo.model.schema.TaxIdentifiers
import com.intuit.demo.model.schema.BusinessAddress
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ValidationClientHystrixTest {

    private lateinit var validationClientHystrix: ValidationClientHystrix
    private val validationCommand = mockk<ValidationCommand>()
    private val validationCommandFactory = mockk<ValidationCommandFactory>()

    @BeforeEach
    fun setUp() {
        validationClientHystrix = ValidationClientHystrix(validationCommandFactory)
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
    fun `validate should return true when validation command executes successfully`() {
        every { validationCommandFactory.createValidationCommand(request) } returns validationCommand
        every { validationCommand.execute() } returns true

        // Act
        val result = validationClientHystrix.validate(request)

        // Assert
        Assertions.assertTrue(result)
    }

    @Test
    fun `validate should return false when validation command fails`() {
        every { validationCommandFactory.createValidationCommand(request) } returns validationCommand
        every { validationCommand.execute() } returns false

        // Act
        val result = validationClientHystrix.validate(request)

        // Assert
        Assertions.assertFalse(result)
    }
}
