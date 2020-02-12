package page.shellcore.tech.android.mvvmkotlin.repository

import androidx.lifecycle.LiveData
import page.shellcore.tech.android.mvvmkotlin.AppExecutors
import page.shellcore.tech.android.mvvmkotlin.api.ApiResponse
import page.shellcore.tech.android.mvvmkotlin.api.GithubApi
import page.shellcore.tech.android.mvvmkotlin.db.GithubDB
import page.shellcore.tech.android.mvvmkotlin.db.RepoDao
import page.shellcore.tech.android.mvvmkotlin.model.Contributor
import page.shellcore.tech.android.mvvmkotlin.model.Owner
import page.shellcore.tech.android.mvvmkotlin.model.Repo
import page.shellcore.tech.android.mvvmkotlin.utils.RateLimiter
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RepoReposity @Inject constructor(
    private val appExecutors: AppExecutors,
    private val db: GithubDB,
    private val repoDao: RepoDao,
    private val githubApi: GithubApi
) {

    private val repoListRateLimiter = RateLimiter<String>(10, TimeUnit.MINUTES)

    fun loadRepos(owner: String): LiveData<Resource<List<Repo>>> =
        object : NetworkBoundResource<List<Repo>, List<Repo>>(appExecutors) {
            override fun saveCallResult(item: List<Repo>) = repoDao.insertRepos(item)

            override fun shouldFetch(data: List<Repo>?): Boolean =
                data == null || data.isEmpty() || repoListRateLimiter.shouldFetch(owner)

            override fun loadFromDb(): LiveData<List<Repo>> = repoDao.loadRepositories(owner)

            override fun createCall(): LiveData<ApiResponse<List<Repo>>> = githubApi.getRepos(owner)

            override fun onFetchFailed() = repoListRateLimiter.reset(owner)
        }.asLiveData()

    fun loadRepo(owner: String, name: String): LiveData<Resource<Repo>> =
        object : NetworkBoundResource<Repo, Repo>(appExecutors) {
            override fun saveCallResult(item: Repo) = repoDao.insert(item)

            override fun shouldFetch(data: Repo?): Boolean = data == null

            override fun loadFromDb(): LiveData<Repo> = repoDao.load(owner, name)

            override fun createCall(): LiveData<ApiResponse<Repo>> = githubApi.getRepo(owner, name)
        }.asLiveData()

    fun loadControbutors(owner: String, name: String): LiveData<Resource<List<Contributor>>> =
        object : NetworkBoundResource<List<Contributor>, List<Contributor>>(appExecutors) {
            override fun saveCallResult(item: List<Contributor>) {
                item.forEach {
                    it.repoName = name
                    it.repoOwner = owner
                }
                db.runInTransaction {
                    repoDao.createRepoIfNotExist(
                        Repo(
                            id = Repo.UNKNOWN_ID,
                            name = name,
                            fullName = "$owner/$name",
                            description = "",
                            owner = Owner(owner, null),
                            stargazersCount = 0
                        )
                    )
                    repoDao.insertContributors(item)
                }
            }

            override fun shouldFetch(data: List<Contributor>?): Boolean =
                data == null || data.isEmpty()

            override fun loadFromDb(): LiveData<List<Contributor>> =
                repoDao.loadContributors(owner, name)

            override fun createCall(): LiveData<ApiResponse<List<Contributor>>> =
                githubApi.getContributors(owner, name)

        }.asLiveData()

    fun searchNextPage(query: String): LiveData<Resource<Boolean>> {
        val fetchNextSearchPageTask = FetchNextSearchPageTask(query, githubApi, db)
        appExecutors.networkIO()
            .execute(fetchNextSearchPageTask)
        return fetchNextSearchPageTask.liveData
    }
}