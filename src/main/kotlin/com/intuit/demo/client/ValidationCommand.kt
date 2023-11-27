package com.intuit.demo.client

import com.intuit.demo.model.schema.BusinessProfile
import com.intuit.demo.util.RETRY_ATTEMPT
import com.intuit.demo.util.RETRY_INITIAL_INTERVAL
import com.intuit.demo.util.RETRY_MAX_NTERVAL
import com.intuit.demo.util.RETRY_MULTIPLIER
import com.netflix.hystrix.HystrixCommand
import com.netflix.hystrix.HystrixCommandGroupKey
import org.slf4j.LoggerFactory
import org.springframework.retry.backoff.ExponentialBackOffPolicy
import org.springframework.retry.policy.SimpleRetryPolicy
import org.springframework.retry.support.RetryTemplate

class ValidationCommand(
    private val validationClient: ValidationClient,
    private val request: BusinessProfile
) : HystrixCommand<Boolean>(HystrixCommandGroupKey.Factory.asKey("ValidationGroup")), BusinessProfileCommand {

    private val log = LoggerFactory.getLogger(ValidationCommand::class.java)

    override fun run(): Boolean {

        /**
         * Actual validation logic using the external client
         */
        val retryTemplate = createRetryTemplate()
        return try {
            retryTemplate.execute<Boolean, Exception> {
                log.info("Executing method that may need retry")
                validationClient.callValidationApi(request)
            }
        } catch (e: Exception) {
            /**
             * Re-throw exceptions for Hystrix fallback
             */
            log.error("Validation API call failed after retries: ${e.message}")
            throw e
        }
    }

    private fun createRetryTemplate(): RetryTemplate {
        val retryTemplate = RetryTemplate()
        val backOffPolicy = ExponentialBackOffPolicy()
        backOffPolicy.initialInterval = RETRY_INITIAL_INTERVAL
        backOffPolicy.maxInterval = RETRY_MAX_NTERVAL
        backOffPolicy.multiplier = RETRY_MULTIPLIER
        retryTemplate.setBackOffPolicy(backOffPolicy)
        retryTemplate.setRetryPolicy(SimpleRetryPolicy(RETRY_ATTEMPT))
        return retryTemplate
    }

    override fun getFallback(): Boolean {
        /**
         * Fallback logic in case of failure
         */
        log.error("Fallback: Validation API call failed after 3 retries")
        return false
    }
}

interface BusinessProfileCommand {
    fun execute(): Boolean
    fun getFallback(): Boolean
}
