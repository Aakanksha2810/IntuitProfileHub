package com.intuit.demo.model.response.error

import com.intuit.demo.model.schema.BusinessProfile
import java.time.LocalDateTime

data class ApiResponse<T>(
    val path: String,
    val timestamp: LocalDateTime = LocalDateTime.now(),
    val data: BusinessProfile?,
    val message: String?,
    val code: Int
)
