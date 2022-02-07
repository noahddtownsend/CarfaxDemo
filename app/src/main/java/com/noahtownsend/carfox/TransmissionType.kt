package com.noahtownsend.carfox

enum class TransmissionType(val friendlyName: String) {
    MANUAL("Manual"),
    AUTOMATIC("Automatic");

    override fun toString(): String {
        return friendlyName
    }
}