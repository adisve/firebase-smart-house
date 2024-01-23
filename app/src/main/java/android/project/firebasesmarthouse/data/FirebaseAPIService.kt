package android.project.firebasesmarthouse.data

import android.project.firebasesmarthouse.BuildConfig
import android.project.firebasesmarthouse.interfaces.FirebaseAPI
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

object FirebaseAPIService: FirebaseAPI {
    private val database by lazy { Firebase.database("https://${BuildConfig.FIREBASE_DATABASE_URL}") }
    private val lightRef = database.getReference("light")
    private val doorRef = database.getReference("door")
    private val windowRef = database.getReference("window")

    var onLightStateChanged: ((Boolean) -> Unit)? = null
    var onDoorStateChanged: ((Boolean) -> Unit)? = null
    var onWindowStateChanged: ((Boolean) -> Unit)? = null

    private val listener = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val state = snapshot.getValue(Boolean::class.java) ?: false
            when (snapshot.ref) {
                lightRef -> onLightStateChanged?.invoke(state)
                doorRef -> onDoorStateChanged?.invoke(state)
                windowRef -> onWindowStateChanged?.invoke(state)
            }
        }
        override fun onCancelled(error: DatabaseError) {
            error.toException().printStackTrace()
        }
    }

    init {
        lightRef.addValueEventListener(listener)
        doorRef.addValueEventListener(listener)
        windowRef.addValueEventListener(listener)
    }

    override fun setLightState(state: Boolean) {
        lightRef.setValue(state)
    }

    override fun setDoorState(state: Boolean) {
        doorRef.setValue(state)
    }

    override fun setWindowState(state: Boolean) {
        windowRef.setValue(state)
    }
}