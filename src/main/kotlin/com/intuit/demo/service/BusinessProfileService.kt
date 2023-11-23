package com.intuit.demo.service

import com.intuit.demo.exception.BusinessProfileValidationException
import com.intuit.demo.exception.HttpResponseException
import com.intuit.demo.exception.NotFoundException
import com.intuit.demo.model.schema.BusinessProfile
import com.intuit.demo.repository.BusinessProfileRepository
import com.intuit.demo.repository.UserSubscriptionRepository
import com.mongodb.MongoException
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.core.FindAndModifyOptions
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.stereotype.Service
import org.springframework.web.client.HttpStatusCodeException
import java.util.*

/**
 * Class to validate the request & Adds the business profile in database
 * @property [businessProfileRepository] for repository call in business profile database
 * @property [userSubscriptionRepository] for repository call in user subscription database
 */
@Service
class BusinessProfileService @Autowired constructor(
    val mongoTemplate: MongoTemplate,
    val businessProfileRepository: BusinessProfileRepository,
    val userSubscriptionRepository: UserSubscriptionRepository,
    val validationClientHystrix: ValidationClientHystrix
) {
    private val log = LoggerFactory.getLogger(BusinessProfileService::class.java)

    /**
     * Adds the business profile in database
     * @param request The business profile request initiated by the user
     */
    private fun createBusinessProfile(request: BusinessProfile): BusinessProfile =
        try {
            mongoTemplate.insert(request)
        } catch (e: MongoException) {
            log.error("Failed to create a business profile: $request, with exception $e")
            throw HttpResponseException("Internal server error")
        }

    /**
     * Find by email ID in the business profile database
     * @param request The business profile request initiated by the user
     */
    private fun findById(request: BusinessProfile): Optional<BusinessProfile> =
        try {
            businessProfileRepository.findById(request.email)
        } catch (e: MongoException) {
            log.error("Failed to find the business profile with exception $e")
            throw HttpResponseException("Internal server error")
        }


    /**
     * Updates the business profile in database
     * @param request The business profile request initiated by the user
     */
    private fun updateBusinessProfile(request: BusinessProfile): BusinessProfile? {
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
            throw HttpResponseException("Internal server error")
        }
    }


    /**
     * Function to validate the request and accordingly add or update the business profile
     * @param request The business profile request initiated by the user
     */
    suspend fun validateBusinessProfile(request: BusinessProfile): BusinessProfile? = coroutineScope {
        /**
         * Fetch all subscribed products of the user from the user subscription database
         */
        try {
            val userSubscriptions = userSubscriptionRepository
                .findById(request.email)
                .orElseThrow { NotFoundException("Profile with email id ${request.email} not found") }
            log.info("user subscription is $userSubscriptions")

            val allRequestsSuccessful = userSubscriptions.subscriptions.map {
                async { return@async validationClientHystrix.validate(request) }
            }
                .awaitAll()
                .all { it }

            if (allRequestsSuccessful && findById(request).isPresent) {
                val updatedProfile = updateBusinessProfile(request)
                log.info("Business profile updated successfully")
                updatedProfile
            } else if (allRequestsSuccessful) {
                val createdProfile = createBusinessProfile(request)
                log.info("Business profile created successfully")
                createdProfile
            } else {
                // Handle error message to the client
                throw BusinessProfileValidationException("Validation failed for the business profile")
            }
        } catch (e: Exception) {
            // Handle other exceptions
            log.error("Error validating business profile", e)
            throw e
        }
    }
}