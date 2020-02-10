package page.shellcore.tech.android.mvvmkotlin.db

import androidx.room.Database
import androidx.room.RoomDatabase
import page.shellcore.tech.android.mvvmkotlin.model.Contributor
import page.shellcore.tech.android.mvvmkotlin.model.Repo
import page.shellcore.tech.android.mvvmkotlin.model.RepoSearchResult
import page.shellcore.tech.android.mvvmkotlin.model.User

@Database(
    entities = [
        User::class,
        Repo::class,
        Contributor::class,
        RepoSearchResult::class
    ],
    version = 1
)
abstract class GithubDB : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun repoDao(): RepoDao
}