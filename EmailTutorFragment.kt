package ke.co.academicplanner.ui.fragments

import android.os.Bundle
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import ke.co.academicplanner.data.AcademicPlannerConstants
import ke.co.academicplanner.data.models.Email
import ke.co.academicplanner.databinding.FragmentEmailTutorBinding

private const val TAG = "EmailTutorFragment"

class EmailTutorFragment : Fragment() {

    private lateinit var binding: FragmentEmailTutorBinding

    private lateinit var firebaseDb: FirebaseFirestore
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        firebaseDb = FirebaseFirestore.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentEmailTutorBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.sendEmailButton.setOnClickListener {
            val email = binding.emailAddressField.text.toString()
            val subject = binding.subjectField.text.toString()
            val message = binding.messageField.text.toString()

            if (email.isEmpty()) {
                binding.emailAddressField.error = "Email address required"
                return@setOnClickListener
            }

            if (subject.isEmpty()) {
                binding.subjectField.error = "Subject is required"
                return@setOnClickListener
            }

            if (message.isEmpty()) {
                binding.messageField.error = "Message is required"
                return@setOnClickListener
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.emailAddressField.error = "Valid email address required"
                return@setOnClickListener
            }

            val emailInstance = Email(
                userId = firebaseAuth.currentUser?.uid,
                address = email,
                subject = subject,
                message = message
            )

            binding.progressBar.visibility = View.VISIBLE

            firebaseDb.collection(AcademicPlannerConstants.EMAIL_COLLECTION)
                .add(emailInstance)
                .addOnCompleteListener {
                    if (it.isSuccessful) {

                        Log.i(TAG, "Email Sent Successfully")

                        binding.progressBar.visibility = View.GONE

                        Toast.makeText(
                            requireContext(),
                            "Email Successfully Sent",
                            Toast.LENGTH_SHORT
                        ).show()

                    } else {

                        Log.e(TAG, "Sending Mail Failed", it.exception)

                        binding.progressBar.visibility = View.GONE

                        Toast.makeText(
                            requireContext(),
                            "Sending Mail Failed",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

        }
    }
}