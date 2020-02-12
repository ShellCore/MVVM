package page.shellcore.tech.android.mvvmkotlin.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import page.shellcore.tech.android.mvvmkotlin.api.*
import page.shellcore.tech.android.mvvmkotlin.db.GithubDB
import page.shellcore.tech.android.mvvmkotlin.model.RepoSearchResult
import java.io.IOException
import java.lang.Exception

class FetchNextSearchPageTask constructor(
    private val query: String,
    private val githubApi: GithubApi,
    private val db: GithubDB
): Runnable {

    private val _liveData = MutableLiveData<Resource<Boolean>>()
    val liveData: LiveData<Resource<Boolean>> = _liveData

    override fun run() {
        var current = db.repoDao()
            .findSearchResult(query)
        if (current == null) {
            _liveData.postValue(null)
            return
        }
        val nextPage = current.next
        if (nextPage == null) {
            _liveData.postValue(Resource.success(false))
            return
        }
        val newValue = try {
            val response = githubApi.searchRepos(query, nextPage)
                .execute()
            val apiResponse = ApiResponse.create(response)
            when (apiResponse) {
                is ApiSuccessResponse -> {
                    val ids = arrayListOf<Int>()
                    ids.addAll(current.repoIds)
                    ids.addAll(apiResponse.body.items.map { it.id })
                    val merged = RepoSearchResult(apiResponse.nextPage, query, ids, apiResponse.body.totalCount)
                    try {
                        db.runInTransaction {
                            db.repoDao().insert(merged)
                            db.repoDao().insertRepos(apiResponse.body.items)
                        }
                    } catch (ex: Exception) {
                        Log.e("FetchNextSearchPageTask", "Error inserting ids: ${ex.localizedMessage}")
                        Resource.error(ex.message!!, true)
                    }
                    Resource.success(apiResponse.nextPage != null)
                }
                is ApiEmptyResponse -> Resource.success(false)
                is ApiErrorResponse -> Resource.error(apiResponse.errorMessage, true)
            }
        } catch (ex: IOException) {
            Log.e("FetchNextSearchPageTask", "Error creating resource: ${ex.localizedMessage}")
            Resource.error(ex.message!!, true)
        }

        _liveData.postValue(newValue)
    }

}