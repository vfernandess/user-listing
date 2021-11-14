package com.voidx.userlisting.feature.profile.presentation

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.voidx.userlisting.data.model.User
import com.voidx.userlisting.data.util.RxProvider
import com.voidx.userlisting.feature.profile.domain.CreateUserUseCase
import com.voidx.userlisting.feature.profile.domain.EditUserProfileUseCase
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable

class UserProfileViewModel(
    private val createUserUseCase: CreateUserUseCase,
    private val editUserUseCase: EditUserProfileUseCase,
    private val userProfileStateMapper: UserProfileStateMapper,
    private val rxProvider: RxProvider,
    private val disposable: CompositeDisposable = CompositeDisposable()
) : ViewModel() {

    private var currentImage: String? = null
    private lateinit var user: User

    private val state = MutableLiveData<UserProfileState>()

    fun state(): LiveData<UserProfileState> = state

    fun save(name: String?, age: String?) {
        val request: Single<User> = if (this::user.isInitialized) {
            user.name = name
            user.age = age
            currentImage.let { user.avatar = it }
            editUserUseCase(user)
        } else {
            createUserUseCase(name, age, currentImage)
        }

        request
            .subscribeOn(rxProvider.provideIoScheduler())
            .observeOn(rxProvider.provideMainScheduler())
            .map { userProfileStateMapper(it, this::user.isInitialized) }
            .onErrorReturn { userProfileStateMapper(it) }
            .subscribe { value ->
                state.postValue(value)
            }
            .also { disposable.add(it) }
    }

    override fun onCleared() {
        disposable.clear()
        super.onCleared()
    }

    fun load(user: User?) {
        user?.let {
            this.user = it
            state.postValue(UserProfileState.UserLoaded(it))
        }
    }

    fun setImage(image: String?) {
        this.currentImage = image
    }
}