package ke.co.academicplanner.data.models

import com.google.firebase.firestore.DocumentId

data class Score(
        @DocumentId var id: String? = null,
        var userId: String? = null,
        var unitCode: String? = null,
        var unitCredit: String?=null,
        var assignment: Int? = null,
        var cat: Int? = null,
        var exam: Int? = null
) {
    fun getTotal(): Double {

        val assignmentContribution = assignment!!.toDouble() / 30 * 10
        val catContribution = cat!!.toDouble() / 30 * 20
        val examContribution = exam!!.toDouble() / 100 * 70

        return assignmentContribution + catContribution + examContribution
    }
}