package studykotlin.dongnao.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.study.learnkotiln.R
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import studykotlin.dongnao.common.Event
import studykotlin.dongnao.common.LocalEventBus

class SharedFlowFragment : Fragment() {
    private var mTxtTime: TextView? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_sharedflow, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mTxtTime = view?.findViewById<TextView>(R.id.tv_time)


        lifecycleScope.launchWhenCreated {
            LocalEventBus.events.collect { event: Event ->
                mTxtTime?.text = event.time.toString()
            }
        }
    }
}