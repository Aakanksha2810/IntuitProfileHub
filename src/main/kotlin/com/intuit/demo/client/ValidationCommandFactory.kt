package com.intuit.demo.client

import com.intuit.demo.model.schema.BusinessProfile

interface ValidationCommandFactory {
    fun createValidationCommand(request: BusinessProfile): BusinessProfileCommand
}
