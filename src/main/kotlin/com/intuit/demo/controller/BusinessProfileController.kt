package com.intuit.demo.controller

import com.intuit.demo.model.response.ApiResponse
import com.intuit.demo.model.schema.BusinessProfile
import com.intuit.demo.service.BusinessProfileService
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/business-profile")
class BusinessProfileController(
    private val businessProfileService: BusinessProfileService
) {
    private val log = LoggerFactory.getLogger(BusinessProfileService::class.java)

    @PostMapping("/validation")
    suspend fun validateBusinessProfile(
        @RequestBody businessProfileRequest: BusinessProfile): ResponseEntity<Any> {
        val validationResponse = businessProfileService.validateBusinessProfile(businessProfileRequest)
        val apiResponse = ApiResponse(
            path = "/business-profile/validation",
            data = validationResponse,
            message = "Profile request successful",
            code = HttpStatus.OK.value()
        )
        return ResponseEntity.ok(apiResponse)
    }
}
