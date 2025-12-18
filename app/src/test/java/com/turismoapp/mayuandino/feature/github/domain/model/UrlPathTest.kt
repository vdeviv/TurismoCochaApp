package com.turismoapp.mayuandino.feature.github.domain.model

import com.turismoapp.mayuandino.feature.profile.domain.vo.UrlPath
import org.junit.Test
import org.junit.Assert.assertEquals

class UrlPathTest {

    @Test(expected = IllegalArgumentException::class)
    fun empty_url_throws_exception() {
        UrlPath("")
    }

    @Test
    fun valid_url_is_created_successfully() {
        val url = UrlPath("myurl")
        assertEquals("myurl", url.value)
    }
}
