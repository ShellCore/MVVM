package page.shellcore.tech.android.mvvmkotlin.ui.search

import androidx.lifecycle.*
import androidx.lifecycle.Observer
import page.shellcore.tech.android.mvvmkotlin.model.Repo
import page.shellcore.tech.android.mvvmkotlin.repository.RepoReposity
import page.shellcore.tech.android.mvvmkotlin.repository.Resource
import page.shellcore.tech.android.mvvmkotlin.repository.Status
import page.shellcore.tech.android.mvvmkotlin.utils.AbsentLiveData
import java.util.*
import javax.inject.Inject

class SearchViewModel @Inject constructor(repoReposity: RepoReposity) : ViewModel() {

    private val query = MutableLiveData<String>()
    private val nextPageHandler = NextPageHandler(repoReposity)
    val queryLD: LiveData<String> = query

    val result: LiveData<Resource<List<Repo>>> = Transformations.switchMap(query) { search ->
        if (search.isNullOrEmpty()) {
            AbsentLiveData.create()
        } else {
            repoReposity.search(search)
        }
    }

    val loadMoreState: LiveData<LoadMoreState>
        get() = nextPageHandler.loadMoreState

    fun setQuery(originalInput: String) {
        val input = originalInput.toLowerCase(Locale.getDefault())
            .trim()
        if (input==query.value) {
            return
        }
        nextPageHandler.reset()
        query.value = input
    }

    fun loadNextPage() {
        query.value?.let {
            if (it.isNotBlank()) {
                nextPageHandler.queryNextPage(it)
            }
        }
    }

    fun refresh() {
        query.value?.let {
            query.value = it
        }
    }

    class LoadMoreState(val isRunning: Boolean, val errorMessage: String?) {

        private var handlerError: Boolean = false
        val errorMessageIfNotHandled: String?
            get() {
                if (handlerError) {
                    return null
                }
                handlerError = true
                return errorMessage
            }
    }

    class NextPageHandler(private val reposity: RepoReposity) : Observer<Resource<Boolean>> {

        private var nextPageLiveData: LiveData<Resource<Boolean>>? = null
        val loadMoreState = MutableLiveData<LoadMoreState>()
        private var query: String? = null
        private var _hasMore: Boolean = false
        val hasMore
            get() = _hasMore

        init {
            reset()
        }

        override fun onChanged(result: Resource<Boolean>?) {
            if (result == null) {
                reset()
            } else {
                when (result.status) {
                    Status.SUCCESS -> {
                        _hasMore = result.data == true
                        unregister()
                        loadMoreState.value = LoadMoreState(
                            isRunning = false,
                            errorMessage = null
                        )
                    }
                    Status.ERROR -> {
                        _hasMore = true
                        unregister()
                        loadMoreState.value = LoadMoreState(
                            isRunning = false,
                            errorMessage = result.message
                        )
                    }
                    Status.LOADING -> {
                    }
                }
            }
        }

        fun queryNextPage(query: String) {
            if (this.query == query) {
                return
            }
            unregister()
            this.query = query
            nextPageLiveData = reposity.searchNextPage(query)
            loadMoreState.value = LoadMoreState(
                isRunning = true,
                errorMessage = null
            )
            nextPageLiveData?.observeForever(this)
        }

        fun reset() {
            unregister()
            _hasMore = true
            loadMoreState.value = LoadMoreState(
                isRunning = false,
                errorMessage = null
            )
        }

        private fun unregister() {
            nextPageLiveData?.removeObserver(this)
            nextPageLiveData = null
            if (_hasMore) {
                query = null
            }
        }
    }
}