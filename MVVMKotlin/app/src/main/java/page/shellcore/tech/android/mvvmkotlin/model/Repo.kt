package page.shellcore.tech.android.mvvmkotlin.model


import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import com.google.gson.annotations.SerializedName

@Entity(
    indices = [
        Index("id"),
        Index("owner_login")
    ],
    primaryKeys = [
        "name",
        "owner_login"
    ]
)
data class Repo(
    @field:SerializedName("description")
    val description: String,
    @field:SerializedName("full_name")
    val fullName: String,
    @field:SerializedName("id")
    val id: Int,
    @field:SerializedName("name")
    val name: String,
    @field:SerializedName("owner")
    @field:Embedded(prefix = "owner_")
    val owner: Owner,
    @SerializedName("stargazers_count")
    val stargazersCount: Int
)