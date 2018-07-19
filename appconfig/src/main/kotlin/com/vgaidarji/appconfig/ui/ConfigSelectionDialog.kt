package com.vgaidarji.appconfig.ui

import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import com.vgaidarji.appconfig.R
import com.vgaidarji.appconfig.config.FeaturesConfig
import com.vgaidarji.appconfig.config.persistence.ConfigPersister
import com.vgaidarji.appconfig.extension.bindView
import java.io.Serializable

private const val FEATURES_LIST = "features_list"

class ConfigSelectionDialog : DialogFragment() {
    private val configsListView: ListView by bindView(R.id.configs_list_view)
    private val adapter: ArrayAdapter<String> by lazy {
        ArrayAdapter<String>(context, android.R.layout.simple_list_item_1)
    }
    private var configs: List<FeaturesConfig> = emptyList()
    private var configSelectedListener: ConfigSelectedListener? = null

    companion object {
        /**
         * @param features features configs list to be shown in dialog
         */
        fun newInstance(features: List<FeaturesConfig>) = ConfigSelectionDialog().apply {
            arguments = Bundle().apply {
                putSerializable(FEATURES_LIST, features as Serializable)
            }
        }
    }

    @Override
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.config_selection, container)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is ConfigSelectedListener) {
            configSelectedListener = context
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initConfigsFromArguments()
        initConfigsListView()
        addConfigsToAdapter()
    }

    private fun initConfigsFromArguments() {
        configs = arguments?.getSerializable(FEATURES_LIST) as List<FeaturesConfig>
    }

    private fun initConfigsListView() {
        configsListView.adapter = adapter
        configsListView.setOnItemClickListener { _, _, position, _ ->
            ConfigPersister(requireContext()).save(configs[position])
            configSelectedListener?.onConfigSelected(configs[position])
        }
    }

    private fun addConfigsToAdapter() {
        configs.forEach { adapter.add(it::class.java.simpleName) }
        adapter.notifyDataSetChanged()
    }
}
