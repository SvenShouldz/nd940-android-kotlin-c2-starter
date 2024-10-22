package com.udacity.asteroidradar.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.databinding.ItemAsteroidBinding
import com.udacity.asteroidradar.room.Asteroid


class AsteroidAdapter(
    private val onClick: (Asteroid) -> Unit
) : RecyclerView.Adapter<AsteroidAdapter.AsteroidViewHolder>() {

    private var asteroidList: List<Asteroid> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AsteroidViewHolder {
        // DataBinding for the RecyclerView item
        val binding = ItemAsteroidBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return AsteroidViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AsteroidViewHolder, position: Int) {
        val asteroid = asteroidList[position]
        holder.bind(asteroid)
        // Set the click listener for the item view
        holder.itemView.setOnClickListener {
            onClick(asteroid) // Invoke the listener with the clicked asteroid
        }
    }

    override fun getItemCount(): Int = asteroidList.size

    // Update the adapter with a new list of asteroids
    fun submitList(newAsteroidList: List<Asteroid>) {
        asteroidList = newAsteroidList
        notifyDataSetChanged()
    }

    // ViewHolder with DataBinding
    class AsteroidViewHolder(private val binding: ItemAsteroidBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(asteroid: Asteroid) {
            binding.asteroid = asteroid
            binding.executePendingBindings() // Apply the data immediately
        }
    }
}

