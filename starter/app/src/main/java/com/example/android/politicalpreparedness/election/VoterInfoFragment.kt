package com.example.android.politicalpreparedness.election

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.android.politicalpreparedness.data.ServiceLocator
import com.example.android.politicalpreparedness.databinding.FragmentVoterInfoBinding

class VoterInfoFragment : Fragment() {

    private val _viewModel: VoterInfoViewModel by viewModels {
        val repo = ServiceLocator.provideVoterInfoRepository(requireContext())
        VoterInfoViewModelFactory(repo)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        val binding = FragmentVoterInfoBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = _viewModel

        val args = VoterInfoFragmentArgs.fromBundle(requireArguments())
        _viewModel.load(args)

        //TODO: Handle loading of URLs

        return binding.root
    }

    private fun openUrl(url: String?) {

    }

}