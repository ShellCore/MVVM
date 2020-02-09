package page.shellcore.tech.android.viewmodellivedatakotlin.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_live_data.*
import page.shellcore.tech.android.viewmodellivedatakotlin.R
import page.shellcore.tech.android.viewmodellivedatakotlin.utils.User
import page.shellcore.tech.android.viewmodellivedatakotlin.viewmodel.LiveDataViewModel

class LiveDataActivity : AppCompatActivity() {

    private var number: Int = 0
    private lateinit var viewModel: LiveDataViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_live_data)

        setupView()
    }

    private fun setupView() {
        viewModel = ViewModelProvider(this).get(LiveDataViewModel::class.java)
        btnSave.setOnClickListener { saveUserByPos(number++) }
        setupObserver()
    }

    private fun setupObserver() {
        val listObserver = Observer<List<User>> {
            var text = ""
            it.forEach { text += "$it\n" }
            txtLiveData.text = text
        }

        // Nos permite suscribir nuestro Observer al Observable
        viewModel.getUsers().observe(this, listObserver)
    }

    private fun saveUserByPos(number: Int) {
        when (number) {
            0 -> saveUser("Alberto", 30)
            1 -> saveUser("María", 25)
            2 -> saveUser("Cesar", 32)
        }
    }

    private fun saveUser(name: String, age: Int) {
        val user = User(name, age)
        viewModel.addUser(user)
    }
}
