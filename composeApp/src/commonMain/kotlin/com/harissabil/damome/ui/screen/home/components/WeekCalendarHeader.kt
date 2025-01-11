package com.harissabil.damome.ui.screen.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.kizitonwose.calendar.compose.WeekCalendar
import com.kizitonwose.calendar.compose.weekcalendar.rememberWeekCalendarState
import kotlinx.datetime.LocalDate
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.theme.MiuixTheme

@Composable
fun WeekCalendarHeader(
    modifier: Modifier = Modifier,
    currentDate: LocalDate,
    startDate: LocalDate,
    endDate: LocalDate,
    selection: LocalDate,
    onDateClick: (LocalDate) -> Unit,
) {
    val state = rememberWeekCalendarState(
        startDate = startDate,
        endDate = endDate,
        firstVisibleWeekDate = currentDate,
    )

    BoxWithConstraints(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.TopCenter,
    ) {
        WeekCalendar(
            modifier = modifier.fillMaxWidth(),
            state = state,
            calendarScrollPaged = true,
            dayContent = { day ->
                Day(
                    modifier = Modifier.width(this@BoxWithConstraints.maxWidth / 9f)
                        .align(Alignment.Center),
                    date = day.date,
                    selected = day.date == selection,
                    onClick = onDateClick
                )
            }
        )
    }
}

@Composable
private fun Day(
    modifier: Modifier = Modifier,
    date: LocalDate,
    selected: Boolean = false,
    onClick: (LocalDate) -> Unit = {},
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = date.dayOfWeek.name.first().toString(),
            style = MiuixTheme.textStyles.headline2
        )

        val backgroundColorModifier = if (selected) {
            Modifier.background(MiuixTheme.colorScheme.primary)
        } else Modifier

        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(RoundedCornerShape(4.dp))
                .then(backgroundColorModifier)
                .clickable { onClick(date) },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = date.dayOfMonth.toString(),
                style = MiuixTheme.textStyles.headline2,
                color = if (selected) MiuixTheme.colorScheme.onPrimary else MiuixTheme.colorScheme.onSurface,
            )
        }
    }
}