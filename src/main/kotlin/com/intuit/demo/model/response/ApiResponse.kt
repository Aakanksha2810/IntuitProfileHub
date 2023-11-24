package com.intuit.demo.model.response

import java.time.LocalDateTime

data class ApiResponse<T>(
    val path: String,
    val timestamp: LocalDateTime = LocalDateTime.now(),
    val data:T? = null,
    val message: String?,
    val code: Int
)