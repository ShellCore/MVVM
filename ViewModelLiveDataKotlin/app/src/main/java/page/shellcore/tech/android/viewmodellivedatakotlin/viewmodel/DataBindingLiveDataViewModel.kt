package page.shellcore.tech.android.viewmodellivedatakotlin.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import page.shellcore.tech.android.viewmodellivedatakotlin.utils.User

class DataBindingLiveDataViewModel: ViewModel() {

    var user: MutableLiveData<User> = MutableLiveData()

    fun setUser(user: User) {
        this.user.value = user
    }

    fun updateUser() {
        val user = User("Laura", 23)
        setUser(user)
    }
}