package com.turismoapp.mayuandino.feature.calendar.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.time.LocalDate

@Composable
fun MiniCalendar(
    currentMonth: LocalDate,
    selectedDate: LocalDate,
    onDayClick: (LocalDate) -> Unit
) {
    val daysInMonth = currentMonth.lengthOfMonth()
    val firstDay = currentMonth.withDayOfMonth(1).dayOfWeek.value % 7

    Column {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            listOf("L", "M", "X", "J", "V", "S", "D").forEach { day ->
                Text(day, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        var day = 1

        for (row in 0 until 6) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                for (col in 0 until 7) {

                    if (row == 0 && col < firstDay || day > daysInMonth) {
                        Box(modifier = Modifier.size(40.dp))
                    } else {
                        val date = currentMonth.withDayOfMonth(day)
                        val isSelected = selectedDate == date

                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(
                                    if (isSelected)
                                        MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                                    else
                                        MaterialTheme.colorScheme.surface
                                )
                                .clickable { onDayClick(date) },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = day.toString(),
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        }

                        day++
                    }
                }
            }
        }
    }
}
