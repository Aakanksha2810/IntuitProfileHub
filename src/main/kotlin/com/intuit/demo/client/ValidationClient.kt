package com.intuit.demo.client

import com.intuit.demo.model.schema.BusinessProfile
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping

@FeignClient(name = "validationClient", url = "\${validation.client.url}")
interface ValidationClient {
    @PostMapping("/validateAPI")
    fun callValidationApi(request: BusinessProfile): Boolean
}
