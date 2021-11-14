package com.voidx.userlisting

import com.voidx.userlisting.data.dataModule
import com.voidx.userlisting.feature.list.di.listUserModule
import com.voidx.userlisting.feature.profile.di.userProfileModule

val appModule = listOf(dataModule, listUserModule, userProfileModule)