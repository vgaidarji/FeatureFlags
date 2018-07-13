package com.vgaidarji.appconfig.feature

import com.vgaidarji.appconfig.config.FeaturesConfig

/**
 * Describes which features in application could be configured via configuration.
 */
open class ConfigurableFeatures(private val config: FeaturesConfig) {
    enum class SupportedFeatures(val featureName: String) {
        BOTTOM_NAVIGATION("bottom_navigation")
    }

    fun hasBottomNavigation(): Boolean {
        return hasFeature(SupportedFeatures.BOTTOM_NAVIGATION)
    }

    private fun hasFeature(feature: SupportedFeatures): Boolean {
        return config.features().firstOrNull {
            it.name == feature.featureName
        }?.isEnabled ?: false
    }
}
