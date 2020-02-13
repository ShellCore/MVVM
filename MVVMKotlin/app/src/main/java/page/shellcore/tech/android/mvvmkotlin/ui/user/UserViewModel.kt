package page.shellcore.tech.android.mvvmkotlin.ui.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import page.shellcore.tech.android.mvvmkotlin.model.Repo
import page.shellcore.tech.android.mvvmkotlin.model.User
import page.shellcore.tech.android.mvvmkotlin.repository.RepoReposity
import page.shellcore.tech.android.mvvmkotlin.repository.Resource
import page.shellcore.tech.android.mvvmkotlin.repository.UserRepository
import page.shellcore.tech.android.mvvmkotlin.utils.AbsentLiveData
import javax.inject.Inject

class UserViewModel @Inject constructor(
    userRepository: UserRepository,
    repoReposity: RepoReposity
) : ViewModel() {
    private val _login = MutableLiveData<String>()
    val login: LiveData<String>
        get() = _login

    val repositories: LiveData<Resource<List<Repo>>> = Transformations.switchMap(_login) { login ->
        if (login == null) {
            AbsentLiveData.create()
        } else {
            repoReposity.loadRepos(login)
        }
    }

    val user: LiveData<Resource<User>> = Transformations.switchMap(_login) { login ->
        if (login == null) {
            AbsentLiveData.create()
        } else {
            userRepository.loadUser(login)
        }
    }

    fun setLogin(login: String?) {
        if (_login.value != login) {
            _login.value = login
        }
    }

    fun retry() {
        _login.value?.let {
            _login.value = it
        }
    }
}