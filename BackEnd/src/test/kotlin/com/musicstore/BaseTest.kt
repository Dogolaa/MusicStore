package com.musicstore

import io.ktor.server.config.ApplicationConfig
import io.ktor.server.testing.ApplicationTestBuilder
import io.ktor.server.testing.testApplication

open class BaseTest {
    protected fun testApp(block: suspend ApplicationTestBuilder.() -> Unit) {
        testApplication {
            environment {
                config = ApplicationConfig("application_test.yaml")
            }
            block()
        }
    }
}
