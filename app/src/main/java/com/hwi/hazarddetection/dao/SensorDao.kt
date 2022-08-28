package com.hwi.hazarddetection.dao

import com.google.firebase.firestore.FirebaseFirestore

class SensorDao {
    private val db = FirebaseFirestore.getInstance()
    val sensorCollection = db.collection("sensors")
}