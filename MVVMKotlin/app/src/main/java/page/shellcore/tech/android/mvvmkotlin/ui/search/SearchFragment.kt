package page.shellcore.tech.android.mvvmkotlin.ui.search


import android.content.Context
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import page.shellcore.tech.android.mvvmkotlin.AppExecutors
import page.shellcore.tech.android.mvvmkotlin.R
import page.shellcore.tech.android.mvvmkotlin.binding.FragmentDataBindingComponent
import page.shellcore.tech.android.mvvmkotlin.databinding.FragmentSearchBinding
import page.shellcore.tech.android.mvvmkotlin.di.Injectable
import page.shellcore.tech.android.mvvmkotlin.ui.common.RepoListAdapter
import page.shellcore.tech.android.mvvmkotlin.ui.common.RetryCallback
import page.shellcore.tech.android.mvvmkotlin.utils.autoCleared
import javax.inject.Inject

class SearchFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var appExecutors: AppExecutors

    var dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)

    var binding by autoCleared<FragmentSearchBinding>()

    var adapter by autoCleared<RepoListAdapter>()

    val searchViewModel: SearchViewModel by viewModels {
        viewModelFactory
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_search,
            container,
            false,
            dataBindingComponent
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.lifecycleOwner = viewLifecycleOwner
        initRecyclerView()
        val rvAdapter = RepoListAdapter(
            dataBindingComponent,
            appExecutors,
            true
        ) { repo ->
            findNavController().navigate(
                SearchFragmentDirections.actionSearchFragmentToRepoFragment(
                    repo.name,
                    repo.owner.login
                )
            )
        }
        binding.query = searchViewModel.queryLD
        binding.reposSearched.adapter = rvAdapter
        adapter = rvAdapter

        initSearchInputListener()

        binding.callback = object : RetryCallback {
            override fun retry() {
                searchViewModel.refresh()
            }
        }
    }

    private fun initSearchInputListener() {
        binding.edtSearch
            .setOnEditorActionListener { view, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    doSearch(view)
                    true
                } else {
                    false
                }
            }
        binding.edtSearch
            .setOnKeyListener { view, keyCode, event ->
                if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    doSearch(view)
                    true
                } else {
                    false
                }
            }
    }

    private fun doSearch(view: View) {
        val query = binding.edtSearch.text.toString()
        dismissKeyboard(view.windowToken)
        searchViewModel.setQuery(query)
    }

    private fun dismissKeyboard(windowToken: IBinder) {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(windowToken, 0)
    }

    private fun initRecyclerView() {
        binding.reposSearched.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val lastPosition = layoutManager.findLastVisibleItemPosition()
                if (lastPosition == (adapter.itemCount - 1)) {
                    searchViewModel.loadNextPage()
                }
            }
        })
        binding.searchResult = searchViewModel.result
        searchViewModel.result
            .observe(viewLifecycleOwner, Observer {
                adapter.submitList(it?.data)
            })

        searchViewModel.loadMoreState
            .observe(viewLifecycleOwner, Observer { loadingMore ->
                if (loadingMore == null) {
                    binding.loadingMore = false
                } else {
                    binding.loadingMore = loadingMore.isRunning
                    val error = loadingMore.errorMessageIfNotHandled
                    if (error != null) {
                        Log.d("SearchFragment", "Error: $error")
                    }
                }
            })
    }
}
