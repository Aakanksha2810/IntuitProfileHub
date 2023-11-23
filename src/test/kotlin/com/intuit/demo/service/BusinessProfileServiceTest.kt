import com.intuit.demo.exception.BusinessProfileValidationException
import com.intuit.demo.exception.HttpResponseException
import com.intuit.demo.exception.NotFoundException
import com.intuit.demo.model.schema.*
import com.intuit.demo.repository.BusinessProfileRepository
import com.intuit.demo.repository.UserSubscriptionRepository
import com.intuit.demo.service.BusinessProfileService
import com.intuit.demo.service.ValidationClientHystrix
import com.mongodb.MongoException
import io.mockk.*
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.data.mongodb.core.FindAndModifyOptions
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.insert
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import java.util.*

@ExtendWith(MockKExtension::class)
class BusinessProfileServiceTest {

    private val mongoTemplate = mockk<MongoTemplate>()
    private val businessProfileRepository = mockk<BusinessProfileRepository>()
    private val userSubscriptionRepository = mockk<UserSubscriptionRepository>()
    private val validationClientHystrix = mockk<ValidationClientHystrix>()

    private val businessProfileService = BusinessProfileService(
        mongoTemplate,
        businessProfileRepository,
        userSubscriptionRepository,
        validationClientHystrix
    )

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

    @Test
    fun `test updateBusinessProfile`() {
        // Mock the findAndModify result
        val updatedBusinessProfile =  BusinessProfile(
            email = "test@example.com",
            companyName = "ABC Company",
            legalName = "ABC Legal",
            businessAddress = businessAddress,
            legalAddress = "789 Legal St, Legal City, Legal State, 67890, USA",
            taxIdentifiers = taxIdentifiers,
            website = "http://www.example.com"
        )

        every {
            mongoTemplate.findAndModify(
                any(),
                any(),
                any(),
                BusinessProfile::class.java
            )
        } returns updatedBusinessProfile

        // Use reflection to invoke the private method
        val privateMethod = BusinessProfileService::class.java
            .getDeclaredMethod("updateBusinessProfile", BusinessProfile::class.java)
        privateMethod.isAccessible = true
        val result = privateMethod.invoke(businessProfileService, request) as BusinessProfile?

        // Verify interactions
        verify {
            mongoTemplate.findAndModify(
                Query.query(Criteria.where("email").`is`(request.email)),
                any<Update>(),
                any(),
                BusinessProfile::class.java
            )
        }

        // Assert the result
        // You can assert the result based on your business logic
        // For example, if the update is successful, the result should not be null
        assert(result != null)
        assertEquals(result?.taxIdentifiers?.pan, "ABCDE1234F")
        assertEquals(result?.taxIdentifiers?.ein, "123456789")
    }

    @Test
    @ExperimentalCoroutinesApi
    fun `test validateBusinessProfile with successful validation and create`() = runBlockingTest {
        val userSubscription = UserSubscription("trp@gmail.com", listOf(Subscriptions("","")))

        every { userSubscriptionRepository.findById(request.email) } returns Optional.of(userSubscription)
        every { validationClientHystrix.validate(request) } returns true
        every { businessProfileRepository.findById(request.email).isPresent } returns false
        every { mongoTemplate.insert(request) } returns request

        businessProfileService.validateBusinessProfile(request)

        verify(exactly = 1) { userSubscriptionRepository.findById(request.email) }
        coVerify(exactly = 1) { validationClientHystrix.validate(request) }
        verify(exactly = 1) { businessProfileRepository.findById(request.email) }
    }

    @Test
    @ExperimentalCoroutinesApi
    fun `test validateBusinessProfile with successful validation and update`() = runBlockingTest {
        val userSubscription = UserSubscription("trp@gmail.com", listOf(Subscriptions("","")))

        every { userSubscriptionRepository.findById(request.email) } returns Optional.of(userSubscription)
        every { validationClientHystrix.validate(request) } returns true
        every { businessProfileRepository.findById(request.email).isPresent } returns true
        every {
            mongoTemplate.findAndModify(
                any(),
                any(),
                any(),
                BusinessProfile::class.java
            )
        } returns request


        businessProfileService.validateBusinessProfile(request)

        verify(exactly = 1) { userSubscriptionRepository.findById(request.email) }
        coVerify(exactly = 1) { validationClientHystrix.validate(request) }
        verify(exactly = 1) { businessProfileRepository.findById(request.email) }
    }

