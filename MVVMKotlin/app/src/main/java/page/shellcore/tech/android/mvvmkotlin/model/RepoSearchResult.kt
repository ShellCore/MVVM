package page.shellcore.tech.android.mvvmkotlin.model

import androidx.room.Entity
import androidx.room.TypeConverters
import page.shellcore.tech.android.mvvmkotlin.db.GithubTypeConverters

@Entity(primaryKeys = ["query"])
@TypeConverters(GithubTypeConverters::class)
class RepoSearchResult(
    val next: Int?,
    val query: String,
    val reporIds: List<Int>,
    val totalCount: Int
)