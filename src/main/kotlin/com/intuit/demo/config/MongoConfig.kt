package com.intuit.demo.config

import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory

@Configuration
class MongoConfig {

    //singleton
    @Bean
    fun mongoClient() = MongoClients.create()

    @Bean
    fun mongoDbFactory(mongoClient: MongoClient) =
        SimpleMongoClientDatabaseFactory(mongoClient, "idatabase")

    @Bean
    fun mongoTemplate(mongoDbFactory: SimpleMongoClientDatabaseFactory) =
        MongoTemplate(mongoDbFactory)
}
