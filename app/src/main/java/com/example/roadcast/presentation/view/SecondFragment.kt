package com.example.roadcast.presentation.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.roadcast.data.util.Resource
import com.example.roadcast.databinding.FragmentSecondBinding
import com.example.roadcast.presentation.viewModel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SecondFragment : Fragment() {
    companion object {
        private val TAG: String? = SecondFragment::class.java.simpleName
    }
    private var _binding: FragmentSecondBinding? = null
    private lateinit var viewModel: MainViewModel
    private val binding get() = _binding!!
    private var isLoading = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        viewModel.getDogImage()
        getData()
        binding.btnRandom.setOnClickListener {
            viewModel.getDogImage()
        }
    }

    private fun changeProgress(value: Boolean) {
        isLoading = value
        binding.progressBar.visibility = if (value) View.VISIBLE else View.GONE
    }

    private fun getData() {
        viewModel.dogLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    changeProgress(false)
                    context?.let { it1 ->
                        Glide.with(it1).load(it.data?.message).into(binding.ivImage)
                    }
                }
                is Resource.Error -> {
                    changeProgress(false)
                    Log.e(TAG, "getData: ${it.message}")
                    it.message?.let { message ->
                        Toast.makeText(activity, "An error occurred $message", Toast.LENGTH_LONG)
                            .show()
                    }
                }
                is Resource.Loading -> {
                    changeProgress(true)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}