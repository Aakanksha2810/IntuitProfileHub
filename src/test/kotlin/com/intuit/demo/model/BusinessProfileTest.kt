package com.intuit.demo.model

import com.intuit.demo.model.schema.BusinessProfile
import com.intuit.demo.model.schema.BusinessAddress
import com.intuit.demo.model.schema.Subscriptions
import com.intuit.demo.model.schema.TaxIdentifiers
import com.intuit.demo.model.schema.UserSubscription
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class BusinessProfileTest {

    @Test
    fun `create business profile`() {
        val businessAddress = BusinessAddress("123 Main St", "Apt 456", "Cityville", "CA", "12345", "USA")
        val taxIdentifiers = TaxIdentifiers("ABCDE1234F", "123456789")

        val businessProfile = BusinessProfile(
            email = "test@example.com",
            companyName = "ABC Company",
            legalName = "ABC Legal",
            businessAddress = businessAddress,
            legalAddress = "789 Legal St, Legal City, Legal State, 67890, USA",
            taxIdentifiers = taxIdentifiers,
            website = "http://www.example.com"
        )

        assertEquals("test@example.com", businessProfile.email)
        assertEquals("ABC Company", businessProfile.companyName)
        assertEquals("ABC Legal", businessProfile.legalName)
        assertEquals(businessAddress, businessProfile.businessAddress)
        assertEquals("789 Legal St, Legal City, Legal State, 67890, USA", businessProfile.legalAddress)
        assertEquals(taxIdentifiers, businessProfile.taxIdentifiers)
        assertEquals("http://www.example.com", businessProfile.website)
    }

    @Test
    fun `test setters for BusinessProfile class`() {
        // Given
        val businessAddress = BusinessAddress("123 Main St", "Apt 456", "Cityville", "CA", "12345", "USA")
        val taxIdentifiers = TaxIdentifiers("ABCDE1234F", "123456789")
        val businessProfile = BusinessProfile(
            email = "newemail@example.com",
            companyName = "ABC Company",
            legalName = "ABC Legal",
            businessAddress = businessAddress,
            legalAddress = "789 Legal St, Legal City, Legal State, 67890, USA",
            taxIdentifiers = taxIdentifiers,
            website = "http://www.example.com"
        )

        // When
        businessProfile.companyName = "New Company"
        businessProfile.legalName = "New Legal"
        businessProfile.businessAddress = BusinessAddress("456 Oak St", null, "Townsville", "NY", "54321", "USA")
        businessProfile.legalAddress = "987 Legal St, Legal City, Legal State, 87654, USA"
        businessProfile.website = "http://new.example.com"

        // Then
        assertThat(businessProfile.email).isEqualTo("newemail@example.com")
        assertThat(businessProfile.companyName).isEqualTo("New Company")
        assertThat(businessProfile.legalName).isEqualTo("New Legal")
        assertThat(businessProfile.businessAddress)
            .isEqualTo(BusinessAddress("456 Oak St", null, "Townsville", "NY", "54321", "USA"))
        assertThat(businessProfile.legalAddress).isEqualTo("987 Legal St, Legal City, Legal State, 87654, USA")
        assertThat(businessProfile.taxIdentifiers).isEqualTo(TaxIdentifiers("ABCDE1234F", "123456789"))
        assertThat(businessProfile.website).isEqualTo("http://new.example.com")
    }

    @Test
    fun `test setters for BusinessAddress class`() {
        // Given
        val businessAddress = BusinessAddress("123 Main St", "Apt 456", "Cityville", "CA", "12345", "USA")

        // When
        businessAddress.line1 = "456 Oak St"
        businessAddress.line2 = null
        businessAddress.city = "Townsville"
        businessAddress.state = "NY"
        businessAddress.zip = "54321"

        // Then
        assertThat(businessAddress.line1).isEqualTo("456 Oak St")
        assertThat(businessAddress.line2).isNull()
        assertThat(businessAddress.city).isEqualTo("Townsville")
        assertThat(businessAddress.state).isEqualTo("NY")
        assertThat(businessAddress.zip).isEqualTo("54321")
        assertThat(businessAddress.country).isEqualTo("USA")
    }

    @Test
    fun `test setters for TaxIdentifiers class`() {
        // Given
        val taxIdentifiers = TaxIdentifiers("ABCDE1234F", "123456789")

        // Then
        assertThat(taxIdentifiers.pan).isEqualTo("ABCDE1234F")
        assertThat(taxIdentifiers.ein).isEqualTo("123456789")
    }
}

class UserSubscriptionTest {

