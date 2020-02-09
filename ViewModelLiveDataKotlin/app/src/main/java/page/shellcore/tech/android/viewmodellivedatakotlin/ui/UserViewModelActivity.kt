package page.shellcore.tech.android.viewmodellivedatakotlin.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_user_view_model.*
import page.shellcore.tech.android.viewmodellivedatakotlin.R
import page.shellcore.tech.android.viewmodellivedatakotlin.utils.User
import page.shellcore.tech.android.viewmodellivedatakotlin.viewmodel.UserViewModel

class UserViewModelActivity : AppCompatActivity() {

    private var users: ArrayList<User> = ArrayList()
    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_view_model)

        setupView()
    }

    private fun setupView() {
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        btnSave.setOnClickListener { addUser() }
        btnShow.setOnClickListener { showUsers() }
    }

    private fun addUser() {
        saveUser()
        clearData()
        showMessage("Usuario guardado")
    }

    private fun showUsers() {
        var text = ""
        users.forEach { text += "${it.name}, ${it.age} años\n" }
        txtUser.text = text

        var textInViewModel = ""
        userViewModel.users.forEach { textInViewModel += "$it\n" }
        txtUserViewModel.text = textInViewModel
    }

    private fun saveUser() {
        val user = User().apply {
            name = tilName.editText!!.text.toString()
            age = tilAge.editText!!.text.toString().toInt()
        }
        users.add(user)
        userViewModel.addUser(user)
    }

    private fun clearData() {
        tilName.editText!!.text.clear()
        tilAge.editText!!.text.clear()
    }

    private fun showMessage(message: String) {
        Snackbar.make(userViewModelContainer, message, Snackbar.LENGTH_SHORT)
            .show()
    }
}
