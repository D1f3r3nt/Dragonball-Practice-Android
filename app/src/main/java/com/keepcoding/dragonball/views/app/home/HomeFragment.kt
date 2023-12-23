package com.keepcoding.dragonball.views.app.home

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.keepcoding.dragonball.commons.Error
import com.keepcoding.dragonball.commons.ProgressManager
import com.keepcoding.dragonball.commons.ResponseHeroes
import com.keepcoding.dragonball.databinding.FragmentHomeBinding
import com.keepcoding.dragonball.views.app.AppRouter
import com.keepcoding.dragonball.views.app.AppViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeFragment(
    private val activity: AppRouter,
    private val sharedProgress: SharedPreferences,
): Fragment() {
    
    private lateinit var binding: FragmentHomeBinding

    private val homeAdapter = HomeAdapter(activity)

    private val viewModel: AppViewModel by activityViewModels()
    private val progressManager = ProgressManager()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentHomeBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setUpRecyclerView()
        setObservers()
    }

    fun setUpRecyclerView() {
        binding.list.layoutManager = LinearLayoutManager(binding.root.context)
        binding.list.adapter = homeAdapter
    }

    private fun handleErrorState(error: Error) {
        Toast.makeText(activity.getContext(), error.msg, Toast.LENGTH_LONG).show()
    }

    private fun handleResponseHeroesState(response: ResponseHeroes) {
        val heroes = progressManager.controlData(response, sharedProgress)

        homeAdapter.update(heroes)
    }

    private fun setObservers() {
        lifecycleScope.launch(Dispatchers.Main) {
            viewModel.state.collect{ state ->
                when (state) {
                    is Error -> handleErrorState(state)
                    is ResponseHeroes -> handleResponseHeroesState(state)
                    else -> {}
                }
            }
        }
    }
    
}