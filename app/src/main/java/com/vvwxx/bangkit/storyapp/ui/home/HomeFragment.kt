package com.vvwxx.bangkit.storyapp.ui.home

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.vvwxx.bangkit.storyapp.data.response.ListStoryItem
import com.vvwxx.bangkit.storyapp.databinding.FragmentHomeBinding
import com.vvwxx.bangkit.storyapp.ui.create.CreateStoriesActivity
import com.vvwxx.bangkit.storyapp.ui.detail.DetailActivity
import com.vvwxx.bangkit.storyapp.utils.ViewModelFactory

class HomeFragment : Fragment() {

    private var _binding : FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var factory: ViewModelFactory
    private val homeViewModel: HomeViewModel by viewModels { factory }

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

        homeViewModel.listStories.observe(requireActivity()) {stories ->
            setStoriesData(stories)
        }

        homeViewModel.isLoading.observe(requireActivity()) {
            showLoading(it)
        }

        homeViewModel.getUser.observe(requireActivity()) { user->
            homeViewModel.getAllStories(user.token)
        }

        setupAction()
    }

    private fun setupAction() {
        binding.addStories.setOnClickListener {
            val intent = Intent(requireActivity(), CreateStoriesActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setStoriesData(listStories: List<ListStoryItem>) {
        val stories = ArrayList<ListStoryItem> ()
        for (i in listStories) {
            stories.add(i)
        }
        val adapter = StoriesAdapter(stories) {
            showSelectedStories(it)
        }
        binding.rvStories.adapter = adapter
    }

    private fun showSelectedStories(stories: ListStoryItem) {
        val intent = Intent(requireActivity(), DetailActivity::class.java)
        intent.putExtra(DetailActivity.EXTRA_USER, stories.id)
        startActivity(intent)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

}