package ke.co.academicplanner.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import ke.co.academicplanner.data.AcademicPlannerConstants
import ke.co.academicplanner.databinding.FragmentDeadlineSummaryBinding

private const val TAG = "DeadlineSummaryFrag"

class DeadlineSummaryFragment : Fragment() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseDb: FirebaseFirestore

    private lateinit var binding: FragmentDeadlineSummaryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        firebaseAuth = FirebaseAuth.getInstance()
        firebaseDb = FirebaseFirestore.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDeadlineSummaryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.viewButton.setOnClickListener {
            binding.viewLayout.visibility = View.GONE
            binding.actionLayout.visibility = View.VISIBLE
        }

        binding.actionButton.setOnClickListener {

            val deadline = binding.deadlineField.text.toString()

            if (deadline.isEmpty()) {
                binding.deadlineField.error = "Deadline is required"
                return@setOnClickListener
            }

            val data = mapOf(Pair("deadline", deadline))

            binding.progressBar.visibility = View.VISIBLE

            firebaseDb.collection(AcademicPlannerConstants.DEADLINES_COLLECTION)
                .document(firebaseAuth.currentUser!!.uid)
                .set(data)
                .addOnCompleteListener {

                    if (it.isSuccessful) {

                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(requireContext(), "Deadline Set", Toast.LENGTH_SHORT).show()
                        binding.actionLayout.visibility = View.GONE
                        binding.viewLayout.visibility = View.VISIBLE

                    } else {

                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(
                            requireContext(),
                            "Deadline Setting Failed",
                            Toast.LENGTH_SHORT
                        ).show()

                    }

                }


        }

    }

    override fun onStart() {
        super.onStart()

        firebaseDb.collection(AcademicPlannerConstants.DEADLINES_COLLECTION)
            .document(firebaseAuth.currentUser!!.uid)
            .addSnapshotListener(requireActivity()) { documentSnapshot, exception ->

                if (exception != null) {
                    Log.e(TAG, "getting deadline: failed", exception)
                    Toast.makeText(requireContext(), "A fatal error occurred", Toast.LENGTH_SHORT)
                        .show()
                    return@addSnapshotListener
                }

                if (documentSnapshot!!.exists()) {
                    val deadline = documentSnapshot.get("deadline").toString()
                    if (binding.viewLayout.visibility == View.VISIBLE) {
                        binding.deadlineTv.text = deadline
                    }
                } else {
                    Toast.makeText(requireContext(), "Deadline not yet set", Toast.LENGTH_SHORT)
                        .show()
                }
            }
    }

    override fun onStop() {
        super.onStop()
    }
}