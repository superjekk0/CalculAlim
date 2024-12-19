package org.mandziuk.calculalim.db

import androidx.room.TypeConverter
import java.time.Instant
import java.time.format.DateTimeFormatter
import java.util.Date

class Converter {
    @TypeConverter
    fun dateToString(value : Date) : String{
        return DateTimeFormatter.ofPattern("yyyy-MM-dd").format(value.toInstant());
    }

    @TypeConverter
    fun stringToDate(value : String) : Date{
        return Date.from(Instant.from(
            DateTimeFormatter.ISO_INSTANT.parse(value + "T00:00:00Z")
        ));
    }
}