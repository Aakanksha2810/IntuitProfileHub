package com.intuit.demo.exception

import com.intuit.demo.model.response.ErrorResponse
import com.intuit.demo.model.schema.BusinessAddress
import com.intuit.demo.model.schema.BusinessProfile
import com.intuit.demo.model.schema.TaxIdentifiers
import com.intuit.demo.service.BusinessProfileService
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.http.HttpStatus
import java.time.Instant
class GlobalExceptionHandlerTest {

    private val globalExceptionHandler = GlobalExceptionHandler()
    private val businessProfileService = mockk<BusinessProfileService>()

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

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `handleSpecificException should return ErrorResponse for ValidationException`() = runBlockingTest {
        coEvery {
            businessProfileService.validateBusinessProfile(request)
        } throws ValidationException("Validation failed for the business profile")

        val exception = assertThrows<BusinessProfileValidationException> {
            businessProfileService.validateBusinessProfile(request)
        }
        val responseEntity = globalExceptionHandler.handleSpecificException(exception)

        val expectedErrorResponse = ErrorResponse(
            statusCode = HttpStatus.BAD_REQUEST.value(),
            error = HttpStatus.BAD_REQUEST.reasonPhrase,
            message = "Validation failed for the business profile",
        )
        assertEquals(expectedErrorResponse.statusCode, responseEntity.statusCode.value())
        assertEquals(expectedErrorResponse.error, responseEntity.body?.error)
        assertEquals(expectedErrorResponse.message, responseEntity.body?.message)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `handleSpecificException should return ErrorResponse with correct status code & message for NotFoundException`() =
        runBlockingTest {
            coEvery {
                businessProfileService.validateBusinessProfile(request)
            } throws NotFoundException("Profile with email id ${request.email} not found")

            val exception = assertThrows<NotFoundException> {
                businessProfileService.validateBusinessProfile(request)
            }
            val responseEntity = globalExceptionHandler.handleSpecificException(exception)

            val expectedErrorResponse = ErrorResponse(
                statusCode = HttpStatus.NOT_FOUND.value(),
                error = HttpStatus.NOT_FOUND.reasonPhrase,
                message = "Profile with email id test@example.com not found",
            )
            assertEquals(expectedErrorResponse.statusCode, responseEntity.statusCode.value())
            assertEquals(expectedErrorResponse.error, responseEntity.body?.error)
            assertEquals(expectedErrorResponse.message, responseEntity.body?.message)
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `handleSpecificException should return ErrorResponse with correct status code & message for DatabaseException`() =
        runBlockingTest {
            coEvery {
                businessProfileService.validateBusinessProfile(request)
            } throws DatabaseException("Internal Server Error")

            val exception = assertThrows<BusinessProfileValidationException> {
                businessProfileService.validateBusinessProfile(request)
            }
            val responseEntity = globalExceptionHandler.handleSpecificException(exception)

            val expectedErrorResponse = ErrorResponse(
                statusCode = HttpStatus.INTERNAL_SERVER_ERROR.value(),
                error = HttpStatus.INTERNAL_SERVER_ERROR.reasonPhrase,
                message = "Internal Server Error",
            )
            assertEquals(expectedErrorResponse.statusCode, responseEntity.statusCode.value())
            assertEquals(expectedErrorResponse.error, responseEntity.body?.error)
            assertEquals(expectedErrorResponse.message, responseEntity.body?.message)
        }

    @Test
    fun `handleGenericException should return ErrorResponse with correct status code and message`() {
        val exceptionMessage = "Something went wrong"
        val exception = RuntimeException(exceptionMessage)

        val actual = globalExceptionHandler.handleGenericException(exception)

        val expected = ErrorResponse(
            statusCode = HttpStatus.INTERNAL_SERVER_ERROR.value(),
            error = HttpStatus.INTERNAL_SERVER_ERROR.reasonPhrase,
            message = exceptionMessage,
            timestamp = Instant.now()
        )

        // Verify that the expected ErrorResponse is returned
        assert(expected.statusCode == actual.statusCode.value())
        assert(expected.error == actual.body?.error)
        assert(expected.message == actual.body?.message)
    }
}
