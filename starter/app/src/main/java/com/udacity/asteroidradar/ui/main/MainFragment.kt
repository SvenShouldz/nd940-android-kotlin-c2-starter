package com.udacity.asteroidradar.ui.main

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }
    private lateinit var binding: FragmentMainBinding
    private lateinit var adapter: AsteroidAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout using Data Binding
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false)

        // Set the lifecycle owner for data binding
        binding.lifecycleOwner = this

        // Bind the viewModel to the layout
        binding.viewModel = viewModel

        // Initialize the RecyclerView Adapter
        adapter = AsteroidAdapter { asteroid ->
            // Navigate to a detail fragment on click
            findNavController(binding.root).navigate(MainFragmentDirections.actionShowDetail(asteroid))
        }
        binding.asteroidRecycler.adapter = adapter
        binding.asteroidRecycler.layoutManager = LinearLayoutManager(requireContext())

        // Observe the asteroidList LiveData and submit the list to the adapter
        viewModel.asteroidList.observe(viewLifecycleOwner, Observer { asteroidList ->
            asteroidList?.let {
                adapter.submitList(it)
            }
        })
        viewModel.apod.observe(viewLifecycleOwner) { apodResponse ->
            apodResponse?.let {

                Log.d("TAG", it.url)
                // Load the image using Glide
                Glide.with(requireContext())
                    .load(it.url)
                    .into(binding.activityMainImageOfTheDay)
            }
        }

        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return true
    }
}

