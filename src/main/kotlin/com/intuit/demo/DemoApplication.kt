package com.intuit.demo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.netflix.hystrix.EnableHystrix
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.retry.annotation.EnableRetry

@SpringBootApplication
@EnableFeignClients
@EnableHystrix
@EnableRetry
class DemoApplication

fun main(args: Array<String>) {
	runApplication<DemoApplication>(*args)
}
