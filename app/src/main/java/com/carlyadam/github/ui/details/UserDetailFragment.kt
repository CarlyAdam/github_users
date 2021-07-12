package com.carlyadam.github.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.carlyadam.github.R
import com.carlyadam.github.data.db.model.User
import com.carlyadam.github.databinding.FragmentUserDetailBinding
import com.carlyadam.github.utils.loadImage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserDetailFragment : Fragment(R.layout.fragment_user_detail) {

    private var _binding: FragmentUserDetailBinding? = null
    private val binding get() = _binding!!
    private val args by navArgs<UserDetailFragmentArgs>()
    private val userViewModel: UserViewModel by viewModels()
    private lateinit var user: User

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUserDetailBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        user = args.user

        binding.apply {
            textViewUserName.text = user.login
            imageViewUser.loadImage(requireActivity(), user.avatar, progressBar)
            ratingBarScore.rating = user.score.toFloat()
            checkBox.isChecked = user.favorite
            checkBox.apply {
                this.setOnClickListener {
                    val userDb = com.carlyadam.github.data.db.model.User(
                        user.id,
                        user.login,
                        user.avatar,
                        this.isChecked,
                        user.score
                    )
                    setUserFavorite(
                        userDb,
                        this.isChecked
                    )
                }
            }
        }
    }

    private fun setUserFavorite(
        user: User,
        favorite: Boolean
    ) {
        userViewModel.setUserFavorite(user.id, favorite)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
