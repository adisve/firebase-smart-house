package android.project.firebasesmarthouse;

import android.app.Application;

public class FirebaseSmartHouseApplication extends Application {
    private static FirebaseSmartHouseApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static FirebaseSmartHouseApplication getInstance() {
        return instance;
    }
}

