package com.intuit.demo.client

import com.intuit.demo.model.schema.BusinessAddress
import com.intuit.demo.model.schema.BusinessProfile
import com.intuit.demo.model.schema.TaxIdentifiers
import io.mockk.clearAllMocks
import io.mockk.verify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ValidationCommandTest {
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
    private lateinit var validationCommand: ValidationCommand

    private val validationClient: ValidationClient = mockk()

    @BeforeEach
    fun setUp() {
        validationCommand = ValidationCommand(validationClient, request)
    }

    @AfterEach
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `execute should return true when validationClient call succeeds`() {
        every { validationClient.callValidationApi(request) } returns true

        // Act
        val result = runBlocking { validationCommand.execute() }

        // Assert
        assertEquals(true, result)
        verify(exactly = 1) { validationClient.callValidationApi(request) }
    }

    @Test
    fun `execute should return false after max retries when validationClient call fails`() {

        every { validationClient.callValidationApi(request) } throws Exception("Simulated failure")

        // Act
        val result = runBlocking { validationCommand.execute() }

        // Assert
        assertEquals(false, result)
        verify(exactly = 3) { validationClient.callValidationApi(request) }
    }

}
