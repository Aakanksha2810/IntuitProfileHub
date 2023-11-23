package com.intuit.demo.controller

import com.intuit.demo.exception.BusinessProfileValidationException
import com.intuit.demo.model.response.error.ApiResponse
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
class BusinessProfileController (
    private val businessProfileService: BusinessProfileService
) {
    private val log = LoggerFactory.getLogger(BusinessProfileService::class.java)

    @PostMapping("/validation")
    suspend fun validateBusinessProfile(@RequestBody businessProfileRequest: BusinessProfile):
            ResponseEntity<Any> {
        return try {
            val validationResponse = businessProfileService.validateBusinessProfile(businessProfileRequest)
            val apiResponse = ApiResponse<Any>(
                path = "/business-profile/validation",
                data = validationResponse,
                message = "Profile request successful",
                code = HttpStatus.OK.value()
            )
            ResponseEntity.ok(apiResponse)
        } catch (e: BusinessProfileValidationException) {
            val apiResponse = ApiResponse<Any>(
                path = "/business-profile/validation",
                data = null,
                message = e.message,
                code = HttpStatus.BAD_REQUEST.value()
            )
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse)
        } catch (e: Exception) {
            log.error("Internal Server Error", e)
            val apiResponse = ApiResponse<Any>(
                path = "/business-profile/validation",
                data = null,
                message = "Internal Server Error",
                code = HttpStatus.INTERNAL_SERVER_ERROR.value()
            )
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponse)
        }
    }
}
