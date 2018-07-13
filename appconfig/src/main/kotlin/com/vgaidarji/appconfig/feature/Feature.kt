package com.vgaidarji.appconfig.feature

import java.io.Serializable

class Feature(
        val name: String = "",
        val isEnabled: Boolean = false
) : Serializable
