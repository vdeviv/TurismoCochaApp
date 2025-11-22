package com.example.turismoapp.feature.calendar.data.repository
import com.example.turismoapp.Framework.local.dao.CalendarEventDao
import com.example.turismoapp.Framework.local.entity.CalendarEventEntity
import com.example.turismoapp.feature.calendar.domain.model.CalendarEvent
import com.example.turismoapp.feature.calendar.domain.repository.CalendarEventRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class CalendarEventRepositoryImpl(
    private val dao: CalendarEventDao
) : CalendarEventRepository {

    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    override fun getEventsByDate(date: LocalDate): Flow<List<CalendarEvent>> {
        val dateStr = date.format(formatter)
        return dao.getEventsByDate(dateStr).map { list ->
            list.map { it.toDomain() }
        }
    }

    override fun getEventsByMonth(year: Int, month: Int): Flow<List<CalendarEvent>> {
        val monthStr = "%02d".format(month)
        val prefix = "$year-$monthStr"
        return dao.getEventsByMonth(prefix).map { list ->
            list.map { it.toDomain() }
        }
    }

    override suspend fun insertEvent(event: CalendarEvent) {
        dao.insertEvent(event.toEntity())
    }

    private fun CalendarEventEntity.toDomain(): CalendarEvent {
        return CalendarEvent(
            id = id,
            title = title,
            location = location,
            price = price,
            date = LocalDate.parse(date, formatter),
            description = description
        )
    }

    private fun CalendarEvent.toEntity(): CalendarEventEntity {
        return CalendarEventEntity(
            id = id,
            title = title,
            location = location,
            price = price,
            date = date.format(formatter),
            description = description
        )
    }
}
