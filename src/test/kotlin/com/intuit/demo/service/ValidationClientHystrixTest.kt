import com.intuit.demo.client.ValidationClient
import com.intuit.demo.model.schema.BusinessAddress
import com.intuit.demo.model.schema.BusinessProfile
import com.intuit.demo.model.schema.TaxIdentifiers
import com.intuit.demo.service.ValidationClientHystrix
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class ValidationClientHystrixTest {

    @Mock
    private lateinit var validationClient: ValidationClient

    @InjectMocks
    private lateinit var validationClientHystrix: ValidationClientHystrix

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this)
    }

    private val businessAddress = BusinessAddress("123 Main St", "Apt 456", "Cityville", "CA", "12345", "USA")
    private val taxIdentifiers = TaxIdentifiers("ABCDE1234F", "123456789")

    private val businessProfile = BusinessProfile(
        email = "test@example.com",
        companyName = "ABC Company",
        legalName = "ABC Legal",
        businessAddress = businessAddress,
        legalAddress = "789 Legal St, Legal City, Legal State, 67890, USA",
        taxIdentifiers = taxIdentifiers,
        website = "http://www.example.com"
    )

    @Test
    fun testValidationSuccess() {
        Mockito.`when`(validationClient.callValidationApi(businessProfile)).thenReturn(true)

        // Act
        val result = validationClientHystrix.validate(businessProfile)

        // Assert
        Assertions.assertTrue(result)
    }

    @Test
    fun testValidationFailureWithFallback() {
        Mockito.`when`(validationClient.callValidationApi(businessProfile))
            .thenThrow(RuntimeException("Validation API error"))

        // Act
        val result = validationClientHystrix.validate(businessProfile)

        // Assert
        Assertions.assertFalse(result)
    }
}
