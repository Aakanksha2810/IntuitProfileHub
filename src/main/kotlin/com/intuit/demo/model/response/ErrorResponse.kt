package com.intuit.demo.model.response

import org.springframework.http.HttpStatus
import java.time.Instant

data class ErrorResponse(
    val status: HttpStatus,
    val error: String,
    val message: String,
    val timestamp: Instant
)
