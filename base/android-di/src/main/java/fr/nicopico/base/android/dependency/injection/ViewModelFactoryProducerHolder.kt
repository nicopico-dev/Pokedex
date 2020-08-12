package fr.nicopico.base.android.dependency.injection

import androidx.lifecycle.ViewModelProvider

private typealias FactoryProducer = () -> ViewModelProvider.Factory

object ViewModelFactoryProducerHolder {

    val injectedFactoryProducer: FactoryProducer
        get() = _factoryProducer
            ?: throw IllegalStateException("FactoryProducer must be initialized " +
                    "by calling ViewModelFactoryProducerHolder.init()")

    private var _factoryProducer: FactoryProducer? = null

    fun init(factoryProducer: FactoryProducer) {
        this._factoryProducer = factoryProducer
    }
}
