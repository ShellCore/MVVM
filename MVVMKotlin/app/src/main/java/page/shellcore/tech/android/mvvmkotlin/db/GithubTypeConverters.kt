package page.shellcore.tech.android.mvvmkotlin.db

import android.util.Log
import androidx.room.TypeConverter

object GithubTypeConverters {

    @TypeConverter
    @JvmStatic
    fun stringToIntList(data: String?): List<Int>? = data?.let {string ->
        string.split(",").mapNotNull {subString ->
            try {
                subString.toInt()
            } catch (ex: NumberFormatException) {
                Log.e("StringToIntList", "No puede convertir a número: ${ex.localizedMessage}")
                null
            }
        }
    }

    @TypeConverter
    @JvmStatic
    fun intListToString(ints: List<Int>?): String? = ints?.joinToString(",")
}