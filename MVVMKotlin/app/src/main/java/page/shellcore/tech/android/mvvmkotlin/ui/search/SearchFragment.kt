package page.shellcore.tech.android.mvvmkotlin.ui.search


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import page.shellcore.tech.android.mvvmkotlin.AppExecutors

import page.shellcore.tech.android.mvvmkotlin.R
import page.shellcore.tech.android.mvvmkotlin.binding.FragmentDataBindingComponent
import page.shellcore.tech.android.mvvmkotlin.databinding.FragmentSearchBinding
import page.shellcore.tech.android.mvvmkotlin.di.Injectable
import page.shellcore.tech.android.mvvmkotlin.ui.common.RepoListAdapter
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


}
