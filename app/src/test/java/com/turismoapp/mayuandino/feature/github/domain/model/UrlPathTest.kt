package com.turismoapp.mayuandino.feature.github.domain.model

import com.turismoapp.mayuandino.feature.profile.domain.vo.UrlPath
import org.junit.Test
class UrlPathTest {
    @Test(expected = Exception::class)
    fun testUrlPath() {
        UrlPath("myurl")
        UrlPath("")
    }
}