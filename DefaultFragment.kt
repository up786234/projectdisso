package ke.co.academicplanner.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import ke.co.academicplanner.R
import ke.co.academicplanner.databinding.FragmentDefaultBinding

class DefaultFragment : Fragment() {

    private lateinit var binding: FragmentDefaultBinding
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDefaultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = findNavController()

        binding.academicCalenderTv.setOnClickListener {
            navController.navigate(R.id.academicCalendarFragment)
        }

        binding.emailTutorTv.setOnClickListener {
            navController.navigate(R.id.emailTutorFragment)
        }

        binding.gradeCalculatorTv.setOnClickListener {
            navController.navigate(R.id.gradeCalculatorFragment)
        }

        binding.deadlineSummaryTv.setOnClickListener {
            navController.navigate(R.id.deadlineSummaryFragment)
        }
    }

}