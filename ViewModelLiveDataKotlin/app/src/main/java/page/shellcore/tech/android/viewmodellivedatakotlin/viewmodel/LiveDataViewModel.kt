package page.shellcore.tech.android.viewmodellivedatakotlin.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import page.shellcore.tech.android.viewmodellivedatakotlin.utils.User

class LiveDataViewModel : ViewModel() {

    private var usersLiveData: MutableLiveData<List<User>> = MutableLiveData()
    private val users: MutableList<User> = ArrayList()

    fun getUsers(): LiveData<List<User>> = usersLiveData

    fun addUser(user: User) {
        users.add(user)
        usersLiveData.value = users
    }
}