package studykotlin.dongnao.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import studykotlin.dongnao.api.User
import studykotlin.dongnao.repository.UserRepository

class MainViewModel : ViewModel() {
    val userLiveData = MutableLiveData<User>()

    private val userRepository by lazy { UserRepository() }

    fun getUser(name: String) {
        viewModelScope.launch {
            userLiveData.value = userRepository.getUser(name)
        }
    }

}