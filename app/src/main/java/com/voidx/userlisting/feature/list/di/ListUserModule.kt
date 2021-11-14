package com.voidx.userlisting.feature.list.di

import com.voidx.userlisting.feature.list.domain.DeleteUserUseCase
import com.voidx.userlisting.feature.list.domain.ListAllUsersUseCase
import com.voidx.userlisting.feature.list.presentation.ListUserStateMapper
import com.voidx.userlisting.feature.list.presentation.ListUserViewModel
import com.voidx.userlisting.feature.list.view.ListUserFragment
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val listUserModule = module {

    scope<ListUserFragment> {

        scoped<ListAllUsersUseCase> {
            ListAllUsersUseCase.Impl(get())
        }

        scoped<DeleteUserUseCase> {
            DeleteUserUseCase.Impl(get())
        }

        scoped<ListUserStateMapper> {
            ListUserStateMapper.Impl()
        }

        viewModel {
            ListUserViewModel(get(), get(), get())
        }
    }
}