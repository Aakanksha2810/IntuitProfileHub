package com.intuit.demo.client

import com.intuit.demo.model.schema.BusinessProfile
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.retry.annotation.Backoff
import org.springframework.retry.annotation.Retryable
import org.springframework.web.bind.annotation.PostMapping

@FeignClient(name = "validationClient", url = "\${validation.client.url}")
interface ValidationClient {
    @PostMapping("/validateAPI")
    @Retryable(maxAttempts = 3, backoff = Backoff(delay = 1000))
    fun callValidationApi(request: BusinessProfile): Boolean
}
