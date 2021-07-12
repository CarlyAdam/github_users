package com.carlyadam.github.ui.welcome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.carlyadam.github.R
import com.carlyadam.github.databinding.FragmentWelcomeBinding
import com.carlyadam.github.utils.showToast
import dagger.hilt.android.AndroidEntryPoint
import me.toptas.fancyshowcase.FancyShowCaseView

@AndroidEntryPoint
class WelcomeFragment : Fragment(R.layout.fragment_welcome) {

    private var _binding: FragmentWelcomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWelcomeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            createShowCase(searchView)

            searchView.setOnQueryTextListener(object :
                SearchView.OnQueryTextListener,
                android.widget.SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    if (query!!.length >= 3) {
                        searchView.setQuery("", false)
                        val action = WelcomeFragmentDirections.actionWelcomeSearch(query!!)
                        findNavController().navigate(action)
                    } else {
                       showToast(requireActivity(),getString(R.string.search_error))
                    }

                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return false
                }
            })
        }
    }

    private fun createShowCase(view: View) {
        FancyShowCaseView.Builder(requireActivity())
            .focusOn(view)
            .title(getString(R.string.search_hint))
            .enableAutoTextPosition()
            .showOnce("id0")
            .build()
            .show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
