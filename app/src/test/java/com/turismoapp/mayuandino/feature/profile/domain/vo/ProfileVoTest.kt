package com.turismoapp.mayuandino.feature.profile.domain.vo

import org.junit.Test
import org.junit.Assert.assertEquals

class ProfileVoTest {

    @Test
    fun `PersonName exitoso con mas de 4 caracteres`() {
        val nombre = "Juan Perez"
        val vo = PersonName(nombre)
        assertEquals(nombre, vo.value)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `PersonName falla si es muy corto`() {
        PersonName("Ana")
    }

    @Test
    fun `EmailAddress valida formato correctamente`() {
        val email = "test@mail.com"
        val vo = EmailAddress(email)
        assertEquals(email, vo.value)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `EmailAddress falla con formato invalido`() {
        EmailAddress("correo-sin-arroba")
    }
}