package com.intuit.demo.exception

import com.intuit.demo.model.response.ErrorResponse
import com.mongodb.MongoException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import java.time.Instant

@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(BusinessProfileValidationException::class, NotFoundException::class, ValidationException::class)
    fun handleSpecificException(ex: Exception): ResponseEntity<ErrorResponse> {
        val errorResponse = ErrorResponse(
            status = resolveHttpStatus(ex),
            error = resolveHttpStatus(ex).reasonPhrase,
            message = ex.message ?: "Internal Server Error",
            timestamp = Instant.now()
        )
        return ResponseEntity(errorResponse, resolveHttpStatus(ex))
    }

    @ExceptionHandler(Exception::class)
    fun handleGenericException(ex: Exception): ResponseEntity<ErrorResponse> {
        return handleSpecificException(ex)
    }

    private fun resolveHttpStatus(ex: Exception): HttpStatus {
        return when (ex) {
            is NotFoundException -> HttpStatus.NOT_FOUND
            is ValidationException -> HttpStatus.BAD_REQUEST
            is DatabaseException -> HttpStatus.INTERNAL_SERVER_ERROR
            is BusinessProfileValidationException -> HttpStatus.INTERNAL_SERVER_ERROR
            else -> HttpStatus.INTERNAL_SERVER_ERROR
        }
    }
}
