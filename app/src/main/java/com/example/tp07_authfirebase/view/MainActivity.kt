package com.example.tp07_authfirebase.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.tp07_authfirebase.R
import com.example.tp07_authfirebase.databinding.ActivityMainBinding
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val user = FirebaseAuth.getInstance().currentUser
        if (user == null) {
            openLogin()
            return
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.txtName.text = user.displayName?.takeIf { it.isNotBlank() } ?: getString(R.string.no_name)
        binding.txtEmail.text = user.email
        binding.txtUid.text = user.uid

        binding.btnSignOut.setOnClickListener {
            AuthUI.getInstance().signOut(this).addOnCompleteListener { openLogin() }
        }
    }

    private fun openLogin() {
        val intent = Intent(this, LoginActivity::class.java)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
        finish()
    }
}
