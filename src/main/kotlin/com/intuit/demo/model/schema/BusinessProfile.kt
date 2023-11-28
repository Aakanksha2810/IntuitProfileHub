package com.intuit.demo.model.schema

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Pattern
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "business_profile")
data class BusinessProfile(
        @Id
        @field:Email(message = "Invalid email format")
        val email: String,
        @Indexed
        var companyName: String,
        @Indexed
        var legalName: String,
        var businessAddress: BusinessAddress,
        var legalAddress: String,
        val taxIdentifiers: TaxIdentifiers,
        @field:Pattern(regexp = "^(http://www\\.|https://www\\.|http://|https://)?[a-z0-9]+([\\-\\.]{1}[a-z0-9]+)" +
                "*\\.[a-z]{2,6}(:[0-9]{1,5})?(\\/.*)?\$", message = "Invalid website format")
        var website: String
)

data class BusinessAddress(
        var line1: String,
        var line2: String?,
        var city: String,
        var state: String,
        var zip: String,
        val country: String
)

data class TaxIdentifiers(
        val pan: String,
        val ein: String
)
