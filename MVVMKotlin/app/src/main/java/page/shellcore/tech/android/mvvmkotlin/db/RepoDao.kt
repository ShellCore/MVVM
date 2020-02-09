package page.shellcore.tech.android.mvvmkotlin.db

import android.util.SparseIntArray
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import page.shellcore.tech.android.mvvmkotlin.model.Contributor
import page.shellcore.tech.android.mvvmkotlin.model.Repo
import page.shellcore.tech.android.mvvmkotlin.model.RepoSearchResult
import java.util.*

@Dao
abstract class RepoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(vararg repos: Repo)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertContributors(contributors: List<Contributor>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertRepos(repos: List<Repo>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun createRepoIfNotExist(repo: Repo): Long

    @Query("SELECT * FROM repo WHERE owner_login = :ownerLogin AND name = :name")
    abstract fun load(ownerLogin: String, name: String): LiveData<Repo>

    @Query("SELECT login, avatarUrl, repoName, repoOwner, contributions FROM contributor WHERE repoName = :name AND repoOwner = :repoOwner ORDER BY contributions DESC")
    abstract fun loadContributors(repoOwner: String, name: String): LiveData<List<Contributor>>

    @Query("SELECT * FROM repo WHERE owner_login = :ownerLogin ORDER BY stargazersCount DESC")
    abstract fun loadRepositories(ownerLogin: String): LiveData<List<Repo>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(result: RepoSearchResult)

    @Query("SELECT * FROM reposearchresult WHERE `query` = :query")
    abstract fun search(query: String): LiveData<RepoSearchResult>

    fun loadOrdered(reposIds: List<Int>): LiveData<List<Repo>> {
        val order = SparseIntArray()
        reposIds.withIndex()
            .forEach { order.put(it.value, it.index) }
        return Transformations.map(loadById(reposIds)) { repositories ->
            Collections.sort(repositories) { r1, r2 ->
                val pos1 = order.get(r1.id)
                val pos2 = order.get(r2.id)
                pos1 - pos2
            }
            repositories
        }
    }

    @Query("SELECT * FROM repo WHERE id in(:repoIds)")
    protected abstract fun loadById(repoIds: List<Int>): LiveData<List<Repo>>

    @Query("SELECT * FROM RepoSearchResult WHERE `query` = :query")
    abstract fun findSearchResult(query: String): RepoSearchResult?
}