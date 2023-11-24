package com.intuit.demo.service

import com.intuit.demo.client.ValidationCommandFactory
import com.intuit.demo.model.schema.BusinessProfile
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ValidationClientHystrix @Autowired constructor(
    private val validationCommandFactory: ValidationCommandFactory
) {
    fun validate(request: BusinessProfile): Boolean {
        return validationCommandFactory.createValidationCommand(request).execute()
    }
}

