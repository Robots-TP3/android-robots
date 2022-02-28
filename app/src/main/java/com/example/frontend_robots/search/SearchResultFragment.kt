package com.example.frontend_robots.search

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.frontend_robots.R
import com.example.frontend_robots.domain.SearchResponse
import com.example.frontend_robots.utils.BaseFragment
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_search_result.*


class SearchResultFragment: BaseFragment(R.layout.fragment_search_result) {

    private val searchLoadingViewModel by activityViewModels<SearchViewModel>()
    private lateinit var searchResponse: SearchResponse

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        searchResponse = searchLoadingViewModel.searchResponse!!
        fillSearch(searchResponse)
        button_link.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(searchResponse.link))
            startActivity(browserIntent)
        }
    }

    private fun fillSearch(searchResponse: SearchResponse) {
        searchResponse.message?.let {
            result_ok_container.visibility = View.GONE
            result_not_found_container.visibility = View.VISIBLE
            result_message_not_found.text = it
        } ?: run {
            result_ok_container.visibility = View.VISIBLE
            result_not_found_container.visibility = View.GONE
        }
    }

    private fun replaceFragment(fragment: Fragment){
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.search_container, fragment)
        transaction.commit()
    }

    companion object {
        fun newInstance(): SearchResultFragment = SearchResultFragment()
    }
}