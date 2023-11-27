package com.intuit.demo.model.schema

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "business_profile")
data class BusinessProfile(
        @Id
        val email: String,
        @Indexed
        var companyName: String,
        @Indexed
        var legalName: String,
        var businessAddress: BusinessAddress,
        var legalAddress: String,
        val taxIdentifiers: TaxIdentifiers,
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
