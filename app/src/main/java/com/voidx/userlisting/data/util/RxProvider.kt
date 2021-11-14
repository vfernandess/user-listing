package com.voidx.userlisting.data.util

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.schedulers.Schedulers.io

interface RxProvider {

    fun provideIoScheduler(): Scheduler
    fun provideMainScheduler(): Scheduler

    class Impl : RxProvider {
        override fun provideIoScheduler(): Scheduler = io()
        override fun provideMainScheduler(): Scheduler = mainThread()
    }
}