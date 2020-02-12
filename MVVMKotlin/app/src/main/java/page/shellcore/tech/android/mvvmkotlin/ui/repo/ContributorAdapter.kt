package page.shellcore.tech.android.mvvmkotlin.ui.repo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import page.shellcore.tech.android.mvvmkotlin.AppExecutors
import page.shellcore.tech.android.mvvmkotlin.R
import page.shellcore.tech.android.mvvmkotlin.databinding.ItemContributorBinding
import page.shellcore.tech.android.mvvmkotlin.model.Contributor
import page.shellcore.tech.android.mvvmkotlin.ui.common.DataBoundListAdapter

class ContributorAdapter(
    private var dataBindingComponent: DataBindingComponent,
    appExecutors: AppExecutors,
    private val callback: ((Contributor)->Unit)?
): DataBoundListAdapter<Contributor, ItemContributorBinding> (
    appExecutors = appExecutors,
    diffCallback = object : DiffUtil.ItemCallback<Contributor>() {
        override fun areItemsTheSame(oldItem: Contributor, newItem: Contributor): Boolean =
            oldItem.login == newItem.login

        override fun areContentsTheSame(oldItem: Contributor, newItem: Contributor): Boolean =
            oldItem.avatarUrl == newItem.avatarUrl
                    && oldItem.contributions == newItem.contributions

    }
) {

    override fun createBinding(parent: ViewGroup): ItemContributorBinding {
        val binding = DataBindingUtil.inflate<ItemContributorBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_contributor,
            parent,
            false,
            dataBindingComponent
        )
        binding.root.setOnClickListener {
            binding.contributor?.let {
                callback?.invoke(it)
            }
        }
        return binding
    }

    override fun bind(binding: ItemContributorBinding, item: Contributor) {
        binding.contributor = item
    }
}