package com.intuit.demo.controller

import com.intuit.demo.model.response.ApiResponse
import com.intuit.demo.model.schema.BusinessProfile
import com.intuit.demo.service.BusinessProfileService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.http.MediaType
import org.springframework.validation.annotation.Validated

@RestController
@RequestMapping("/business-profile")
@Validated
class BusinessProfileController(
    private val businessProfileService: BusinessProfileService
) {
    @PostMapping(
        value = ["/validation"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    suspend fun validateBusinessProfile(
        @Valid @RequestBody businessProfileRequest: BusinessProfile): ResponseEntity<ApiResponse<BusinessProfile>> {
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
