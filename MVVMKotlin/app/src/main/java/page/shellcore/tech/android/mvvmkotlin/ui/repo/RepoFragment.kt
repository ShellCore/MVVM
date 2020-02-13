package page.shellcore.tech.android.mvvmkotlin.ui.repo


import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import page.shellcore.tech.android.mvvmkotlin.databinding.FragmentRepoBinding
import page.shellcore.tech.android.mvvmkotlin.di.Injectable
import page.shellcore.tech.android.mvvmkotlin.ui.common.RetryCallback
import page.shellcore.tech.android.mvvmkotlin.utils.autoCleared
import javax.inject.Inject

class RepoFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    val repoViewModel: RepoViewModel by viewModels {
        viewModelFactory
    }

    @Inject
    lateinit var appExecutors: AppExecutors

    var dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)

    var binding by autoCleared<FragmentRepoBinding>()

    private val params by navArgs<RepoFragmentArgs>()
    private var adapter by autoCleared<ContributorAdapter>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val dataBinding = DataBindingUtil.inflate<FragmentRepoBinding>(
            inflater,
            R.layout.fragment_repo,
            container,
            false
        )
        dataBinding.retryCallback = object : RetryCallback {
            override fun retry() {
                repoViewModel.retry()
            }
        }

        binding = dataBinding
        sharedElementEnterTransition = TransitionInflater.from(context)
            .inflateTransition(R.transition.move)

        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val params = RepoFragmentArgs.fromBundle(arguments!!)
        repoViewModel.setId(params.owner, params.name)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.repo = repoViewModel.repo
        val adapter = ContributorAdapter(dataBindingComponent, appExecutors) { contributor ->
            findNavController().navigate(
                RepoFragmentDirections.actionRepoFragmentToUserFragment(
                    contributor.login,
                    contributor.avatarUrl
                )
            )
        }
        this.adapter = adapter
        binding.recContributors.adapter = adapter
        postponeEnterTransition()
        binding.recContributors.viewTreeObserver.addOnPreDrawListener {
            startPostponedEnterTransition()
            true
        }
        initContributorList(repoViewModel)
    }

    private fun initContributorList(viewModel: RepoViewModel) {
        viewModel.contributors
            .observe(viewLifecycleOwner, Observer { listResource ->
                if (listResource?.data != null) {
                    adapter.submitList(listResource.data)
                } else {
                    adapter.submitList(emptyList())
                }
            })
    }
}
