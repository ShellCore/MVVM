package page.shellcore.tech.android.mvvmkotlin.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import page.shellcore.tech.android.mvvmkotlin.repository.RepoReposity
import page.shellcore.tech.android.mvvmkotlin.repository.Resource
import page.shellcore.tech.android.mvvmkotlin.repository.Status
import javax.inject.Inject

class SearchViewModel @Inject constructor(repoReposity: RepoReposity) : ViewModel() {

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
                    Status.LOADING -> {}
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

        private fun reset() {
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