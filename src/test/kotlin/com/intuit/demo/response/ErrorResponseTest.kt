package com.intuit.demo.response

import com.intuit.demo.model.response.ErrorResponse
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.Duration
import java.time.Instant

class ErrorResponseTest {

    @Test
    fun `timestamp should be set to the current time by default`() {
        // Given
        val errorResponse = ErrorResponse(
            statusCode = 500,
            error = "Internal Server Error",
            message = "Something went wrong"
        )

        // When
        val currentTime = Instant.now()
        val timestampDifference = Duration.between(currentTime, errorResponse.timestamp).abs()

        // Then
        assertThat(timestampDifference).isLessThan(Duration.ofSeconds(1)) // Adjust the threshold if necessary
    }

    @Test
    fun `timestamp should be set to a specific time`() {
        // Given
        val specificTime = Instant.parse("2023-01-01T00:00:00Z")

        // When
        val errorResponse = ErrorResponse(
            statusCode = 404,
            error = "Not Found",
            message = "Resource not found",
            timestamp = specificTime
        )

        // Then
        assertThat(errorResponse.timestamp).isEqualTo(specificTime)
    }
}
