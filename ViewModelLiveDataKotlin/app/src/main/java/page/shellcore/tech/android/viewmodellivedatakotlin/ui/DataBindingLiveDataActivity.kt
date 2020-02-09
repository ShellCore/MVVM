package page.shellcore.tech.android.viewmodellivedatakotlin.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import page.shellcore.tech.android.viewmodellivedatakotlin.R
import page.shellcore.tech.android.viewmodellivedatakotlin.databinding.ActivityDataBindingLiveDataBinding
import page.shellcore.tech.android.viewmodellivedatakotlin.utils.User
import page.shellcore.tech.android.viewmodellivedatakotlin.viewmodel.DataBindingLiveDataViewModel

class DataBindingLiveDataActivity : AppCompatActivity() {

    lateinit var viewModel: DataBindingLiveDataViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityDataBindingLiveDataBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_data_binding_live_data)
        binding.lifecycleOwner = this

        viewModel = ViewModelProvider(this).get(DataBindingLiveDataViewModel::class.java)

        binding.vm = viewModel

        val user = User("Cesar", 32)
        viewModel.setUser(user)
    }
}
