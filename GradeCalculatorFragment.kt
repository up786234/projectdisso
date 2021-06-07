package ke.co.academicplanner.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import ke.co.academicplanner.R
import ke.co.academicplanner.data.AcademicPlannerConstants
import ke.co.academicplanner.data.models.Score
import ke.co.academicplanner.databinding.FragmentGradeCalculatorBinding
import ke.co.academicplanner.ui.ScoresAdapter

private const val TAG = "GradeCalculatorFrag"

class GradeCalculatorFragment : Fragment() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseDb: FirebaseFirestore
    private lateinit var navController: NavController

    private lateinit var binding: FragmentGradeCalculatorBinding
    private var adapter: ScoresAdapter = ScoresAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        firebaseAuth = FirebaseAuth.getInstance()
        firebaseDb = FirebaseFirestore.getInstance()
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        binding = FragmentGradeCalculatorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = findNavController()

        binding.scoresRv.layoutManager = LinearLayoutManager(requireContext())
        binding.scoresRv.addItemDecoration(
                DividerItemDecoration(
                        requireContext(),
                        DividerItemDecoration.VERTICAL
                )
        )
        binding.scoresRv.adapter = adapter

        binding.fab.setOnClickListener {
            navController.navigate(R.id.addUnitFragment)
        }
    }

    override fun onStart() {
        super.onStart()

        firebaseDb.collection(AcademicPlannerConstants.SCORES_COLLECTION)
                .whereEqualTo("userId", firebaseAuth.currentUser!!.uid)
                .addSnapshotListener { value, error ->
                    if (error != null) {
                        Log.e(TAG, "Getting Scores", error)
                        Toast.makeText(requireContext(), "Failed to get scores", Toast.LENGTH_SHORT)
                                .show()
                        return@addSnapshotListener
                    }

                    if (!value!!.isEmpty) {

                        val scoresList = value.toObjects(Score::class.java)
                        adapter.scoresList = scoresList

                        var sum = 0.00

                        for (score in scoresList) sum += score.getTotal()

                        var finalScore = sum / scoresList.size

                        binding.gradeTv.text = getGrade(finalScore)


                    } else {
                        Toast.makeText(requireContext(), "Empty scores", Toast.LENGTH_SHORT).show()
                    }
                }
    }

    private fun getGrade(score: Double): String {
        return when {
            score >= 75 -> "A+"
            score >= 70 -> "A"
            score >= 60 -> "B+"
            score >= 55 -> "B"
            score >= 50 -> "C+"
            score >= 45 -> "C"
            score >= 40 -> "D+"
            score >= 35 -> "D"
            else -> "F"
        }
    }

}

