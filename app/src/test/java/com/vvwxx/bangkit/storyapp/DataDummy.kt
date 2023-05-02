package com.vvwxx.bangkit.storyapp

import com.vvwxx.bangkit.storyapp.data.response.ListStoryItem
import kotlin.text.Typography.quote

object DataDummy {
    fun generateDummyQuoteResponse(): List<ListStoryItem> {
        val items: MutableList<ListStoryItem> = arrayListOf()
        for (i in 0..100) {
            val stories = ListStoryItem(
                "photoUrl: $i",
                "createdAt: $i",
                "name: $i",
                "description: $i",
                i.toDouble(),
                "id: $i",
                i.toDouble()
            )
            items.add(stories)
        }
        return items
    }
}