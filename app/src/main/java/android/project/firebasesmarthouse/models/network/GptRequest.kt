package android.project.firebasesmarthouse.models.network

import com.google.gson.annotations.SerializedName

class GptRequest(
    @field:SerializedName("model") private val model: String,
    @field:SerializedName("temperature") private val temperature: Double,
    @field:SerializedName("messages") private val messages: List<Map<String, String>>
)