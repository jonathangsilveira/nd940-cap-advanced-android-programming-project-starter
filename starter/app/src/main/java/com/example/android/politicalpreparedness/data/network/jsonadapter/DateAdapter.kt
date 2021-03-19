package com.example.android.politicalpreparedness.data.network.jsonadapter

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class DateAdapter {

    private val format: DateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    @FromJson
    fun dateFromJson(dateStr: String): Date {
        return format.parse(dateStr)
    }

    @ToJson
    fun dateToJson(date: Date): String {
        return format.format(date)
    }
}