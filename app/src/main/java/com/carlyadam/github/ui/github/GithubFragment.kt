package com.carlyadam.github.ui.github

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.carlyadam.github.R
import com.carlyadam.github.data.db.model.User
import com.carlyadam.github.databinding.FragmentGithubBinding
import com.carlyadam.github.ui.github.adapter.GithubAdapter
import com.carlyadam.github.ui.github.adapter.GithubLoadStateAdapter
import com.carlyadam.github.utils.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import com.carlyadam.github.data.api.model.User as ApiUser

@AndroidEntryPoint
class GithubFragment :
    Fragment(R.layout.fragment_github),
    GithubAdapter.AdapterListener {

    private var _binding: FragmentGithubBinding? = null
    private val binding get() = _binding!!
    private lateinit var githubdapter: GithubAdapter
    private val githubViewModel: GithubViewModel by viewModels()
    private lateinit var searchView: SearchView
    private lateinit var searchItem: MenuItem
    private val args by navArgs<GithubFragmentArgs>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        githubViewModel.saveQuery(args.query)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGithubBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        githubdapter = GithubAdapter(this, requireActivity())

        binding.apply {
            recyclerGithub.layoutManager = LinearLayoutManager(requireActivity())

            githubdapter.withLoadStateFooter(
                footer = GithubLoadStateAdapter { githubdapter.retry() },
            )

            recyclerGithub.adapter = githubdapter

            buttonRetry.setOnClickListener {
                githubdapter.retry()
            }

            swipetorefresh.setOnRefreshListener {
                searchUsers(githubViewModel.getQuery()!!)
            }

            githubdapter.addLoadStateListener { loadState ->

                // show empty list
                val isListEmpty =
                    loadState.refresh is LoadState.NotLoading && githubdapter.itemCount == 0
                if (isListEmpty) {
                    recyclerGithub.isVisible = false
                    textViewEmpty.isVisible = true
                } else {
                    textViewEmpty.isVisible = false
                }

                // Only show the list if refresh succeeds, either from the the local db or the remote.
                binding.recyclerGithub.isVisible =
                    loadState.source.refresh is LoadState.NotLoading || loadState.mediator?.refresh is LoadState.NotLoading
                // Show loading spinner during initial load or refresh.
                shimmerViewContainer.isVisible =
                    loadState.mediator?.refresh is LoadState.Loading
                // Show the retry state if initial load or refresh fails and there are no items.
                buttonRetry.isVisible =
                    loadState.mediator?.refresh is LoadState.Error && githubdapter.itemCount == 0
                textViewError.isVisible =
                    loadState.mediator?.refresh is LoadState.Error && githubdapter.itemCount == 0
                // Toast on any error, regardless of whether it came from RemoteMediator or PagingSource
                val errorState = loadState.source.append as? LoadState.Error
                    ?: loadState.source.prepend as? LoadState.Error
                    ?: loadState.append as? LoadState.Error
                    ?: loadState.prepend as? LoadState.Error
                errorState?.let {
                    showToast(requireActivity(), "${it.error}")
                }
            }
        }

        setHasOptionsMenu(true)

        githubViewModel.getQuery()?.apply {
            searchUsers(this)
        }
    }

    private fun searchUsers(query: String) {
        binding.swipetorefresh.isRefreshing = false
        viewLifecycleOwner.lifecycleScope.launch {
            githubViewModel.users(query).collectLatest { pagingData ->
                githubdapter.submitData(viewLifecycleOwner.lifecycle, pagingData)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.menu_search, menu)

        searchItem = menu.findItem(R.id.action_search)
        searchView = searchItem!!.actionView as SearchView

        searchView!!.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    binding.recyclerGithub.scrollToPosition(0)
                    githubViewModel.saveQuery(query)
                    searchView.clearFocus()
                    searchUsers(query)
                }
                return true
            }

            override fun onQueryTextChange(query: String?): Boolean {
                return true
            }
        })
        githubViewModel.getQuery()?.apply {
            searchItem.expandActionView()
            searchView.setQuery(this, false)
            searchView.clearFocus()
        }
    }

    override fun onItemTap(user: User) {
        val apiUser = ApiUser(user.id, user.login, user.avatar, user.favorite, user.score)
        val action = GithubFragmentDirections.actionGithubUserDetails(apiUser)
        findNavController().navigate(action)
    }

    override fun onFavoriteTap(user: User, favorite: Boolean) {
        user.favorite = favorite
        setUserFavorite(user.id, favorite)
    }

    private fun setUserFavorite(id: Long, favorite: Boolean) {
        githubViewModel.setUserFavorite(id, favorite)
    }

    override fun onDestroy() {
        super.onDestroy()
        this.arguments?.clear()
        _binding = null
    }
}
