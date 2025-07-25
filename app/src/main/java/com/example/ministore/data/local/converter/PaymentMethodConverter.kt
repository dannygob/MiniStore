package com.example.ministore.data.local.converter

import androidx.room.TypeConverter
import java.util.Date

class PaymentMethodConverter {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}