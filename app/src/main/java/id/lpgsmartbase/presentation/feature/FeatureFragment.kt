package id.lpgsmartbase.presentation.feature

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import id.lpgsmartbase.R
import id.lpgsmartbase.databinding.FragmentFeatureBinding

class FeatureFragment : Fragment(R.layout.fragment_feature) {
    private var _binding: FragmentFeatureBinding? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentFeatureBinding.bind(view)
        _binding?.featureTitle?.text = requireArguments().getString(ARG_TITLE)
    }
    override fun onDestroyView() { _binding = null; super.onDestroyView() }
    companion object { const val ARG_TITLE = "title"; fun args(title: String) = bundleOf(ARG_TITLE to title) }
}
