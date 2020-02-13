package page.shellcore.tech.android.mvvmkotlin.ui.user


import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.postDelayed
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import page.shellcore.tech.android.mvvmkotlin.AppExecutors
import page.shellcore.tech.android.mvvmkotlin.R
import page.shellcore.tech.android.mvvmkotlin.binding.FragmentDataBindingComponent
import page.shellcore.tech.android.mvvmkotlin.databinding.FragmentUserBinding
import page.shellcore.tech.android.mvvmkotlin.di.Injectable
import page.shellcore.tech.android.mvvmkotlin.ui.common.RepoListAdapter
import page.shellcore.tech.android.mvvmkotlin.ui.common.RetryCallback
import page.shellcore.tech.android.mvvmkotlin.utils.autoCleared
import javax.inject.Inject

class UserFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var appExecutors: AppExecutors

    var binding by autoCleared<FragmentUserBinding>()

    var dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)

    private val userViewModel: UserViewModel by viewModels {
        viewModelFactory
    }

    private val params by navArgs<UserFragmentArgs>()
    private var adapter by autoCleared<RepoListAdapter>()
    private var handler = Handler(Looper.getMainLooper())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val dataBinding = DataBindingUtil.inflate<FragmentUserBinding>(
            inflater,
            R.layout.fragment_user,
            container,
            false,
            dataBindingComponent
        )
        dataBinding.retryCallback = object : RetryCallback {
            override fun retry() {
                userViewModel.retry()
            }
        }

        binding = dataBinding
        sharedElementEnterTransition =
            TransitionInflater.from(context).inflateTransition(R.transition.move)

        handler.postDelayed(100) {
            startPostponedEnterTransition()
        }
        postponeEnterTransition()
        return dataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val params = UserFragmentArgs.fromBundle(arguments!!)
        userViewModel.setLogin(params.login)
        userViewModel.user.observe(viewLifecycleOwner, Observer { userResource ->
            binding.user = userResource.data
            binding.userResource = userResource
        })

        val rvAdapter = RepoListAdapter(
            dataBindingComponent,
            appExecutors,
            false
        ) { repo ->
            findNavController().navigate(
                UserFragmentDirections.actionUserFragmentToRepoFragment(
                    repo.owner.login,
                    repo.name
                )
            )
        }
        binding.recRepos.adapter = rvAdapter
        this.adapter = rvAdapter

        initRepoList()
    }

    private fun initRepoList() {
        userViewModel.repositories
            .observe(viewLifecycleOwner, Observer { repos ->
                adapter.submitList(repos?.data)
            })
    }
}
