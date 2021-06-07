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
import ke.co.academicplanner.data.models.Score
import ke.co.academicplanner.databinding.FragmentAddUnitBinding

private const val TAG = "AddUnitFragment"

class AddUnitFragment : Fragment() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseDb: FirebaseFirestore

    private lateinit var binding: FragmentAddUnitBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        firebaseAuth = FirebaseAuth.getInstance()
        firebaseDb = FirebaseFirestore.getInstance()
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddUnitBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.addScoreButton.setOnClickListener {

            val unitCode = binding.unitCodeField.text.toString()
            val unitCredit = binding.unitCodeCredits.text.toString()
            val assignment = binding.assignmentField.text.toString()
            val cat = binding.catField.text.toString()
            val exam = binding.examField.text.toString()

            if (unitCode.isEmpty()) {
                binding.unitCodeField.error = "Unit code is required"
                return@setOnClickListener
            }
            if (unitCredit.isEmpty()) {
                binding.unitCodeCredits.error = "Unit credits required"
                return@setOnClickListener
            }

            if (assignment.isEmpty()) {
                binding.assignmentField.error = "Assignment score is required"
                return@setOnClickListener
            }

            if (cat.isEmpty()) {
                binding.catField.error = "CAT score is required"
                return@setOnClickListener
            }

            if (exam.isEmpty()) {
                binding.catField.error = "Exam score is required"
                return@setOnClickListener
            }

            val score = Score(
                    userId = firebaseAuth.currentUser!!.uid,
                    unitCode = unitCode,
                    unitCredit = unitCredit,
                    assignment = assignment.toInt(),
                    cat = cat.toInt(),
                    exam = exam.toInt()
            )

            firebaseDb.collection(AcademicPlannerConstants.SCORES_COLLECTION)
                    .add(score)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            Toast.makeText(
                                    requireContext(),
                                    "Score successfully added",
                                    Toast.LENGTH_SHORT
                            ).show()
                            requireActivity().onBackPressed()
                        } else {
                            Log.e(TAG, "adding unit score: failed", it.exception)
                            Toast.makeText(
                                    requireContext(),
                                    "Adding Unit Score Failed",
                                    Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
        }
    }
}