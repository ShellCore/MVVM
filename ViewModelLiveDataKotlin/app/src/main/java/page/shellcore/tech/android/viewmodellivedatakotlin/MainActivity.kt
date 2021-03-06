package page.shellcore.tech.android.viewmodellivedatakotlin

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import page.shellcore.tech.android.viewmodellivedatakotlin.ui.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupView()
    }

    private fun setupView() {
        btnViewModel.setOnClickListener {
            startActivity(
                Intent(
                    applicationContext,
                    ViewModelActivity::class.java
                )
            )
        }
        btnUserViewModel.setOnClickListener {
            startActivity(
                Intent(
                    applicationContext,
                    UserViewModelActivity::class.java
                )
            )
        }

        btnLiveData.setOnClickListener {
            startActivity(
                Intent(
                    applicationContext,
                    LiveDataActivity::class.java
                )
            )
        }

        btnDataBinding.setOnClickListener {
            startActivity(
                Intent(
                    applicationContext,
                    DataBindingActivity::class.java
                )
            )
        }
        btnDataBindingLiveData.setOnClickListener {
            startActivity(
                Intent(
                    applicationContext,
                    DataBindingLiveDataActivity::class.java
                )
            )
        }
    }

}
