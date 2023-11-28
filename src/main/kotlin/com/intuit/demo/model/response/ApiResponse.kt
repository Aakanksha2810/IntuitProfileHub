package com.intuit.demo.model.response

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import java.util.Date

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("path", "timestamp", "data", "message", "code")
data class ApiResponse<T>(
    val path: String,
    val timestamp: Date = Date(),
    val data:T? = null,
    val message: String?,
    val code: Int
)