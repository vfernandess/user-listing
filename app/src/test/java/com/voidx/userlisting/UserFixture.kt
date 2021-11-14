package com.voidx.userlisting

import com.voidx.userlisting.data.model.User
import java.util.*

object UserFixture {

    fun createUser() =
        User(
            id = UUID.randomUUID().toString(),
            name = "JohnDoe",
            age = "28",
            avatar = ""
        )
}