package com.example.projectmanpro

data class AdminAccessRequests(
    var email: String?,
    var role: String? = "Dosen",
    var catatan: String?
)