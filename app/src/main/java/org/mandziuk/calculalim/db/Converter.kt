package org.mandziuk.calculalim.db

import androidx.room.TypeConverter
import java.time.Instant
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date

class Converter {

    @TypeConverter
    fun stringToDate(value : String) : Date{
        return Date.from(Instant.from(
            DateTimeFormatter.ofPattern("yyyy-MM-dd").parse(value + "T00:00:00Z")
        ));
    }

    @TypeConverter
    fun dateToLong(value: Date): Long {
        return value.time;
    }

    @TypeConverter
    fun longToDate(value: Long): Date {
        return Date(value);
    }
}