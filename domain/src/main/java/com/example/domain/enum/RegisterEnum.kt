package com.example.domain.enum

enum class RegisterEnum {
    EMAIL {
        override fun toLocalization(): String = "Email"
    },
    Password {
        override fun toLocalization(): String = "Password"
    },
    Name {
        override fun toLocalization(): String = "User Name"
    },
    Address {
        override fun toLocalization(): String = "Address"
    };

    abstract fun toLocalization(): String
}