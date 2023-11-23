package com.intuit.demo.repository

import com.intuit.demo.model.schema.BusinessProfile
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface BusinessProfileRepository: MongoRepository<BusinessProfile, String>