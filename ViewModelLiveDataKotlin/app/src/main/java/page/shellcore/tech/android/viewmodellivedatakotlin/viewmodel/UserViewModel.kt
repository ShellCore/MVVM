package page.shellcore.tech.android.viewmodellivedatakotlin.viewmodel

import androidx.lifecycle.ViewModel
import page.shellcore.tech.android.viewmodellivedatakotlin.utils.User

class UserViewModel(val users: MutableList<User> = ArrayList()) : ViewModel() {

    fun addUser(user: User) {
        users.add(user)
    }
}