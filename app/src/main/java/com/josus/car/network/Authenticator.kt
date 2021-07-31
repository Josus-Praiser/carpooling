package com.josus.car.network

interface Authenticator {

    suspend fun signInWithMail(email:String,password:String)

    class FirebaseAuthentication:Authenticator{
        override suspend fun signInWithMail(email: String, password: String) {

        }
    }

}