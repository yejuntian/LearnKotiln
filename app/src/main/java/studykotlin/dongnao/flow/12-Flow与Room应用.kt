package studykotlin.dongnao.flow

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.study.learnkotiln.R
import kotlinx.coroutines.launch
import studykotlin.dongnao.adapter.UserListAdapter
import studykotlin.dongnao.viewmodel.UserViewModel

/**
 * 使用Flow与Room应用
 */

class RoomUserActivity : AppCompatActivity() {
    private var mUserId: EditText? = null
    private var mFirstName: EditText? = null
    private var mLastName: EditText? = null
    private var mAddUser: Button? = null
    private var mRecycleView: RecyclerView? = null
    private val userListAdapter by lazy { UserListAdapter() }

    //使用viewModel代理 创建对象
    private val userViewModel by viewModels<UserViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flow_room)
        initView()
        initListener()
        initData()
    }

    private fun initData() {
        lifecycleScope.launch {
            val listFlow = userViewModel.getAllUser()
            listFlow.collect {
                userListAdapter.setData(it)
            }
        }
    }

    private fun initView() {
        mUserId = findViewById<EditText>(R.id.edt_userId)
        mFirstName = findViewById<EditText>(R.id.edt_firstName)
        mLastName = findViewById<EditText>(R.id.edt_lastName)
        mAddUser = findViewById<Button>(R.id.btn_addUser)
        mRecycleView = findViewById<RecyclerView>(R.id.recycleView)

        mRecycleView?.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mRecycleView?.adapter = userListAdapter
    }

    private fun initListener() {
        mAddUser?.setOnClickListener {
            lifecycleScope.launch {
                val userId = mUserId?.text?.trim()?.toString()
                val firstName = mFirstName?.text?.trim()?.toString()
                val lastName = mFirstName?.text?.trim()?.toString()
                userViewModel?.insertUser(userId, firstName, lastName)
            }
        }
    }
}


