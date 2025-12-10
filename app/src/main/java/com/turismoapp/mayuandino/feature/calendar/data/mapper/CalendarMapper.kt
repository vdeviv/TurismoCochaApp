package com.turismoapp.mayuandino.feature.calendar.data.mapper

import com.google.firebase.firestore.DocumentSnapshot
import com.turismoapp.mayuandino.framework.local.entity.CalendarEventEntity

fun DocumentSnapshot.toCalendarEventEntity(): CalendarEventEntity? {

    val id = this.id

    val date = this.getString("date") ?: return null
    val title = this.getString("title") ?: return null
    val location = this.getString("location") ?: ""
    val description = this.getString("description") ?: ""
    val imageUrl = this.getString("imageUrl") ?: ""
    val price = this.getLong("price")?.toDouble() ?: 0.0

    return CalendarEventEntity(
        id = id,
        date = date,
        title = title,
        location = location,
        price = price,
        description = description,
        imageUrl = imageUrl

    )
}
