package android.project.firebasesmarthouse.interfaces

interface FirebaseAPI {
    fun setLightState(state: Boolean)

    fun setDoorState(state: Boolean)

    fun setWindowState(state: Boolean)
}