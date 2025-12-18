package com.turismoapp.mayuandino.feature.calendar.domain.usecase

import com.turismoapp.mayuandino.feature.calendar.domain.model.CalendarEvent
import com.turismoapp.mayuandino.feature.calendar.domain.repository.CalendarEventRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import java.time.LocalDate

class InsertCalendarEventUseCaseTest {

    private lateinit var repository: CalendarEventRepository
    private lateinit var useCase: InsertCalendarEventUseCase

    @Before
    fun setup() {
        repository = mockk(relaxed = true)
        useCase = InsertCalendarEventUseCase(repository)
    }

    @Test
    fun `invoke inserta evento correctamente`() = runTest {
        // Given
        val event = CalendarEvent(
            id = "1",
            title = "Nuevo Evento",
            location = "Cochabamba",
            price = 100.0,
            date = LocalDate.of(2025, 12, 25),
            description = "Descripción",
            imageUrl = "url"
        )

        coEvery { repository.insertEvent(event) } returns Unit

        // When
        useCase(event)

        // Then
        coVerify(exactly = 1) { repository.insertEvent(event) }
    }
}