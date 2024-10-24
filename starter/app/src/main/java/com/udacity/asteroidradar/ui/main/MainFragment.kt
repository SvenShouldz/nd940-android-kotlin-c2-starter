package com.udacity.asteroidradar.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.squareup.picasso.Picasso
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentMainBinding
import com.udacity.asteroidradar.network.AsteroidCacheWorker
import java.util.concurrent.TimeUnit

class MainFragment : Fragment() {

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }
    private lateinit var binding: FragmentMainBinding
    private lateinit var adapter: AsteroidAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflating layout using Data Binding
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false)

        // Setting lifecycle owner for data binding
        binding.lifecycleOwner = this

        // Binding viewModel to layout
        binding.viewModel = viewModel

        // Initialize the RecyclerView
        setupAsteroidRecycler()

        // Observe the asteroidList LiveData and Picture of Day
        observeAsteroidList()
        observePictureOfDay()

        setHasOptionsMenu(true)
        return binding.root
    }


    private fun observePictureOfDay() {
        viewModel.apod.observe(viewLifecycleOwner) { apodResponse ->
            val apodBinding = binding.activityMainImageOfTheDay
            apodResponse?.let {
                // Load the image using Picasso
                Picasso.get()
                    .load(it.url)
                    .into(binding.activityMainImageOfTheDay)
            }
        }
    }

    private fun observeAsteroidList() {
        viewModel.asteroidList.observe(viewLifecycleOwner, Observer { asteroidList ->
            asteroidList?.let {
                adapter.submitList(it)
            }
        })
    }

    private fun setupAsteroidRecycler() {
        adapter = AsteroidAdapter { asteroid ->
            // Navigate to a detail fragment on click
            findNavController(binding.root).navigate(
                MainFragmentDirections.actionShowDetail(
                    asteroid
                )
            )
        }
        binding.asteroidRecycler.adapter = adapter
        binding.asteroidRecycler.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return true
    }
}

