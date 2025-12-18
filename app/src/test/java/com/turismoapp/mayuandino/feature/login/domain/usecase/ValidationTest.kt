package com.turismoapp.mayuandino.feature.login.domain.usecase

import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class ValidationTest {
    private val validateEmail = ValidateEmailUseCase()
    private val validatePassword = ValidatePasswordUseCase()

    @Test
    fun `email vacio retorna error`() {
        val result = validateEmail("")
        assertNotNull(result)
    }

    @Test
    fun `password sin mayuscula retorna error`() {
        val result = validatePassword("123456a")
        assertNotNull(result)
    }

    @Test
    fun `password correcto retorna null`() {
        val result = validatePassword("Password123")
        assertNull(result)
    }
}