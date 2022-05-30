package com.ra.consumerapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.ra.consumerapp.R
import com.ra.consumerapp.data.UserData
import kotlinx.android.synthetic.main.item_row_user.view.*
import kotlin.collections.ArrayList

class ListUserAdapter(private val listUser: ArrayList<UserData>): RecyclerView.Adapter<ListUserAdapter.ListViewHolder>() {
    private var onItemClickCallback: OnItemClickCallback? = null
    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    fun setUser(users: ArrayList<UserData>){
        listUser.clear()
        listUser.addAll(users)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ListViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_row_user,viewGroup, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) = holder.bind(listUser[position])

    override fun getItemCount(): Int = listUser.size

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(user : UserData){
            with(itemView){
                Glide.with(itemView)
                        .load(user.photo)
                        .apply(RequestOptions().override(55,55))
                        .into(img_item_photo)

                tv_item_username.text = user.username
                tv_item_name.text = user.name

                itemView.setOnClickListener { onItemClickCallback?.onItemClicked(user) }
            }
        }
    }
    interface OnItemClickCallback {
        fun onItemClicked(data: UserData)
    }
}