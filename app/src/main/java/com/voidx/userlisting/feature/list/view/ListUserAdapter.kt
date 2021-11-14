package com.voidx.userlisting.feature.list.view

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.voidx.userlisting.R
import com.voidx.userlisting.data.model.User
import com.voidx.userlisting.databinding.UserItemBinding

const val INSERT_ON_LAST_POSITION = -1

typealias OnUserClicked = ((User?) -> Unit)

class ListUserAdapter(
    private val onUserClicked: OnUserClicked?
) : RecyclerView.Adapter<ListUserViewHolder>() {

    private var items = mutableListOf<User>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListUserViewHolder {
        val binding = UserItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListUserViewHolder(binding, onUserClicked)
    }

    override fun onBindViewHolder(holder: ListUserViewHolder, position: Int) {
        holder.bind(items.getOrNull(position))
    }

    override fun getItemCount(): Int = items.size

    fun insert(position: Int = INSERT_ON_LAST_POSITION, user: User) {
        with(items) {
            if (position == INSERT_ON_LAST_POSITION) {
                val futureLastPosition = size
                items.add(user)
                notifyItemInserted(futureLastPosition)
            } else {
                items.add(position, user)
                notifyItemInserted(position)
            }
        }
    }

    fun changeUser(user: User) {
        val position = items.indexOfFirst { user.id == it.id }
        items[position] = user
        notifyItemChanged(position)
    }

    fun removeAt(position: Int): User =
        items
            .removeAt(position)
            .also {
                notifyItemRemoved(position)
            }
}

class ListUserViewHolder(
    private val binding: UserItemBinding,
    private val onUserClicked: OnUserClicked?
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(user: User?) {

        binding.root.setOnClickListener {
            onUserClicked?.invoke(user)
        }

        binding.name.text = user?.name
        binding.age.text = user?.age

        val uri = if (user?.avatar.isNullOrBlank())
            Uri.EMPTY
        else
            Uri.parse(user?.avatar)

        Glide
            .with(binding.avatar)
            .load(uri)
            .placeholder(R.drawable.ic_account_circle)
            .apply(RequestOptions.circleCropTransform())
            .into(binding.avatar)
    }
}