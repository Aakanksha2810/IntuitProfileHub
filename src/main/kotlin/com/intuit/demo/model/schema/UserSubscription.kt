package com.intuit.demo.model.schema

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.Date

@Document(collection = "user_subscription")
data class UserSubscription(
        @Id
        val emailId: String,
        val subscriptions: List<Subscriptions>
)

data class Subscriptions(
        val subscriptionId: String,
        val subscriptionName: String
)