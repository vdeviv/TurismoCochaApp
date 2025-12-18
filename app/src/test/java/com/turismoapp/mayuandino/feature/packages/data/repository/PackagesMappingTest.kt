package com.turismoapp.mayuandino.feature.packages.data.repository

import com.turismoapp.mayuandino.framework.dto.PlaceDto
import org.junit.Assert.assertEquals
import org.junit.Test

class PackagesMappingTest {

    @Test
    fun `mapeo de PlaceDto a PackageModel mantiene datos integros`() {
        val dto = PlaceDto(id = "1", name = "Tour Tunari", rating = 4.5, city = "Quillacollo")

        assertEquals("Tour Tunari", dto.name)
        assertEquals(4.5, dto.rating, 0.0)
        assertEquals("Quillacollo", dto.city)
    }
}