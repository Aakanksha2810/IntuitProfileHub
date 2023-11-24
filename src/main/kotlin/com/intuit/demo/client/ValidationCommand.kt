package com.intuit.demo.client

import com.intuit.demo.model.schema.BusinessProfile
import com.netflix.hystrix.HystrixCommand
import com.netflix.hystrix.HystrixCommandGroupKey
import org.slf4j.LoggerFactory
import org.springframework.retry.annotation.Backoff
import org.springframework.retry.annotation.Retryable
import org.springframework.retry.backoff.ExponentialBackOffPolicy
import org.springframework.retry.policy.SimpleRetryPolicy
import org.springframework.retry.support.RetryTemplate

class ValidationCommand(
    private val validationClient: ValidationClient,
    private val request: BusinessProfile
) : HystrixCommand<Boolean>(
    HystrixCommandGroupKey.Factory.asKey("ValidationGroup")
), BusinessProfileCommand {
    private val log = LoggerFactory.getLogger(ValidationCommand::class.java)
    private var retryCount = 0

    override fun run(): Boolean {
        /**
         * Actual validation logic using the external client
         */
        val retryTemplate = RetryTemplate()
        val backOffPolicy = ExponentialBackOffPolicy()
//        backOffPolicy.initialInterval = 1000 // Initial interval in milliseconds
//        backOffPolicy.multiplier = 2.0 // Multiplier for each subsequent interval
//        backOffPolicy.maxInterval = 5000 // Maximum interval in milliseconds

        retryTemplate.setBackOffPolicy(backOffPolicy)
        retryTemplate.setRetryPolicy(SimpleRetryPolicy(3))
        return try {
            retryTemplate.execute<Boolean, Exception> {
                log.info("Executing method that may need retry")
                validationClient.callValidationApi(request)
            }
        } catch (e: Exception) {
            log.error("Validation API call failed after retries: ${e.message}")
            throw e // Re-throw exceptions for Hystrix fallback
        }
    }

    override fun getFallback(): Boolean {
        /**
         * Fallback logic in case of failure
         */
//        println("retry count is $retryCount")
//        if (retryCount == 3) {
            log.error("Fallback: Validation API call failed after 3 retries")
            return false
//        }
//        return false
    }
}

interface BusinessProfileCommand {
    fun execute(): Boolean
    fun getFallback(): Boolean
}
