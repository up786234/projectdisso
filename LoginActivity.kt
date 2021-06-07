package ke.co.academicplanner.ui.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import ke.co.academicplanner.databinding.ActivityLoginBinding
import ke.co.academicplanner.ui.HomeActivity

private const val TAG = "RegisterActivity"


class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        initWidgets()
    }

    private fun initWidgets() {

        binding.loginButton.setOnClickListener {

            val email = binding.emailAddressField.text.toString()
            val password = binding.passwordField.text.toString()

            if (email.isEmpty()) {
                binding.emailAddressField.error = "Email address required"
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                binding.passwordField.error = "Password is required"
                return@setOnClickListener
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.emailAddressField.error = "Valid email address required"
                return@setOnClickListener
            }

            binding.loginProgressbar.visibility = View.VISIBLE
            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (it.isSuccessful) {

                        Log.i(TAG, "signInWithEmailAndPassword: success")

                        binding.loginProgressbar.visibility = View.GONE

                        sendHome()

                    } else {

                        Log.e(TAG, "signInWithEmailAndPassword: failed", it.exception)

                        binding.loginProgressbar.visibility = View.GONE


                        Toast.makeText(
                            this@LoginActivity,
                            "Authentication failed.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

        }

        binding.registerPromptTv.setOnClickListener {

            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)

            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()

        val currentUser = firebaseAuth.currentUser;

        if (currentUser != null) {
            sendHome()
        }
    }

    private fun sendHome() {

        val intent = Intent(this@LoginActivity, HomeActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        }

        startActivity(intent)

    }
}