package page.shellcore.tech.android.mvvmkotlin.repository

import page.shellcore.tech.android.mvvmkotlin.AppExecutors
import page.shellcore.tech.android.mvvmkotlin.api.GithubApi
import page.shellcore.tech.android.mvvmkotlin.db.UserDao
import page.shellcore.tech.android.mvvmkotlin.model.User
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val appExecutors: AppExecutors,
    private val userDao: UserDao,
    private val githubApi: GithubApi
) {

    fun loadUser(login: String) =
        object : NetworkBoundResource<User, User>(appExecutors) {
            override fun saveCallResult(item: User) = userDao.insert(item)

            override fun shouldFetch(data: User?) = data == null

            override fun loadFromDb() = userDao.findByLogin(login)

            override fun createCall() = githubApi.getUser(login)

        }.asLiveData()
}