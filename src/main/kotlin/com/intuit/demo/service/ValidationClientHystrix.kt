package com.intuit.demo.service

import com.intuit.demo.client.ValidationClient
import com.intuit.demo.model.schema.BusinessProfile
import com.netflix.hystrix.HystrixCommand
import com.netflix.hystrix.HystrixCommandGroupKey
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ValidationClientHystrix(
    @Autowired
    private val validationClient: ValidationClient
) {
    private val log = LoggerFactory.getLogger(ValidationClientHystrix::class.java)

    fun validate(request: BusinessProfile): Boolean {
        return ValidationCommand(validationClient, request).execute()
    }

    private inner class ValidationCommand(
        private val validationClient: ValidationClient,
        private val request: BusinessProfile
    ) : HystrixCommand<Boolean>(
        HystrixCommandGroupKey.Factory.asKey("ValidationGroup")
    ) {

        override fun run(): Boolean {
            /**
             * Actual validation logic using the external client
             */
            return validationClient.callValidationApi(request)
        }

         override fun getFallback(): Boolean {
            /**
             * Fallback logic in case of failure
             */
            log.error("Fallback: Validation API call failed")
            return false
        }
    }
}