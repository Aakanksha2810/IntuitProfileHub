package com.intuit.demo.model.response

import java.time.Instant

data class ErrorResponse(
    val statusCode: Int,
    val error: String,
    val message: String,
    val timestamp: Instant = Instant.now(),
)
