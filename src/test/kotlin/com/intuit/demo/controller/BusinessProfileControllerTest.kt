package com.intuit.demo.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.intuit.demo.client.ValidationClient
import com.intuit.demo.client.ValidationCommand
import com.intuit.demo.model.schema.BusinessProfile
import com.intuit.demo.model.schema.BusinessAddress
import com.intuit.demo.model.schema.TaxIdentifiers
import com.intuit.demo.service.BusinessProfileService
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.springframework.http.MediaType
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import java.util.*


@SpringBootTest
@AutoConfigureMockMvc
class BusinessProfileControllerTest {
    private lateinit var businessProfileController: BusinessProfileController
    private val businessProfileService: BusinessProfileService = mockk()

    private lateinit var mockMvc: MockMvc
    @BeforeEach
    fun setUp() {
        businessProfileController = BusinessProfileController(businessProfileService)
        mockMvc = MockMvcBuilders.standaloneSetup(businessProfileController).build()
    }

    @AfterEach
    fun tearDown() {
        clearAllMocks()
    }

    private val businessAddress = BusinessAddress("123 Main St", "Apt 456", "Cityville", "CA", "12345", "USA")
    private val taxIdentifiers = TaxIdentifiers("ABCDE1234F", "123456789")

    private val businessProfileRequest = BusinessProfile(
        email = "test@example.com",
        companyName = "ABC Company",
        legalName = "ABC Legal",
        businessAddress = businessAddress,
        legalAddress = "789 Legal St, Legal City, Legal State, 67890, USA",
        taxIdentifiers = taxIdentifiers,
        website = "http://www.example.com"
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `validateBusinessProfile should return OK response with ApiResponse`() = runBlockingTest {
        coEvery {
            businessProfileService.validateBusinessProfile(businessProfileRequest) } returns businessProfileRequest
        val objectMapper = ObjectMapper()

        // Act
        val result = mockMvc.perform(
            post("/business-profile/validation")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .characterEncoding("UTF-8") // Set the character encoding
                .content(objectMapper.writeValueAsString(businessProfileRequest))
            ).andExpect(status().isOk)
//            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andReturn()


        assertEquals(200, result.response.status)

        val contentType = result.response.contentType
        println("Content Type: $contentType")
//        // Print the request content
//        val requestContent = result.request.contentAsString
//        println("Request Content: $requestContent")
//        coVerify {
//            businessProfileService.validateBusinessProfile(businessProfileRequest)
//        }
//        // Check HTTP status code
//        val status = result.response.status
//        println("HTTP Status Code: $status")
//        // Print the result.response object
//        println("result.response: ${result.response}")
//        val responseBody = result.response.contentAsString
//        assertNotNull(responseBody, "Response body is null or empty")
        // Print or log the response body
//        println("Response Body: $responseBody")
//        val parsedResponse: ResponseEntity<ApiResponse<BusinessProfile>> = objectMapper.readValue(responseBody)
//        println("parsedResponse $parsedResponse")
//        // Assert the content of the parsed response
//        assertEquals("/business-profile/validation", parsedResponse.)
//        assertEquals("Profile request successful", parsedResponse.message)
//        assertEquals(200, parsedResponse.code)
//        assertEquals("test@example.com", parsedResponse.data?.email)
//        assertEquals("ABC Company", parsedResponse.data?.companyName)
    }
}
