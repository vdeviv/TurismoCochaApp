package com.turismoapp.mayuandino.feature.calendar.domain.model

import org.junit.Assert.*
import org.junit.Test
import java.time.LocalDate

class CalendarEventTest {

    @Test
    fun `crear CalendarEvent con valores correctos`() {
        val date = LocalDate.of(2025, 12, 25)
        val event = CalendarEvent(
            id = "1",
            title = "Navidad",
            location = "Cochabamba",
            price = 100.0,
            date = date,
            description = "Celebración navideña",
            imageUrl = "https://example.com/image.jpg"
        )

        assertEquals("1", event.id)
        assertEquals("Navidad", event.title)
        assertEquals("Cochabamba", event.location)
        assertEquals(100.0, event.price, 0.01)
        assertEquals(date, event.date)
    }

    @Test
    fun `CalendarEvent con valores por defecto`() {
        val event = CalendarEvent(
            id = "",
            title = "Evento",
            location = null,
            price = 0.0,
            date = LocalDate.now(),
            description = null,
            imageUrl = ""
        )

        assertEquals("", event.id)
        assertNull(event.location)
        assertNull(event.description)
    }
}