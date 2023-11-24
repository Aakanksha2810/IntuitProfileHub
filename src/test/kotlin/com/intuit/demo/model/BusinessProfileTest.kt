//package com.intuit.demo.model
//
//import com.intuit.demo.model.schema.*
//import org.junit.jupiter.api.Assertions.assertEquals
//import org.junit.jupiter.api.Test
//
//class BusinessProfileTest {
//
//    @Test
//    fun `create business profile`() {
//        val businessAddress = BusinessAddress("123 Main St", "Apt 456", "Cityville", "CA", "12345", "USA")
//        val taxIdentifiers = TaxIdentifiers("ABCDE1234F", "123456789")
//
//        val businessProfile = BusinessProfile(
//            email = "test@example.com",
//            companyName = "ABC Company",
//            legalName = "ABC Legal",
//            businessAddress = businessAddress,
//            legalAddress = "789 Legal St, Legal City, Legal State, 67890, USA",
//            taxIdentifiers = taxIdentifiers,
//            website = "http://www.example.com"
//        )
//
//        assertEquals("test@example.com", businessProfile.email)
//        assertEquals("ABC Company", businessProfile.companyName)
//        assertEquals("ABC Legal", businessProfile.legalName)
//        assertEquals(businessAddress, businessProfile.businessAddress)
//        assertEquals("789 Legal St, Legal City, Legal State, 67890, USA", businessProfile.legalAddress)
//        assertEquals(taxIdentifiers, businessProfile.taxIdentifiers)
//        assertEquals("http://www.example.com", businessProfile.website)
//    }
//
////    @Test
////    fun `equality check`() {
////        val businessAddress1 = BusinessAddress("123 Main St", "Apt 456", "Cityville", "CA", "12345", "USA")
////        val taxIdentifiers1 = TaxIdentifiers("ABCDE1234F", "123456789")
////
////        val businessProfile1 = BusinessProfile(
////            email = "test@example.com",
////            companyName = "ABC Company",
////            legalName = "ABC Legal",
////            businessAddress = businessAddress1,
////            legalAddress = "789 Legal St, Legal City, Legal State, 67890, USA",
////            taxIdentifiers = taxIdentifiers1,
////            website = "http://www.example.com"
////        )
////
////        val businessAddress2 = BusinessAddress("456 Oak St", null, "Townsville", "NY", "56789", "USA")
////        val taxIdentifiers2 = TaxIdentifiers("FGHIJ6789K", "987654321")
////
////        val businessProfile2 = BusinessProfile(
////            email = "test@example.com",
////            companyName = "XYZ Corporation",
////            legalName = "XYZ Legal",
////            businessAddress = businessAddress2,
////            legalAddress = "456 Legal St, Legal City, Legal State, 56789, USA",
////            taxIdentifiers = taxIdentifiers2,
////            website = "http://www.xyz-corp.com"
////        )
////
////        assertEquals(businessProfile1, businessProfile2)
////    }
//}
//
//class UserSubscriptionTest {
//
//    @Test
//    fun `user subscription data class should have correct values`() {
//        // Arrange
//        val subscriptions = listOf(
//            Subscriptions("sub1", "Subscription 1"),
//            Subscriptions("sub2", "Subscription 2")
//        )
//
//        val userSubscription = UserSubscription(
//            emailId = "test@example.com",
//            subscriptions = subscriptions
//        )
//
//        // Act & Assert
//        assertEquals("test@example.com", userSubscription.emailId)
//        assertEquals(subscriptions, userSubscription.subscriptions)
//    }
//
//    @Test
//    fun `setCompanyName should update companyName property`() {
//        // Arrange
//        val businessAddress = BusinessAddress("123 Main St", "Apt 456", "Cityville", "CA", "12345", "USA")
//        val taxIdentifiers = TaxIdentifiers("ABCDE1234F", "123456789")
//
//        val businessProfile = BusinessProfile(
//            email = "test@example.com",
//            companyName = "ABC Company",
//            legalName = "ABC Legal",
//            businessAddress = businessAddress,
//            legalAddress = "789 Legal St, Legal City, Legal State, 67890, USA",
//            taxIdentifiers = taxIdentifiers,
//            website = "http://www.example.com"
//        )
//
//        // Act
//        businessProfile.companyName = "New Company Name"
//
//        // Assert
//        assertEquals("New Company Name", businessProfile.companyName)
//    }
//
//    @Test
//    fun `setCompanyName should update legalName property`() {
//        // Arrange
//        val businessAddress = BusinessAddress("123 Main St", "Apt 456", "Cityville", "CA", "12345", "USA")
//        val taxIdentifiers = TaxIdentifiers("ABCDE1234F", "123456789")
//
//        val businessProfile = BusinessProfile(
//            email = "test@example.com",
//            companyName = "ABC Company",
//            legalName = "ABC Legal",
//            businessAddress = businessAddress,
//            legalAddress = "789 Legal St, Legal City, Legal State, 67890, USA",
//            taxIdentifiers = taxIdentifiers,
//            website = "http://www.example.com"
//        )
//        // Act
//        businessProfile.legalName = "New Company Name"
//
//        // Assert
//        assertEquals("New Company Name", businessProfile.legalName)
//    }
//
//    @Test
//    fun `setCompanyName should update businessAddress property`() {
//        // Arrange
//        val businessAddress = BusinessAddress("123 Main St", "Apt 456", "Cityville", "CA", "12345", "USA")
//        val taxIdentifiers = TaxIdentifiers("ABCDE1234F", "123456789")
//
//        val businessProfile = BusinessProfile(
//            email = "test@example.com",
//            companyName = "ABC Company",
//            legalName = "ABC Legal",
//            businessAddress = businessAddress,
//            legalAddress = "789 Legal St, Legal City, Legal State, 67890, USA",
//            taxIdentifiers = taxIdentifiers,
//            website = "http://www.example.com"
//        )
//        // Act
//        businessProfile.businessAddress.city = "New Company city"
//
//        // Assert
//        assertEquals("New Company city", businessProfile.businessAddress.city)
//    }
//
//    @Test
//    fun `setCompanyName should update legalAddress property`() {
//        // Arrange
//        val businessAddress = BusinessAddress("123 Main St", "Apt 456", "Cityville", "CA", "12345", "USA")
//        val taxIdentifiers = TaxIdentifiers("ABCDE1234F", "123456789")
//
//        val businessProfile = BusinessProfile(
//            email = "test@example.com",
//            companyName = "ABC Company",
//            legalName = "ABC Legal",
//            businessAddress = businessAddress,
//            legalAddress = "789 Legal St, Legal City, Legal State, 67890, USA",
//            taxIdentifiers = taxIdentifiers,
//            website = "http://www.example.com"
//        )
//        // Act
//        businessProfile.legalAddress = "New Company address"
//
//        // Assert
//        assertEquals("New Company address", businessProfile.legalAddress)
//    }
//
//    @Test
//    fun `setCompanyName should update website property`() {
//        // Arrange
//        val businessAddress = BusinessAddress("123 Main St", "Apt 456", "Cityville", "CA", "12345", "USA")
//        val taxIdentifiers = TaxIdentifiers("ABCDE1234F", "123456789")
//
//        val businessProfile = BusinessProfile(
//            email = "test@example.com",
//            companyName = "ABC Company",
//            legalName = "ABC Legal",
//            businessAddress = businessAddress,
//            legalAddress = "789 Legal St, Legal City, Legal State, 67890, USA",
//            taxIdentifiers = taxIdentifiers,
//            website = "http://www.example.com"
//        )
//        // Act
//        businessProfile.website = "New website"
//
//        // Assert
//        assertEquals("New website", businessProfile.website)
//    }
//}
//
//class SubscriptionsTest {
//
//    @Test
//    fun `subscriptions data class should have correct values`() {
//        // Arrange
//        val subscription = Subscriptions("sub1", "Subscription 1")
//
//        // Act & Assert
//        assertEquals("sub1", subscription.subscriptionId)
//        assertEquals("Subscription 1", subscription.subscriptionName)
//    }
//}
