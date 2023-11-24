//package com.intuit.demo.controller
//
//import com.intuit.demo.controller.BusinessProfileController
//import com.intuit.demo.exception.BusinessProfileValidationException
//import com.intuit.demo.model.response.error.ApiResponse
//import com.intuit.demo.model.schema.BusinessAddress
//import com.intuit.demo.model.schema.BusinessProfile
//import com.intuit.demo.model.schema.TaxIdentifiers
//import com.intuit.demo.service.BusinessProfileService
//import io.mockk.coEvery
//import io.mockk.mockk
//import kotlinx.coroutines.runBlocking
//import org.junit.jupiter.api.Assertions.assertEquals
//import org.junit.jupiter.api.Assertions.assertNotNull
//import org.junit.jupiter.api.Test
//import org.springframework.http.HttpStatus
//import org.springframework.http.ResponseEntity
//
//class BusinessProfileControllerTest {
//
//    private val businessProfileServiceMock = mockk<BusinessProfileService>()
//    private val businessProfileController = BusinessProfileController(businessProfileServiceMock)
//
//    private val businessAddress = BusinessAddress("123 Main St", "Apt 456", "Cityville", "CA", "12345", "USA")
//    private val taxIdentifiers = TaxIdentifiers("ABCDE1234F", "123456789")
//
//    private val request = BusinessProfile(
//        email = "test@example.com",
//        companyName = "ABC Company",
//        legalName = "ABC Legal",
//        businessAddress = businessAddress,
//        legalAddress = "789 Legal St, Legal City, Legal State, 67890, USA",
//        taxIdentifiers = taxIdentifiers,
//        website = "http://www.example.com"
//    )
//
//    @Test
//    fun `test validateBusinessProfile with successful validation`() = runBlocking {
//
//        coEvery { businessProfileServiceMock.validateBusinessProfile(request) } returns request
//
//        // Act
//        val responseEntity: ResponseEntity<Any> = businessProfileController.validateBusinessProfile(request)
//
//        // Assert
//        assertNotNull(responseEntity)
//        assertEquals(HttpStatus.OK, responseEntity.statusCode)
//
//        val apiResponse = responseEntity.body as ApiResponse<*>
//        assertEquals("/business-profile/validation", apiResponse.path)
//        assertEquals(request, apiResponse.data)
//        assertEquals("Profile request successful", apiResponse.message)
//        assertEquals(HttpStatus.OK.value(), apiResponse.code)
//    }
//
//    @Test
//    fun `test validateBusinessProfile with BusinessProfileValidationException`() = runBlocking {
//        val expectedExceptionMessage = "Validation failed"
//
//        coEvery {
//            businessProfileServiceMock.validateBusinessProfile(request)
//        } throws BusinessProfileValidationException(expectedExceptionMessage)
//
//        // Act
//        val responseEntity: ResponseEntity<Any> = businessProfileController.validateBusinessProfile(request)
//
//        // Assert
//        assertNotNull(responseEntity)
//        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.statusCode)
//
//        val apiResponse = responseEntity.body as ApiResponse<*>
//        assertEquals("/business-profile/validation", apiResponse.path)
//        assertEquals(null, apiResponse.data)
//        assertEquals(expectedExceptionMessage, apiResponse.message)
//        assertEquals(HttpStatus.BAD_REQUEST.value(), apiResponse.code)
//    }
//
//    @Test
//    fun `test validateBusinessProfile with Exception`() = runBlocking {
//        val expectedExceptionMessage = "Internal Server Error"
//
//        coEvery {
//            businessProfileServiceMock.validateBusinessProfile(request)
//        } throws Exception(expectedExceptionMessage)
//
//        // Act
//        val responseEntity: ResponseEntity<Any> = businessProfileController.validateBusinessProfile(request)
//
//        // Assert
//        assertNotNull(responseEntity)
//        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.statusCode)
//
//        val apiResponse = responseEntity.body as ApiResponse<*>
//        assertEquals("/business-profile/validation", apiResponse.path)
//        assertEquals(null, apiResponse.data)
//        assertEquals(expectedExceptionMessage, apiResponse.message)
//        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), apiResponse.code)
//    }
//}
