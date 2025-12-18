package com.turismoapp.mayuandino.feature.calendar.domain.usecase

import com.turismoapp.mayuandino.feature.calendar.domain.model.CalendarEvent
import com.turismoapp.mayuandino.feature.calendar.domain.repository.CalendarEventRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.time.LocalDate

class GetEventsByDateUseCaseTest {

    private lateinit var repository: CalendarEventRepository
    private lateinit var useCase: GetEventsByDateUseCase

    @Before
    fun setup() {
        repository = mockk()
        useCase = GetEventsByDateUseCase(repository)
    }

    @Test
    fun `invoke retorna eventos para fecha específica`() = runTest {
        // Given
        val date = LocalDate.of(2025, 12, 18)
        val events = listOf(
            CalendarEvent(
                id = "1",
                title = "Evento 1",
                location = "Cochabamba",
                price = 50.0,
                date = date,
                description = "Descripción 1",
                imageUrl = "url1"
            ),
            CalendarEvent(
                id = "2",
                title = "Evento 2",
                location = "La Paz",
                price = 75.0,
                date = date,
                description = "Descripción 2",
                imageUrl = "url2"
            )
        )

        coEvery { repository.getEventsByDate(date) } returns flowOf(events)

        // When
        val result = useCase(date).first()

        // Then
        assertEquals(2, result.size)
        assertEquals("Evento 1", result[0].title)
        assertEquals("Evento 2", result[1].title)
    }

    @Test
    fun `invoke retorna lista vacía cuando no hay eventos`() = runTest {
        // Given
        val date = LocalDate.of(2025, 12, 18)
        coEvery { repository.getEventsByDate(date) } returns flowOf(emptyList())

        // When
        val result = useCase(date).first()

        // Then
        assertEquals(0, result.size)
    }
}