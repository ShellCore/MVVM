package page.shellcore.tech.android.viewmodellivedatakotlin.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import page.shellcore.tech.android.viewmodellivedatakotlin.R
import page.shellcore.tech.android.viewmodellivedatakotlin.databinding.ActivityDataBindingBinding
import page.shellcore.tech.android.viewmodellivedatakotlin.utils.User

class DataBindingActivity : AppCompatActivity() {

    lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityDataBindingBinding = DataBindingUtil.setContentView(this@DataBindingActivity, R.layout.activity_data_binding)
        user = User("Luz", 30)

        binding.usr = user
    }
}
