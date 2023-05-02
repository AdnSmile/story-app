package com.vvwxx.bangkit.storyapp.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.vvwxx.bangkit.storyapp.data.paging.LoadingStateAdapter
import com.vvwxx.bangkit.storyapp.databinding.FragmentHomeBinding
import com.vvwxx.bangkit.storyapp.ui.create.CreateStoriesActivity
import com.vvwxx.bangkit.storyapp.utils.ViewModelFactory

class HomeFragment : Fragment() {

    private var _binding : FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var factory: ViewModelFactory
    private val homeViewModel: HomeViewModel by viewModels { factory }
    private lateinit var adapter: StoriesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        factory = ViewModelFactory.getInstance(requireActivity())

        val layoutManager = LinearLayoutManager(requireActivity())
        binding.rvStories.layoutManager = layoutManager

        homeViewModel.listStories.observe(viewLifecycleOwner) {
            adapter.submitData(lifecycle, it)
        }

        setupAction()
        setupAdapter()
    }

    private fun setupAdapter() {
        adapter = StoriesAdapter()
        binding.rvStories.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapter.retry()
            }
        )
    }

    private fun setupAction() {
        binding.addStories.setOnClickListener {
            val intent = Intent(requireActivity(), CreateStoriesActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()
        if (::adapter.isInitialized) {
            adapter.refresh()
        }
    }

}