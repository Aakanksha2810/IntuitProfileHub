package com.intuit.demo.exception

import com.intuit.demo.model.response.ErrorResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(BusinessProfileValidationException::class, NotFoundException::class, ValidationException::class)
    fun handleSpecificException(ex: Exception): ResponseEntity<ErrorResponse> {
        val errorResponse = ErrorResponse(
            statusCode = resolveHttpStatus(ex).value(),
            error = resolveHttpStatus(ex).reasonPhrase,
            message = ex.message ?: "Internal Server Error",
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
            else -> HttpStatus.INTERNAL_SERVER_ERROR
        }
    }
}
