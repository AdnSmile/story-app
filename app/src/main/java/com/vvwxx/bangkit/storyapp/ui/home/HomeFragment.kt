package com.vvwxx.bangkit.storyapp.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.vvwxx.bangkit.storyapp.data.response.ListStoryItem
import com.vvwxx.bangkit.storyapp.databinding.FragmentHomeBinding
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
            Toast.makeText(requireActivity(), "stories.", Toast.LENGTH_SHORT).show()
            setStoriesData(stories)
        }

        homeViewModel.isLoading.observe(requireActivity()) {
            showLoading(it)
        }

        homeViewModel.getUser.observe(requireActivity()) { user->
            homeViewModel.getAllStories(user.token)
        }

//        homeViewModel.message.observe(requireActivity()) {
//            // TODO buat message toast
//        }

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
        Toast.makeText(requireActivity(), stories.name, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

}