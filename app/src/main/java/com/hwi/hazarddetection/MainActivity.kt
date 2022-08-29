package com.hwi.hazarddetection

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase
import com.hwi.hazarddetection.adapter.SensorAdapter
import com.hwi.hazarddetection.dao.SensorDao
import com.hwi.hazarddetection.databinding.ActivityMainBinding
import com.hwi.hazarddetection.model.Sensor
import kotlinx.coroutines.NonCancellable.cancel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var sensorDao: SensorDao
    private lateinit var adapter: SensorAdapter
    private lateinit var googleSignInClient: GoogleSignInClient

//    private var altitude = -1f
    private var humidity = -1f
    private var pressure = -1f
    private var smoke = -1f
//    private var sound = -1f
    private var temperature = -1f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        binding.topAppBar.setOnMenuItemClickListener {
            if (it.itemId == R.id.signOutButton)
            {
                MaterialAlertDialogBuilder(this)
                    .setTitle(resources.getString(R.string.sign_out))
                    .setMessage(resources.getString(R.string.sign_out_confirmation))
                    .setNeutralButton(resources.getString(R.string.cancel)) { dialog, which ->
                        // Respond to neutral button press
                        dialog.dismiss()
                    }
                    .setPositiveButton(resources.getString(R.string.sign_out)) { dialog, which ->
                        // Respond to positive button press
                        Firebase.auth.signOut()
                        googleSignInClient.signOut().addOnCompleteListener {
                            val signInActivityIntent = Intent(this, SignInActivity::class.java)
                            startActivity(signInActivityIntent)
                            finish()
                        }
                    }
                    .show()
                true
            }
            else
                false
        }
        setUpRecyclerView()
        setAlerts()
    }

    private fun setUpRecyclerView() {
        sensorDao = SensorDao()
        val sensorCollection = sensorDao.sensorCollection
        val query = sensorCollection.orderBy("name", Query.Direction.ASCENDING)
        val recyclerViewOption = FirestoreRecyclerOptions.Builder<Sensor>()
            .setQuery(query, Sensor::class.java)
            .build()

        adapter = SensorAdapter(recyclerViewOption)
        binding.recyclerView.adapter = adapter
    }

    private fun setAlerts() {
        val sensorCollection = sensorDao.sensorCollection

//        val altitudeDoc = sensorCollection.document("1")
//        altitudeDoc.addSnapshotListener { snapshot, e ->
//            if (e != null) {
//                Log.w(TAG, "Listen failed.", e)
//                return@addSnapshotListener
//            }
//
//            if (snapshot != null && snapshot.exists()) {
//                altitude = (snapshot.data?.get("value") as String).toInt()
//                invalidateSensors()
//            } else {
//                Log.d(TAG, "Current data: null")
//            }
//        }

        val humidityDoc = sensorCollection.document("2")
        humidityDoc.addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w(TAG, "Listen failed.", e)
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {
                humidity = (snapshot.data?.get("value") as String).toFloat()
                invalidateSensors()
            } else {
                Log.d(TAG, "Current data: null")
            }
        }

        val pressureDoc = sensorCollection.document("3")
        pressureDoc.addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w(TAG, "Listen failed.", e)
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {
                pressure = (snapshot.data?.get("value") as String).toFloat()
                invalidateSensors()
            } else {
                Log.d(TAG, "Current data: null")
            }
        }

        val smokeDoc = sensorCollection.document("4")
        smokeDoc.addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w(TAG, "Listen failed.", e)
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {
                smoke = (snapshot.data?.get("value") as String).toFloat()
                invalidateSensors()
            } else {
                Log.d(TAG, "Current data: null")
            }
        }

//        val soundDoc = sensorCollection.document("5")
//        soundDoc.addSnapshotListener { snapshot, e ->
//            if (e != null) {
//                Log.w(TAG, "Listen failed.", e)
//                return@addSnapshotListener
//            }
//
//            if (snapshot != null && snapshot.exists()) {
//                sound = (snapshot.data?.get("value") as String).toInt()
//                invalidateSensors()
//            } else {
//                Log.d(TAG, "Current data: null")
//            }
//        }

        val temperatureDoc = sensorCollection.document("6")
        temperatureDoc.addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w(TAG, "Listen failed.", e)
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {
                temperature = (snapshot.data?.get("value") as String).toFloat()
                invalidateSensors()
            } else {
                Log.d(TAG, "Current data: null")
            }
        }

    }
    private fun invalidateSensors() {
        if (smoke > 100f && temperature >= 65f) {
            binding.tvAlertValue.text = "Fire"
            binding.tvAlertValue.setTextColor(resources.getColor(R.color.red))
        } else {
            binding.tvAlertValue.text = "--"
            binding.tvAlertValue.setTextColor(resources.getColor(R.color.green))
        }
    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }
}