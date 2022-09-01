package com.dashxdemo.app.feature.home.fragment

import android.app.ProgressDialog
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.dashxdemo.app.databinding.FragmentVideoPreviewBinding
import com.dashxdemo.app.utils.Utils.Companion.initProgressDialog

class VideoPreviewFragment : Fragment() {
    private lateinit var binding: FragmentVideoPreviewBinding

    private val url: VideoPreviewFragmentArgs by navArgs()
    private lateinit var progressDialog: ProgressDialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentVideoPreviewBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        progressDialog = ProgressDialog(requireContext())
        initProgressDialog(progressDialog, requireContext())
        showProgressDialog()

        setUpUi()
        playVideo()
    }

    private fun playVideo() {
        val uri = Uri.parse(url.videoData.videoUrl)

        binding.postVideoPlayer.setVideoURI(uri)
        binding.postVideoPlayer.setOnPreparedListener {
            hideProgressDialog()
        }

        binding.postVideoPlayer.setOnErrorListener { mediaPlayer, i, i2 ->
            hideProgressDialog()
            false
        }

        val mediaController = MediaController(context)
        binding.postVideoPlayer.setMediaController(mediaController)
        mediaController.setAnchorView(binding.postVideoPlayer)
        binding.postVideoPlayer.start()
    }

    private fun setUpUi() {
        binding.closeIcon.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun showProgressDialog() {
        progressDialog.show()
    }

    private fun hideProgressDialog() {
        progressDialog.dismiss()
    }
}
