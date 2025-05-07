package com.example.trackr

object UserDataManager {
    var username: String? = null
    var password: String? = null
    var email: String? = null
    var mobileNumber: String? = null

    fun registerUser(username: String, password: String, email: String, mobileNumber: String) {
        this.username = username
        this.password = password
        this.email = email
        this.mobileNumber = mobileNumber
    }

    fun validateLogin(username: String, password: String): Boolean {
        return this.username == username && this.password == password
    }
}