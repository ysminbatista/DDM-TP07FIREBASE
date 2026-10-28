package com.example.tp07_authfirebase.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import com.example.tp07_authfirebase.R
import com.example.tp07_authfirebase.databinding.ActivityLoginBinding
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth

// Tela de login do FirebaseUI: o mesmo fluxo cobre entrar e cadastrar (e-mail/senha).
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private val signInLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(FirebaseAuthUIActivityResultContract()) { result ->
            onSignInResult(result)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        binding.btnRetry.setOnClickListener { startSignIn() }

        if (FirebaseAuth.getInstance().currentUser != null) {
            openMain()
            return
        }
        startSignIn()
    }

    private fun startSignIn() {
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build()
        )

        val signInIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .setLogo(R.mipmap.ic_launcher)
            .setTheme(R.style.Theme_TP07AuthFirebase)
            .build()
        signInLauncher.launch(signInIntent)
    }

    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        if (result.resultCode == RESULT_OK) {
            openMain()
            return
        }

        val response = result.idpResponse
        if (response == null) {
            // Usuário voltou sem entrar: encerra o app em vez de reabrir o login em loop.
            finish()
            return
        }

        val message = if (response.error?.errorCode == ErrorCodes.NO_NETWORK) {
            R.string.sign_in_no_network
        } else {
            R.string.sign_in_error
        }
        // Sem reabrir o FirebaseUI automaticamente: um erro persistente causaria loop infinito.
        setContentView(binding.root)
        binding.txtError.setText(message)
    }

    private fun openMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
