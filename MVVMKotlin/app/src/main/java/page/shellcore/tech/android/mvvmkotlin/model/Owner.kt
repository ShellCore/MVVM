package page.shellcore.tech.android.mvvmkotlin.model


import com.google.gson.annotations.SerializedName

data class Owner(
    @field:SerializedName("login")
    val login: String,
    @field:SerializedName("url")
    val url: String
)