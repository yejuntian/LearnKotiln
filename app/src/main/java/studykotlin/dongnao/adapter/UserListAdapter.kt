package studykotlin.dongnao.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.study.learnkotiln.R
import studykotlin.dongnao.db.UserEntity

class UserListAdapter : RecyclerView.Adapter<UserListViewHolder>() {
    private val userList = mutableListOf<UserEntity>()

    fun setData(list: List<UserEntity>) {
        if (list.isNotEmpty()) {
            userList.clear()
            userList.addAll(list)
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserListViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_userlist_layout, parent, false)
        return UserListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: UserListViewHolder, position: Int) {
        val userEntity = userList[position]
        holder.txtUserName.text =
            "${userEntity.userId} ${userEntity.firstName} ${userEntity.lastName}"
    }
}

class UserListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val txtUserName: TextView = itemView.findViewById<TextView>(R.id.tv_userName)
}