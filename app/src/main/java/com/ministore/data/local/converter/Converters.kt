package com.ministore.data.local.converter

import android.util.Log
import androidx.room.TypeConverter
import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

private const val TAG = "RoomConverters"

class DateTimeConverter {
    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

    @TypeConverter
    fun toLocalDateTime(value: String?): LocalDateTime? {
        if (value == null) return null
        return try {
            LocalDateTime.parse(value, formatter)
        } catch (e: DateTimeParseException) {
            Log.e(TAG, "Error parsing datetime: $value", e)
            null
        }
    }

    @TypeConverter
    fun fromLocalDateTime(date: LocalDateTime?): String? {
        if (date == null) return null
        return try {
            date.format(formatter)
        } catch (e: Exception) {
            Log.e(TAG, "Error formatting datetime: $date", e)
            null
        }
    }
}

class StringListConverter {
    @TypeConverter
    fun fromString(value: String?): List<String> {
        if (value.isNullOrBlank()) return emptyList()
        return try {
            value.split(",")
                .map { it.trim() }
                .filter { it.isNotBlank() }
        } catch (e: Exception) {
            Log.e(TAG, "Error converting string to list: $value", e)
            emptyList()
        }
    }

    @TypeConverter
    fun toString(list: List<String>?): String {
        if (list.isNullOrEmpty()) return ""
        return try {
            list.filter { it.isNotBlank() }
                .joinToString(",")
        } catch (e: Exception) {
            Log.e(TAG, "Error converting list to string: $list", e)
            ""
        }
    }
}

class BigDecimalConverter {
    @TypeConverter
    fun fromString(value: String?): BigDecimal {
        if (value.isNullOrBlank()) return BigDecimal.ZERO
        return try {
            BigDecimal(value)
        } catch (e: NumberFormatException) {
            Log.e(TAG, "Error converting string to BigDecimal: $value", e)
            BigDecimal.ZERO
        }
    }

    @TypeConverter
    fun toString(value: BigDecimal?): String {
        return value?.toString() ?: "0"
    }
} 