package com.turismoapp.mayuandino.feature.calendar.domain.usecase

import com.turismoapp.mayuandino.feature.calendar.domain.model.CalendarEvent
import com.turismoapp.mayuandino.feature.calendar.domain.repository.CalendarEventRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.time.LocalDate

class GetEventsByMonthUseCaseTest {

    private lateinit var repository: CalendarEventRepository
    private lateinit var useCase: GetEventsByMonthUseCase

    @Before
    fun setup() {
        repository = mockk()
        useCase = GetEventsByMonthUseCase(repository)
    }

    @Test
    fun `invoke retorna todos los eventos del mes`() = runTest {
        // Given
        val year = 2025
        val month = 12
        val events = listOf(
            CalendarEvent(
                id = "1",
                title = "Evento 1",
                location = "Cochabamba",
                price = 50.0,
                date = LocalDate.of(2025, 12, 5),
                description = "Desc 1",
                imageUrl = "url1"
            ),
            CalendarEvent(
                id = "2",
                title = "Evento 2",
                location = "La Paz",
                price = 75.0,
                date = LocalDate.of(2025, 12, 20),
                description = "Desc 2",
                imageUrl = "url2"
            )
        )

        coEvery { repository.getEventsByMonth(year, month) } returns flowOf(events)

        // When
        val result = useCase(year, month).first()

        // Then
        assertEquals(2, result.size)
        assertTrue(result.all { it.date.year == 2025 && it.date.monthValue == 12 })
    }

    @Test
    fun `invoke retorna lista vacía para mes sin eventos`() = runTest {
        // Given
        coEvery { repository.getEventsByMonth(2025, 1) } returns flowOf(emptyList())

        // When
        val result = useCase(2025, 1).first()

        // Then
        assertEquals(0, result.size)
    }
}