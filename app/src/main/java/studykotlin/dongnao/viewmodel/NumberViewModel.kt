package studykotlin.dongnao.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class NumberViewModel : ViewModel() {
    var numberStateFlow = MutableStateFlow<Int>(0)

    fun increment() {
        numberStateFlow.value++
    }

    fun decrement() {
        numberStateFlow.value--
    }
}