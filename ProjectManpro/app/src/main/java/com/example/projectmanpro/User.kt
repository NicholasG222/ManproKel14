package com.example.projectmanpro

data class User(
    var email: String?,
    var password: String?,
    var role: String? = "user"
)
