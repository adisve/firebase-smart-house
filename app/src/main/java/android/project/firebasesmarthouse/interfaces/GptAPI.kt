package android.project.firebasesmarthouse.interfaces


import android.project.firebasesmarthouse.BuildConfig
import android.project.firebasesmarthouse.models.network.GptRequest
import android.project.firebasesmarthouse.models.network.GptResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface GptAPI {
    @Headers(*[
        "Content-Type: application/json",
        "Authorization: Bearer ${BuildConfig.GPT_API_KEY}",
    ])

    @POST("chat/completions")
    suspend fun createCompletion(@Body request: GptRequest): Response<GptResponse>
}
