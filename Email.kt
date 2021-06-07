package ke.co.academicplanner.data.models

import com.google.firebase.firestore.DocumentId

data class Email(
    @DocumentId val id: String? = null,
    val address: String? = null,
    val userId: String? = null,
    val subject: String? = null,
    val message: String? = null
)