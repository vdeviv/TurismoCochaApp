package com.example.turismoapp.feature.github.domain.model

import org.junit.Test
class UrlPathTest {
    @Test(expected = Exception::class)
    fun testUrlPath() {
        UrlPath("myurl")
        UrlPath("")
    }
}