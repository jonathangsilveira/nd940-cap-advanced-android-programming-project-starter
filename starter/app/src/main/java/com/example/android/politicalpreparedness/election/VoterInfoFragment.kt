package com.example.android.politicalpreparedness.election

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.data.ServiceLocator
import com.example.android.politicalpreparedness.databinding.FragmentVoterInfoBinding
import com.google.android.material.snackbar.Snackbar

class VoterInfoFragment : Fragment() {

    private val _viewModel: VoterInfoViewModel by viewModels {
        val repo = ServiceLocator.provideVoterInfoRepository(requireContext())
        VoterInfoViewModelFactory(repo)
    }

    private lateinit var binding: FragmentVoterInfoBinding

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        binding = FragmentVoterInfoBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = _viewModel

        val args = VoterInfoFragmentArgs.fromBundle(requireArguments())
        _viewModel.load(args)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _viewModel.url.observe(viewLifecycleOwner, ::openUrl)
        _viewModel.error.observe(viewLifecycleOwner, ::onError)
    }

    private fun openUrl(url: String?) {
        val uri = url?.toUri() ?: return
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
    }

    private fun onError(message: String?) {
        message ?: return
        Snackbar.make(binding.root, message, Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.ok) {}
                .show()
    }

}