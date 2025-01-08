package org.mandziuk.calculalim.db

import androidx.room.TypeConverter
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.Date

class Converter {

    @TypeConverter
    fun stringToInstant(value : String) : Instant{
        return Instant.parse("${value}T00:00:00Z");
    }

    @TypeConverter
    fun instantToString(value: Instant) : String{
        return SimpleDateFormat("yyyy-MM-dd").format(Date.from(value));
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