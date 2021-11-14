package com.voidx.userlisting.feature.profile.di

import com.voidx.userlisting.feature.profile.domain.CreateUserUseCase
import com.voidx.userlisting.feature.profile.domain.EditUserProfileUseCase
import com.voidx.userlisting.feature.profile.domain.UserValidator
import com.voidx.userlisting.feature.profile.presentation.UserProfileStateMapper
import com.voidx.userlisting.feature.profile.presentation.UserProfileViewModel
import com.voidx.userlisting.feature.profile.view.UserProfileFragment
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val userProfileModule = module {

    scope<UserProfileFragment> {

        scoped<UserValidator> {
            UserValidator.Impl()
        }

        scoped<EditUserProfileUseCase> {
            EditUserProfileUseCase.Impl(get(), get())
        }

        scoped<CreateUserUseCase> {
            CreateUserUseCase.Impl(get(), get())
        }

        scoped<UserProfileStateMapper> {
            UserProfileStateMapper.Impl()
        }

        viewModel {
            UserProfileViewModel(get(), get(), get(), get())
        }
    }
}