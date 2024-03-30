package com.example.adv_andr_firebase

import android.annotation.SuppressLint
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

object FirebaseUtils {
    val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    @SuppressLint("StaticFieldLeak")
    val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
}