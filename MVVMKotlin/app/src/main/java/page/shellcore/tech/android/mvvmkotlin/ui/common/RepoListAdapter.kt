package page.shellcore.tech.android.mvvmkotlin.ui.common

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import page.shellcore.tech.android.mvvmkotlin.AppExecutors
import page.shellcore.tech.android.mvvmkotlin.R
import page.shellcore.tech.android.mvvmkotlin.databinding.ItemRepoBinding
import page.shellcore.tech.android.mvvmkotlin.model.Repo

class RepoListAdapter(
    private val dataBindingComponent: DataBindingComponent,
    appExecutors: AppExecutors,
    private val showFullName: Boolean,
    private val repoClickCallback: ((Repo) -> Unit)?

) : DataBoundListAdapter<Repo, ItemRepoBinding>(
    appExecutors = appExecutors,
    diffCallback = object : DiffUtil.ItemCallback<Repo>() {
        override fun areItemsTheSame(oldItem: Repo, newItem: Repo): Boolean =
            oldItem.owner == newItem.owner
                    && oldItem.name == newItem.name

        override fun areContentsTheSame(oldItem: Repo, newItem: Repo): Boolean =
            oldItem.description == newItem.description
                    && oldItem.stargazersCount == newItem.stargazersCount

    }
) {
    override fun createBinding(parent: ViewGroup): ItemRepoBinding {
        val binding = DataBindingUtil.inflate<ItemRepoBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_repo,
            parent,
            false,
            dataBindingComponent
        )
        binding.showFullName = showFullName
        binding.root
            .setOnClickListener {
                binding.repo?.let {
                    repoClickCallback?.invoke(it)
                }
            }
        return binding
    }

    override fun bind(binding: ItemRepoBinding, item: Repo) {
        binding.repo = item
    }
}