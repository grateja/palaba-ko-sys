package com.csi.palabakosys.app.preferences.user

import android.content.Context
import com.csi.palabakosys.room.entities.EntityUser
import com.csi.palabakosys.room.repository.UserRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository

@Inject
constructor(
    @ApplicationContext context: Context,
    private val userRepository: UserRepository
) {
    private val pref = context.getSharedPreferences("auth_settings", Context.MODE_PRIVATE)
//    val currentUser = userRepository.getCurrentUserByEmail(pref.getString("email", null))

    fun getLastEmail() : String? {
        return pref.getString("email", null)
    }

    suspend fun oneTimeLogin(email: String?, password: String?) : EntityUser? {
        if(email == null || password == null) return null
        with(pref.edit()) {
            this.putString("email", email)
            this.commit()
        }
        return userRepository.getByEmailAndPassword(email, password)
    }

    suspend fun login(email: String?, password: String?, remember: Boolean) : EntityUser? {
//        if(email == null || password == null) return null
        val user = this.oneTimeLogin(email, password)
        if(remember && user != null) {
            with(pref.edit()) {
                this.putString("email", user.email)
                this.commit()
            }
        }
        return user
    }
}