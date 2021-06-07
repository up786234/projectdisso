package ke.co.academicplanner.ui.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import ke.co.academicplanner.databinding.ActivityRegisterBinding
import ke.co.academicplanner.ui.HomeActivity

private const val TAG = "RegisterActivity"

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)

        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        initWidgets()
    }

    private fun initWidgets() {

        binding.loginPromptTv.setOnClickListener {
            //Finish the activity and take user to the previous activity or kill the application
            finish()
        }

        binding.registerButton.setOnClickListener {

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

            binding.registerProgressbar.visibility = View.VISIBLE
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "createUserWithEmail:success")

                        val intent = Intent(this@RegisterActivity, HomeActivity::class.java).apply {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        }
                        startActivity(intent)

                        binding.registerProgressbar.visibility = View.GONE
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", task.exception)
                        Toast.makeText(
                            this@RegisterActivity,
                            "Authentication failed.",
                            Toast.LENGTH_SHORT
                        ).show()

                        binding.registerProgressbar.visibility = View.GONE
                    }
                }
        }
    }
}