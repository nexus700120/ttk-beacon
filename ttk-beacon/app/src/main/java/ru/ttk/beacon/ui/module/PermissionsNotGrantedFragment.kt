package ru.ttk.beacon.ui.module

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.fragment.app.Fragment
import org.koin.android.ext.android.inject
import org.koin.core.qualifier.qualifier
import ru.terrakok.cicerone.Router
import ru.ttk.beacon.R
import ru.ttk.beacon.ui.navigation.RouterType
import ru.ttk.beacon.ui.navigation.Screen
import ru.ttk.beacon.ui.utils.BleHelper

class PermissionsNotGrantedFragment : Fragment(R.layout.fragment_permissions_not_granted) {

    private val helper by inject<BleHelper>()
    private val router by inject<Router>(RouterType.FULL_SCREEN.qualifier)

    private var requestTimestamp = 0L

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<View>(R.id.request_permissions).setOnClickListener {
            if (!helper.isPermissionsGranted) {
                requestTimestamp = System.currentTimeMillis()
                requestPermissions(
                    helper.requiredPermissions,
                    PERMISSIONS_REQUEST_CODE
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (helper.isPermissionsGranted) {
            router.newRootScreen(Screen.BeaconList)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (grantResults.any { it != PackageManager.PERMISSION_GRANTED }) {
            if (System.currentTimeMillis() - requestTimestamp < DO_NOT_ASK_AGAIN_TIMEOUT) {
                // do not ask again
                val intent = Intent(
                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.parse("package:" + requireContext().packageName)
                ).apply { addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) }
                startActivity(intent)
            }
        } else {
            router.newRootScreen(Screen.BeaconList)
        }
    }

    companion object {
        private const val PERMISSIONS_REQUEST_CODE = 301
        private const val DO_NOT_ASK_AGAIN_TIMEOUT = 1000L
    }
}