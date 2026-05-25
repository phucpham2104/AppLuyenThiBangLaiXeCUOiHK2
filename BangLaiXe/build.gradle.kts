plugins {
    id("com.android.application")
    id("com.google.gms.google-services") version "4.4.4" apply false
}

dependencies {
    implementation(platform("com.google.firebase:firebase-bom:32.2.0"))
    implementation("com.google.firebase:firebase-database-ktx")
}