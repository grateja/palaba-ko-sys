package com.csi.palabakosys.preferences

class IpSettings (
    var networkPrefix: String,
    var subnetId: Int,
) {
    fun toString(hostId: Int) : String {
        return "$networkPrefix.$subnetId.$hostId"
    }
}