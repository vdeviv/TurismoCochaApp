package com.turismoapp.mayuandino.feature.calendar.data.mapper

import com.google.firebase.firestore.DocumentSnapshot
import com.turismoapp.mayuandino.framework.local.entity.CalendarEventEntity

fun DocumentSnapshot.toCalendarEventEntity(): CalendarEventEntity? {
    val date = getString("date") ?: return null
    val title = getString("title") ?: return null
    val location = getString("location") ?: ""
    val description = getString("description") ?: ""
    val imageUrl = getString("imageUrl") ?: ""
    val price = getDouble("price") ?: 0.0

    return CalendarEventEntity(
        id = 0L,
        title = title,
        location = location,
        price = price,
        date = date,
        description = description,
        imageUrl = imageUrl
    )
}
