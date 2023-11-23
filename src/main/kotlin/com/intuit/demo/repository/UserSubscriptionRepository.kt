package com.intuit.demo.repository

import com.intuit.demo.model.schema.UserSubscription
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Component
import org.springframework.stereotype.Repository

@Repository
@Component
interface UserSubscriptionRepository: MongoRepository<UserSubscription, String>