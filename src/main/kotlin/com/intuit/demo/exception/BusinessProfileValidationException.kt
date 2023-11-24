package com.intuit.demo.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.BAD_REQUEST)
sealed class BusinessProfileValidationException(message: String, cause: Throwable? = null) :
    RuntimeException(message, cause)

class NotFoundException(message: String) : BusinessProfileValidationException(message)
class ValidationException(message: String) : BusinessProfileValidationException(message)
class DatabaseException(message: String, cause: Throwable? = null) : BusinessProfileValidationException(message, cause)
