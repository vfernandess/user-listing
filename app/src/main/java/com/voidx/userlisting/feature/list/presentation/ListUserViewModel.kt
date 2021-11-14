package com.voidx.userlisting.feature.list.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.voidx.userlisting.data.model.User
import com.voidx.userlisting.data.util.RxProvider
import com.voidx.userlisting.feature.list.domain.DeleteUserUseCase
import com.voidx.userlisting.feature.list.domain.ListAllUsersUseCase
import io.reactivex.rxjava3.disposables.CompositeDisposable
import timber.log.Timber

class ListUserViewModel(
    private val deleteUserUseCase: DeleteUserUseCase,
    private val listUserStateMapper: ListUserStateMapper,
    private val rxProvider: RxProvider
) : ViewModel() {

    private val state = MutableLiveData<ListUserState>()

    private val disposable = CompositeDisposable()

    fun state(): LiveData<ListUserState> = state

    fun delete(position: Int, user: User) {
        deleteUserUseCase(user)
            .subscribeOn(rxProvider.provideIoScheduler())
            .observeOn(rxProvider.provideMainScheduler())
            .subscribe(
                { hasNoMoreItems ->
                    listUserStateMapper.mapDelete(position, user, hasNoMoreItems)
                        .also { state.postValue(it) }
                },
                { Timber.e(it) }
            )
            .also { disposable.add(it) }
    }

    fun insert(position: Int, user: User) {
        val result = listUserStateMapper.mapInsertion(position, user)
        state.postValue(result)
    }

    fun modify(user: User) {
        val result = listUserStateMapper.mapModified(user)
        state.postValue(result)
    }

    override fun onCleared() {
        disposable.clear()
        super.onCleared()
    }
}