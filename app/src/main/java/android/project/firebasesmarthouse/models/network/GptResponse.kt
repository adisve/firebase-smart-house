package android.project.firebasesmarthouse.models.network

import com.google.gson.annotations.SerializedName

class GptResponse {
    @SerializedName("choices")
    val choices: List<GptChoice>? = null
}

class GptChoice {
    @SerializedName("message")
    val message: GptMessage? = null

    @SerializedName("logprobs")
    val logprobs: Any? = null

    @SerializedName("finish_reason")
    val finishReason: String? = null

    @SerializedName("index")
    val index: Int? = null
}

class GptMessage {
    @SerializedName("role")
    val role: String? = null

    @SerializedName("content")
    val content: String? = null
}
