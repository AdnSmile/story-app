package com.vvwxx.bangkit.storyapp.ui.detail

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.vvwxx.bangkit.storyapp.data.response.Story
import com.vvwxx.bangkit.storyapp.databinding.ActivityDetailBinding
import com.vvwxx.bangkit.storyapp.utils.ViewModelFactory

class DetailActivity : AppCompatActivity() {

    private lateinit var binding : ActivityDetailBinding

    private lateinit var factory: ViewModelFactory
    private val detailViewModel: DetailViewModel by viewModels { factory }

    private lateinit var name: String
    private lateinit var createAt: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        factory = ViewModelFactory.getInstance(this)

        val id = intent.getStringExtra(EXTRA_USER)

        detailViewModel.getUser.observe(this) {stories->
            if (id != null) {
                detailViewModel.getDetailStories(stories.token, id)
            }
        }

        detailViewModel.storiesData.observe(this) {data ->
            setStoriesData(data.story)
            name = data.story.name
            createAt = data.story.createdAt
        }

        detailViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        setupAction()
    }

    private fun setStoriesData(data: Story){
        with(binding) {
            setPhoto(data)
            tvName.text = data.name
            tvDesc.text = data.description
            tvCreatedAt.text = data.createdAt
            if (data.lat != null)
                tvLat.text = data.lat.toString()

            if (data.lon != null)
                tvLon.text = data.lon.toString()
        }
    }

    private fun setupAction() {
        binding.actionShare.setOnClickListener {
            val text = "$name uplaoded stories at $createAt"
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_TEXT, text)
            startActivity(Intent.createChooser(shareIntent, "Dicoding Stories"))
        }
    }

    private fun setPhoto(data: Story) {
        Glide.with(this)
            .load(data.photoUrl)
            .into(binding.imgItemPhoto)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        const val EXTRA_USER = "extra_user"
    }
}