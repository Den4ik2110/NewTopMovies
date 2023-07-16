package ru.netology.newtopmovies.database

import androidx.room.TypeConverter
import ru.netology.newtopmovies.data.SequelsAndPrequels


class SequelsAndPrequelsConverter {
    companion object {
        @TypeConverter
        @JvmStatic
        fun fromSequelsAndPrequels(sequelsAndPrequels: List<SequelsAndPrequels>?): String? {
            var stringSequelsAndPrequels = ""
            sequelsAndPrequels?.forEachIndexed { index, sequelsAndPrequelsOne ->
                stringSequelsAndPrequels += if (index < sequelsAndPrequels.size - 1) sequelsAndPrequelsOne.filmId.toString() + ", " else sequelsAndPrequelsOne.filmId.toString()
            }
            return stringSequelsAndPrequels.ifBlank { null }
        }

        @TypeConverter
        @JvmStatic
        fun toSequelsAndPrequels(data: String?): List<SequelsAndPrequels>? {
            val listSequelsAndPrequels = mutableListOf<SequelsAndPrequels>()
            return if (data == null) {
                null
            } else {
                data.trim().split(", ").forEach {
                    listSequelsAndPrequels.add(SequelsAndPrequels(it.toInt()))
                }
                listSequelsAndPrequels.toList()
            }
        }
    }
}