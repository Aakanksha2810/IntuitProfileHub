package com.intuit.demo.repository

import com.intuit.demo.exception.DatabaseException
import com.intuit.demo.model.schema.BusinessProfile
import com.intuit.demo.service.BusinessProfileService
import com.mongodb.MongoException
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.core.FindAndModifyOptions
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.stereotype.Repository

@Repository
class BusinessProfileTemplateRepository
    @Autowired constructor(private val mongoTemplate: MongoTemplate ) {
    private val log = LoggerFactory.getLogger(BusinessProfileService::class.java)

    /**
     * Adds the business profile in database
     * @param request The business profile request initiated by the user
     */
     fun createBusinessProfile(request: BusinessProfile): BusinessProfile =
        try {
            mongoTemplate.insert(request)
        } catch (e: MongoException) {
            log.error("Failed to create a business profile: $request, with exception $e")
            throw DatabaseException("Internal server error")
        }

    /**
     * Updates the business profile in database
     * @param request The business profile request initiated by the user
     */
     fun updateBusinessProfile(request: BusinessProfile): BusinessProfile? {
        try {
            //builder design pattern
            val update = Update()
                .set("companyName", request.companyName)
                .set("legalName", request.legalName)
                .set("legalAddress", request.legalAddress)
                .set("businessAddress", request.businessAddress)
                .set("taxIdentifiers", request.taxIdentifiers)
                .set("website", request.website)
            return mongoTemplate.findAndModify(
                Query.query(Criteria.where("email").`is`(request.email)),
                update,
                FindAndModifyOptions.options().returnNew(true),
                BusinessProfile::class.java
            )
        } catch (e: MongoException) {
            log.error("Failed to update business profile: $request of email id ${request.email} with exception: $e")
            throw DatabaseException("Internal server error")
        }
    }

}