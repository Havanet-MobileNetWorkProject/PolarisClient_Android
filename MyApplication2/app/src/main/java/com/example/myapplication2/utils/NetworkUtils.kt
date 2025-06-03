package com.example.myapplication2.utils

import android.content.Context
import android.telephony.*
import androidx.core.app.ActivityCompat

object NetworkUtils {

    fun getNetworkTypeName(type: Int): String {
        return when (type) {
            TelephonyManager.NETWORK_TYPE_LTE -> "LTE"
            TelephonyManager.NETWORK_TYPE_NR -> "5G"
            TelephonyManager.NETWORK_TYPE_HSPAP -> "HSPA+"
            TelephonyManager.NETWORK_TYPE_HSPA -> "HSPA"
            TelephonyManager.NETWORK_TYPE_UMTS -> "UMTS"
            TelephonyManager.NETWORK_TYPE_EDGE -> "EDGE"
            TelephonyManager.NETWORK_TYPE_GPRS -> "GPRS"
            TelephonyManager.NETWORK_TYPE_GSM -> "GSM"
            else -> "نامشخص"
        }
    }

    fun getCellInfoText(context: Context): String {
        if (!PermissionsUtils.hasAllPermissions(context)) return "مجوزهای لازم داده نشده است."

        val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

        val cellInfoList = telephonyManager.allCellInfo ?: return "هیچ اطلاعات سلولی موجود نیست."
        val sb = StringBuilder()

        for (cellInfo in cellInfoList) {
            if (!cellInfo.isRegistered) continue

            when (val identity = when (cellInfo) {
                is CellInfoLte -> cellInfo.cellIdentity
                is CellInfoGsm -> cellInfo.cellIdentity
                is CellInfoWcdma -> cellInfo.cellIdentity
                else -> null
            }) {
                is CellIdentityLte -> {
                    sb.append("\nLTE Cell:")
                    sb.append("\nTAC: ${identity.tac}")
                    sb.append("\nCI: ${identity.ci}")
                    sb.append("\nPCI: ${identity.pci}")
                }
                is CellIdentityGsm -> {
                    sb.append("\nGSM Cell:")
                    sb.append("\nLAC: ${identity.lac}")
                    sb.append("\nCID: ${identity.cid}")
                }
                is CellIdentityWcdma -> {
                    sb.append("\nWCDMA Cell:")
                    sb.append("\nLAC: ${identity.lac}")
                    sb.append("\nCID: ${identity.cid}")
                }
            }
        }

        return if (sb.isEmpty()) "اطلاعات سلولی ثبت نشده است." else sb.toString()
    }
}
