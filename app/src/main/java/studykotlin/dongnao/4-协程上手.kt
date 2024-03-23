package studykotlin.dongnao

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.study.learnkotiln.R
import com.study.learnkotiln.databinding.ActivityDatabingBinding
import studykotlin.dongnao.viewmodel.MainViewModel

class DataBingActivity : AppCompatActivity() {
    private var mainViewModel: MainViewModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        //使用dataBinding绑定ViewModel和lifecycle生命周期
        val binding = DataBindingUtil.setContentView<ActivityDatabingBinding>(
            this,
            R.layout.activity_databing
        )
        binding.viewModel = mainViewModel
        binding.lifecycleOwner = this

        binding.submitButton.also {
            it.text = "jack"
        }
        val submitButton = findViewById<TextView>(R.id.submitButton)
        submitButton.setOnClickListener {
            mainViewModel?.getUser("xxx")
        }
    }
}