    @Test
    fun `user subscription data class should have correct values`() {
        // Arrange
        val subscriptions = listOf(
            Subscriptions("sub1", "Subscription 1"),
            Subscriptions("sub2", "Subscription 2")
        )

        val userSubscription = UserSubscription(
            emailId = "test@example.com",
            subscriptions = subscriptions
        )

        // Act & Assert
        assertEquals("test@example.com", userSubscription.emailId)
        assertEquals(subscriptions, userSubscription.subscriptions)
    }

    @Test
    fun `setCompanyName should update companyName property`() {
        // Arrange
        val businessAddress = BusinessAddress("123 Main St", "Apt 456", "Cityville", "CA", "12345", "USA")
        val taxIdentifiers = TaxIdentifiers("ABCDE1234F", "123456789")

        val businessProfile = BusinessProfile(
            email = "test@example.com",
            companyName = "ABC Company",
            legalName = "ABC Legal",
            businessAddress = businessAddress,
            legalAddress = "789 Legal St, Legal City, Legal State, 67890, USA",
            taxIdentifiers = taxIdentifiers,
            website = "http://www.example.com"
        )

        // Act
        businessProfile.companyName = "New Company Name"

        // Assert
        assertEquals("New Company Name", businessProfile.companyName)
    }

    @Test
    fun `setCompanyName should update legalName property`() {
        // Arrange
        val businessAddress = BusinessAddress("123 Main St", "Apt 456", "Cityville", "CA", "12345", "USA")
        val taxIdentifiers = TaxIdentifiers("ABCDE1234F", "123456789")

        val businessProfile = BusinessProfile(
            email = "test@example.com",
            companyName = "ABC Company",
            legalName = "ABC Legal",
            businessAddress = businessAddress,
            legalAddress = "789 Legal St, Legal City, Legal State, 67890, USA",
            taxIdentifiers = taxIdentifiers,
            website = "http://www.example.com"
        )
        // Act
        businessProfile.legalName = "New Company Name"

        // Assert
        assertEquals("New Company Name", businessProfile.legalName)
    }

    @Test
    fun `setCompanyName should update businessAddress property`() {
        // Arrange
        val businessAddress = BusinessAddress("123 Main St", "Apt 456", "Cityville", "CA", "12345", "USA")
        val taxIdentifiers = TaxIdentifiers("ABCDE1234F", "123456789")

        val businessProfile = BusinessProfile(
            email = "test@example.com",
            companyName = "ABC Company",
            legalName = "ABC Legal",
            businessAddress = businessAddress,
            legalAddress = "789 Legal St, Legal City, Legal State, 67890, USA",
            taxIdentifiers = taxIdentifiers,
            website = "http://www.example.com"
        )
        // Act
        businessProfile.businessAddress.city = "New Company city"

        // Assert
        assertEquals("New Company city", businessProfile.businessAddress.city)
    }

    @Test
    fun `setCompanyName should update legalAddress property`() {
        // Arrange
        val businessAddress = BusinessAddress("123 Main St", "Apt 456", "Cityville", "CA", "12345", "USA")
        val taxIdentifiers = TaxIdentifiers("ABCDE1234F", "123456789")

        val businessProfile = BusinessProfile(
            email = "test@example.com",
            companyName = "ABC Company",
            legalName = "ABC Legal",
            businessAddress = businessAddress,
            legalAddress = "789 Legal St, Legal City, Legal State, 67890, USA",
            taxIdentifiers = taxIdentifiers,
            website = "http://www.example.com"
        )
        // Act
        businessProfile.legalAddress = "New Company address"

        // Assert
        assertEquals("New Company address", businessProfile.legalAddress)
    }

    @Test
    fun `setCompanyName should update website property`() {
        // Arrange
        val businessAddress = BusinessAddress("123 Main St", "Apt 456", "Cityville", "CA", "12345", "USA")
        val taxIdentifiers = TaxIdentifiers("ABCDE1234F", "123456789")

        val businessProfile = BusinessProfile(
            email = "test@example.com",
            companyName = "ABC Company",
            legalName = "ABC Legal",
            businessAddress = businessAddress,
            legalAddress = "789 Legal St, Legal City, Legal State, 67890, USA",
            taxIdentifiers = taxIdentifiers,
            website = "http://www.example.com"
        )
        // Act
        businessProfile.website = "New website"

        // Assert
        assertEquals("New website", businessProfile.website)
    }
}

class SubscriptionsTest {

    @Test
    fun `subscriptions data class should have correct values`() {
        // Arrange
        val subscription = Subscriptions("sub1", "Subscription 1")

        // Act & Assert
        assertEquals("sub1", subscription.subscriptionId)
        assertEquals("Subscription 1", subscription.subscriptionName)
    }
}
