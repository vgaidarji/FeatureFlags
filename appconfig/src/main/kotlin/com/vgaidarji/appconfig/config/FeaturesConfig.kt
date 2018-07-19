package com.vgaidarji.appconfig.config

import com.vgaidarji.appconfig.feature.Feature
import java.io.Serializable

abstract class FeaturesConfig : Serializable {
    abstract fun features(): List<Feature>
}
