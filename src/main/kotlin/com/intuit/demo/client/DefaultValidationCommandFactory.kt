package com.intuit.demo.client

import com.intuit.demo.model.schema.BusinessProfile
import org.springframework.stereotype.Component

@Component
class DefaultValidationCommandFactory(
    private val validationClient: ValidationClient
) : ValidationCommandFactory {
    override fun createValidationCommand(request: BusinessProfile): BusinessProfileCommand {
        return ValidationCommand(validationClient, request)
    }
}
