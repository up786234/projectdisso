package ke.co.academicplanner.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ke.co.academicplanner.data.models.Score
import ke.co.academicplanner.databinding.ScoreItemBinding
import java.text.DecimalFormat

class ScoresAdapter : RecyclerView.Adapter<ScoresAdapter.ScoreViewHolder>() {

    var scoresList: List<Score>? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScoreViewHolder {
        return ScoreViewHolder(
            ScoreItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount() = scoresList?.size ?: 0

    override fun onBindViewHolder(holder: ScoreViewHolder, position: Int) {

        val decimalFormat = DecimalFormat("##.##");
        holder.binding.unitCodeTv.text = scoresList!![position].unitCode
        holder.binding.finalScoreTv.text = decimalFormat.format(scoresList!![position].getTotal())
        holder.binding.gradeTv.text = getGrade(scoresList!![position].getTotal()).toString()
    }

    private fun getGrade(score: Double): Char {
        return when {
            score > 70 -> 'A'
            score > 60 -> 'B'
            score > 50 -> 'C'
            score > 40 -> 'D'
            else -> 'F'
        }
    }

    class ScoreViewHolder(val binding: ScoreItemBinding) : RecyclerView.ViewHolder(binding.root)
}