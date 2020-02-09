package page.shellcore.tech.android.mvvmkotlin.model


import com.google.gson.annotations.SerializedName

data class RepoSearchResponse(
    @SerializedName("items")
    val items: List<Repo>,
    @SerializedName("total_count")
    val totalCount: Int
) {
    var nextPage: Int? = null
}