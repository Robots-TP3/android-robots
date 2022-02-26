package com.example.frontend_robots.search

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.frontend_robots.R
import com.example.frontend_robots.domain.SearchData
import com.example.frontend_robots.utils.BaseFragment
import com.example.getfluent.NetworkModule

class SearchLoadingFragment: BaseFragment(R.layout.fragment_loading) {

    private val searchLoadingViewModel by activityViewModels<SearchViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val img = requireArguments().getByteArray("compressed_img")
        Log.d("Search", "Obteniendo datos de la busqueda")
        val data = SearchData(image = img!!.toString(Charsets.UTF_8))
        searchLoadingViewModel.execute(NetworkModule.buildRetrofitClient().search(data))

        searchLoadingViewModel.userDataLoadedSuccessfully.observe(viewLifecycleOwner) { isLoaded ->
            if (isLoaded) {
                replaceFragment(SearchResultFragment.newInstance())
            }
        }
    }

    private fun replaceFragment(fragment: Fragment){
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.search_container, fragment)
        transaction.commit()
    }

    companion object {
        fun newInstance(): SearchLoadingFragment = SearchLoadingFragment()
    }
}