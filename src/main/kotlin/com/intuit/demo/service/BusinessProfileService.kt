package com.intuit.demo.service

import com.intuit.demo.exception.DatabaseException
import com.intuit.demo.exception.BusinessProfileValidationException
import com.intuit.demo.exception.NotFoundException
import com.intuit.demo.exception.ValidationException
import com.intuit.demo.model.schema.BusinessProfile
import com.intuit.demo.repository.BusinessProfileRepository
import com.intuit.demo.repository.BusinessProfileTemplateRepository
import com.intuit.demo.repository.UserSubscriptionRepository
import com.mongodb.MongoException
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.Optional

/**
 * Class to validate the request & Adds the business profile in database
 * @property [businessProfileRepository] for repository call in business profile database
 * @property [userSubscriptionRepository] for repository call in user subscription database
 */
@Service
class BusinessProfileService @Autowired constructor(
    val businessProfileTemplateRepository: BusinessProfileTemplateRepository,
    val businessProfileRepository: BusinessProfileRepository,
    val userSubscriptionRepository: UserSubscriptionRepository,
    val validationClientHystrix: ValidationClientHystrix
) {
    private val log = LoggerFactory.getLogger(BusinessProfileService::class.java)

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
            log.info("User subscriptions: $userSubscriptions")

            val allRequestsSuccessful = userSubscriptions.subscriptions.map {
                async { return@async validationClientHystrix.validate(request) }
            }
                .awaitAll()
                .all { it }

            when {
                allRequestsSuccessful && findById(request).isPresent -> {
                    val updatedProfile = businessProfileTemplateRepository.updateBusinessProfile(request)
                    log.info("Business profile updated successfully")
                    updatedProfile
                }
                allRequestsSuccessful -> {
                    val createdProfile = businessProfileTemplateRepository.createBusinessProfile(request)
                    log.info("Business profile created successfully")
                    createdProfile
                }
                else -> {
                    /**
                     * Handle error message to the client
                     */
                    throw ValidationException("Validation failed for the business profile")
                }
            }
        } catch (e: BusinessProfileValidationException) {
            /**
             * Rethrow custom exceptions
             */
            throw e
        } catch (e: Exception) {
            /**
             * Handle other exceptions
             */
            log.error("Error validating business profile", e)
            throw Exception("Error validating business profile", e)
        }
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
            throw DatabaseException("Internal server error")
        }
}