    @Test
    @ExperimentalCoroutinesApi
    fun `test validateBusinessProfile with mongo exception and update`() = runBlockingTest {
        val userSubscription = UserSubscription("trp@gmail.com", listOf(Subscriptions("","")))
        val mongoException = mockk<MongoException>()
        every { userSubscriptionRepository.findById(request.email) } returns Optional.of(userSubscription)
        every { validationClientHystrix.validate(request) } returns true
        every { businessProfileRepository.findById(request.email).isPresent } returns true
        every {
            mongoTemplate.findAndModify(
                any(),
                any(),
                any(),
                BusinessProfile::class.java
            )
        } throws mongoException


        assertThrows<HttpResponseException> {
            businessProfileService.validateBusinessProfile(request)
        }

        verify(exactly = 1) { userSubscriptionRepository.findById(request.email) }
        coVerify(exactly = 1) { validationClientHystrix.validate(request) }
        verify(exactly = 1) { businessProfileRepository.findById(request.email) }
    }

    @Test
    @ExperimentalCoroutinesApi
    fun `test validateBusinessProfile with false validation`() = runBlockingTest {
        val userSubscription = UserSubscription("", listOf(Subscriptions("","")))

        every { userSubscriptionRepository.findById(request.email) } returns Optional.of(userSubscription)
        every { validationClientHystrix.validate(request) } returns false
        every { businessProfileRepository.findById(request.email).isPresent } returns false
        every { mongoTemplate.insert(request) } returns request

        assertThrows<BusinessProfileValidationException> {
            businessProfileService.validateBusinessProfile(request)
        }

        verify(exactly = 1) { userSubscriptionRepository.findById(request.email) }
        coVerify(exactly = 1) { validationClientHystrix.validate(request) }
    }

    @Test
    @ExperimentalCoroutinesApi
    fun `test validateBusinessProfile with mongo exception for createBusinessProfile`() = runBlockingTest {
        val userSubscription = UserSubscription("", listOf(Subscriptions("","")))
        val mongoException = mockk<MongoException>()
        every { userSubscriptionRepository.findById(request.email) } returns Optional.of(userSubscription)
        every { validationClientHystrix.validate(request) } returns true
        every { businessProfileRepository.findById(request.email).isPresent } returns false
        every { mongoTemplate.insert(request) } throws  mongoException

        assertThrows<HttpResponseException> {
            businessProfileService.validateBusinessProfile(request)
        }

        verify(exactly = 1) { userSubscriptionRepository.findById(request.email) }
        coVerify(exactly = 1) { validationClientHystrix.validate(request) }
        verify(exactly = 1) { businessProfileRepository.findById(request.email) }
    }

    @Test
    @ExperimentalCoroutinesApi
    fun `test validateBusinessProfile with mongo exception for findById`() = runBlockingTest {
        val userSubscription = UserSubscription("", listOf(Subscriptions("","")))
        val mongoException = mockk<MongoException>()
        every { userSubscriptionRepository.findById(request.email) } returns Optional.of(userSubscription)
        every { validationClientHystrix.validate(request) } returns true
        every { businessProfileRepository.findById(request.email) } throws mongoException

        assertThrows<HttpResponseException> {
            businessProfileService.validateBusinessProfile(request)
        }

        verify(exactly = 1) { userSubscriptionRepository.findById(request.email) }
        coVerify(exactly = 1) { validationClientHystrix.validate(request) }
    }

    @Test
    @ExperimentalCoroutinesApi
    fun `test validateBusinessProfile with NotFound exception`() = runBlockingTest {
        every { userSubscriptionRepository.findById(request.email) } returns Optional.empty()
        assertThrows<NotFoundException> {
            businessProfileService.validateBusinessProfile(request)
        }
        verify(exactly = 1) { userSubscriptionRepository.findById(request.email) }
    }
}
