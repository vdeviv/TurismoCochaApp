package com.turismoapp.mayuandino.feature.calendar.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.turismoapp.mayuandino.feature.calendar.data.mapper.toCalendarEventEntity
import com.turismoapp.mayuandino.framework.local.dao.CalendarEventDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class CalendarRepository(
    private val firestore: FirebaseFirestore,
    private val dao: CalendarEventDao
) {

    suspend fun syncCalendarEvents() = withContext(Dispatchers.IO) {

        val snapshot = firestore.collection("calendar_events")
            .get()
            .await()

        val entities = snapshot.documents.mapNotNull { it.toCalendarEventEntity() }

        if (entities.isNotEmpty()) {
            dao.insertEvents(entities)
        }
    }
}
