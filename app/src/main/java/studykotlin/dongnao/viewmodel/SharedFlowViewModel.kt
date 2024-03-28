package studykotlin.dongnao.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import studykotlin.dongnao.common.Event
import studykotlin.dongnao.common.LocalEventBus

class SharedFlowViewModel : ViewModel() {
    private var job: Job? = null

    fun startRefresh() {
        // 如果已经有一个协程在运行，先取消它
        if (job?.isActive == true) {
            job?.cancel()
        }
        job = viewModelScope.launch(Dispatchers.IO) {
            while (true) {
                LocalEventBus.postEvent(Event(System.currentTimeMillis()))
            }
        }
    }

    fun stopRefresh() {
        job?.cancel()
    }
}