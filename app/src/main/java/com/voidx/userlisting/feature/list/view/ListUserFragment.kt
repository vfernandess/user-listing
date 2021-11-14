package com.voidx.userlisting.feature.list.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentResultListener
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_LONG
import com.google.android.material.snackbar.Snackbar
import com.voidx.userlisting.R
import com.voidx.userlisting.data.model.User
import com.voidx.userlisting.databinding.FragmentListUserBinding
import com.voidx.userlisting.feature.list.presentation.ListUserState
import com.voidx.userlisting.feature.list.presentation.ListUserViewModel
import com.voidx.userlisting.feature.profile.view.EXTRA_USER
import com.voidx.userlisting.feature.profile.view.REQUEST_KEY_CREATE_PROFILE
import com.voidx.userlisting.feature.profile.view.REQUEST_KEY_EDIT_PROFILE
import com.voidx.userlisting.feature.profile.view.UserProfileFragment
import org.koin.android.scope.AndroidScopeComponent
import org.koin.androidx.scope.fragmentScope
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.scope.Scope

private const val NO_DRAGGING = 0

class ListUserFragment : Fragment(), AndroidScopeComponent, FragmentResultListener {

    override val scope: Scope by fragmentScope()

    private var binding: FragmentListUserBinding? = null

    val viewModel: ListUserViewModel by viewModel()

    private var listUserAdapter: ListUserAdapter? = null

    private val callback =
        object : ItemTouchHelper.SimpleCallback(NO_DRAGGING, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean =
                false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                listUserAdapter?.removeAt(position)?.run {
                    viewModel.delete(position, this)
                }
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListUserBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.fab?.setOnClickListener {
            showUserProfile()
        }

        binding?.list?.apply {
            listUserAdapter = ListUserAdapter(::showUserProfile)
            this.adapter = listUserAdapter

            val helper = ItemTouchHelper(callback)
            helper.attachToRecyclerView(this)
        }

        viewModel.state().observe({ lifecycle }) { state ->

            when (state) {

                is ListUserState.UserDeleted ->
                    showUndoUserDeleteMessage(state.position, state.user, state.hasNoMoreItems)

                is ListUserState.UserInserted ->
                    addUser(state.position, state.user)

                is ListUserState.UserModified ->
                    onUserChanged(state.user)

            }
        }
    }

    private fun showUserProfile(user: User? = null) {
        val requestKey = if (user == null) REQUEST_KEY_CREATE_PROFILE else REQUEST_KEY_EDIT_PROFILE
        childFragmentManager.setFragmentResultListener(requestKey, this, this)
        UserProfileFragment.show(childFragmentManager, user)
    }

    private fun showUndoUserDeleteMessage(
        userDeletedPosition: Int,
        user: User,
        hasNoMoreItems: Boolean
    ) {
        binding?.let {
            Snackbar
                .make(it.fab, R.string.undo_user_delete, LENGTH_LONG)
                .setAction(R.string.action_undo) {
                    viewModel.insert(userDeletedPosition, user)
                }
                .show()
        }

        if (hasNoMoreItems) {
            showEmptyState()
        }
    }

    private fun showEmptyState() {
        binding?.list?.visibility = GONE
        binding?.emptyState?.visibility = VISIBLE
    }

    private fun addUser(position: Int, user: User) {
        binding?.list?.visibility = VISIBLE
        binding?.emptyState?.visibility = GONE

        listUserAdapter?.insert(position, user)
    }

    private fun onUserChanged(user: User) {
        listUserAdapter?.changeUser(user)
    }

    override fun onFragmentResult(requestKey: String, result: Bundle) {
        if (REQUEST_KEY_CREATE_PROFILE == requestKey) {
            result.getParcelable<User>(EXTRA_USER)?.let {
                viewModel.insert(INSERT_ON_LAST_POSITION, it)
            }
        }

        if (REQUEST_KEY_EDIT_PROFILE == requestKey) {
            result.getParcelable<User>(EXTRA_USER)?.let {
                viewModel.modify(it)
            }
        }
    }
}