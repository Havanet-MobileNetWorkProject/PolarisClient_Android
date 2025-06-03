package com.example.myapplication2.ui.home

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication2.databinding.FragmentHomeBinding
import com.example.myapplication2.utils.LocationUtils
import com.example.myapplication2.utils.NetworkUtils
import com.example.myapplication2.utils.PermissionsUtils

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var homeViewModel: HomeViewModel

    private val PERMISSION_REQUEST_CODE = 1000

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        val root = binding.root
        val textView = binding.textHome

        homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        if (PermissionsUtils.hasAllPermissions(requireContext())) {
            updateLocationAndCellInfo()
        } else {
            requestPermissions()
        }

        return root
    }

    private fun requestPermissions() {
        requestPermissions(
            PermissionsUtils.REQUIRED_PERMISSIONS,
            PERMISSION_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                updateLocationAndCellInfo()
            } else {
                homeViewModel.updateText("مجوزهای لازم داده نشده است.")
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    private fun updateLocationAndCellInfo() {
        LocationUtils.getCurrentLocation(requireContext()) { location ->
            if (location != null) {
                val lat = location.latitude
                val lon = location.longitude

                val telephonyManager = requireContext().getSystemService(android.content.Context.TELEPHONY_SERVICE) as android.telephony.TelephonyManager
                val networkType = NetworkUtils.getNetworkTypeName(telephonyManager.networkType)
                val cellInfoText = NetworkUtils.getCellInfoText(requireContext())

                val displayText = """
                    |📍 موقعیت مکانی:
                    |عرض: $lat
                    |طول: $lon
                    |
                    |📡 فناوری شبکه: $networkType
                    |
                    |🔢 شناسه‌های سلولی:
                    |$cellInfoText
                """.trimMargin()

                homeViewModel.updateText(displayText)

            } else {
                homeViewModel.updateText("عدم توانایی دریافت موقعیت مکانی")
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
