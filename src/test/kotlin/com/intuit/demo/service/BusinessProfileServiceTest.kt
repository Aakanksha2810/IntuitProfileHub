package com.intuit.demo.service

import com.intuit.demo.exception.DatabaseException
import com.intuit.demo.exception.NotFoundException
import com.intuit.demo.exception.ValidationException
import com.intuit.demo.model.schema.BusinessProfile
import com.intuit.demo.model.schema.TaxIdentifiers
import com.intuit.demo.model.schema.BusinessAddress
import com.intuit.demo.model.schema.Subscriptions
import com.intuit.demo.model.schema.UserSubscription
import com.intuit.demo.repository.BusinessProfileRepository
import com.intuit.demo.repository.BusinessProfileTemplateRepository
import com.intuit.demo.repository.UserSubscriptionRepository
import com.mongodb.MongoException
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import java.util.*

@ExtendWith(MockKExtension::class)
class BusinessProfileServiceTest {
    private lateinit var businessProfileService: BusinessProfileService

    private val businessProfileTemplateRepository: BusinessProfileTemplateRepository = mockk()
    private val businessProfileRepository: BusinessProfileRepository = mockk()
    private val userSubscriptionRepository: UserSubscriptionRepository = mockk()
    private val validationClientHystrix: ValidationClientHystrix = mockk()

    @BeforeEach
    fun setUp() {
        businessProfileService = BusinessProfileService(
            businessProfileTemplateRepository,
            businessProfileRepository,
            userSubscriptionRepository,
            validationClientHystrix
        )
    }

    @AfterEach
    fun tearDown() {
        clearAllMocks()
    }

    private val businessAddress = BusinessAddress("123 Main St", "Apt 456", "Cityville", "CA", "12345", "USA")
    private val taxIdentifiers = TaxIdentifiers("ABCDE1234F", "123456789")

    private val request = BusinessProfile(
        email = "test@example.com",
        companyName = "ABC Company",
        legalName = "ABC Legal",
        businessAddress = businessAddress,
        legalAddress = "789 Legal St, Legal City, Legal State, 67890, USA",
        taxIdentifiers = taxIdentifiers,
        website = "http://www.example.com"
    )

    private val userSubscription = UserSubscription("trp@gmail.com", listOf(Subscriptions("","")))

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `validateBusinessProfile should create a new profile when validation is successful and profile not found`() =
        runBlockingTest {
        // Arrange
        every { userSubscriptionRepository.findById(request.email) } returns Optional.of(userSubscription)
        every { validationClientHystrix.validate(request) } returns true
        every { businessProfileRepository.findById(request.email) } returns Optional.empty()
        every { businessProfileTemplateRepository.createBusinessProfile(request) } returns request

        // Act
        val result = businessProfileService.validateBusinessProfile(request)

        // Assert
        assert(result == request)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `validateBusinessProfile should update profile when validation is successful and profile found`() =
        runBlockingTest {
        every { userSubscriptionRepository.findById(request.email) } returns Optional.of(userSubscription)
        every { validationClientHystrix.validate(request) } returns true
        every { businessProfileRepository.findById(request.email) } returns Optional.of(request)
        every { businessProfileTemplateRepository.updateBusinessProfile(request) } returns request

        // Act
        val result = businessProfileService.validateBusinessProfile(request)

        // Assert
        assert(result == request)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `validateBusinessProfile should throw ValidationException when validation fails`() = runBlockingTest {
        // Arrange
        every { userSubscriptionRepository.findById(request.email) } returns Optional.of(userSubscription)
        every { validationClientHystrix.validate(request) } returns false

        // Act and Assert
        assertThrows<ValidationException> {
            businessProfileService.validateBusinessProfile(request)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `validateBusinessProfile should throw NotFoundException when user subscriptions not found`() = runBlockingTest {
        // Arrange
        every { userSubscriptionRepository.findById(request.email) } returns Optional.empty()

        // Act and Assert
        assertThrows<NotFoundException> {
            businessProfileService.validateBusinessProfile(request)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `validateBusinessProfile should throw ValidationException when validation returns false`() = runBlockingTest {
        // Arrange
        every { userSubscriptionRepository.findById(request.email) } returns Optional.of(userSubscription)
        every { validationClientHystrix.validate(request) } returns false

        // Act and Assert
        assertThrows<ValidationException> {
            businessProfileService.validateBusinessProfile(request)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `findById should throw DatabaseException when a MongoException occurs`() = runBlockingTest {
        // Arrange
        every { userSubscriptionRepository.findById(request.email) } returns Optional.of(userSubscription)
        every { validationClientHystrix.validate(request) } returns true
        every { businessProfileRepository.findById(request.email) } throws MongoException("Simulated MongoDB exception")

        // Act and Assert
        assertThrows<DatabaseException> {
            businessProfileService.validateBusinessProfile(request)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `validateBusinessProfile should throw Exception when an unexpected exception occurs`() =
        runBlockingTest {
        // Arrange
        every { userSubscriptionRepository.findById(request.email) } throws
                RuntimeException("Simulated unexpected exception")

        // Act and Assert
        assertThrows<Exception> {
            businessProfileService.validateBusinessProfile(request)
        }
    }

}
