package com.voidx.userlisting.data

import com.voidx.userlisting.data.repository.UserRepository
import com.voidx.userlisting.data.repository.local.UserLocalRepository
import com.voidx.userlisting.data.util.RxProvider
import org.koin.dsl.module

val dataModule = module {

    single<UserRepository> {
        UserLocalRepository()
    }

    factory<RxProvider> {
        RxProvider.Impl()
    }
}