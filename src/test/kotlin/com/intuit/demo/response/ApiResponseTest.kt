package com.intuit.demo.response

import com.intuit.demo.model.response.ApiResponse
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.util.Date

class ApiResponseTest {

    @Test
    fun `test getters and setters for ApiResponse`() {
        // Given
        val path = "/api/resource"
        val timestamp = Date()
        val data = "Sample data"
        val message = "Success"
        val code = 200

        // When
        val apiResponse = ApiResponse(path, timestamp, data, message, code)

        // Then
        assertThat(apiResponse.path).isEqualTo(path)
        assertThat(apiResponse.timestamp).isEqualTo(timestamp)
        assertThat(apiResponse.data).isEqualTo(data)
        assertThat(apiResponse.message).isEqualTo(message)
        assertThat(apiResponse.code).isEqualTo(code)

    }

}
