package com.hwi.hazarddetection.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.hwi.hazarddetection.R
import com.hwi.hazarddetection.model.Sensor

class SensorAdapter(
    options: FirestoreRecyclerOptions<Sensor>
): FirestoreRecyclerAdapter<Sensor, SensorAdapter.SensorViewHolder>(options) {

    class SensorViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.tvName)
        val value: TextView = view.findViewById(R.id.tvValue)
        val unit: TextView = view.findViewById(R.id.tvUnit)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SensorViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context).inflate(R.layout.item_sensor_data, parent, false)
        return SensorViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: SensorViewHolder, position: Int, model: Sensor) {
        holder.name.text = model.name
        holder.value.text = model.value
        holder.unit.text = model.unit
    }
}