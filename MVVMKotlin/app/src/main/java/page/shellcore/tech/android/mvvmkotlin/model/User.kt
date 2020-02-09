package page.shellcore.tech.android.mvvmkotlin.model

import androidx.room.Entity
import com.google.gson.annotations.SerializedName

@Entity(primaryKeys = ["login"])
data class User(
    @SerializedName("avatar_url")
    val avatarUrl: String,
    @SerializedName("blog")
    val blog: String,
    @SerializedName("company")
    val company: Any,
    @SerializedName("login")
    val login: String,
    @SerializedName("name")
    val name: String
)