package fr.nicopico.base.android.dependency.injection

import androidx.annotation.MainThread
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelStoreOwner

@MainThread
inline fun <reified VM : ViewModel> Fragment.injectedViewModels(
    noinline ownerProducer: () -> ViewModelStoreOwner = { this }
) = this.viewModels<VM>(ownerProducer, ViewModelFactoryProducerHolder.injectedFactoryProducer)

inline fun <reified VM : ViewModel> Fragment.injectedActivityViewModels() =
    this.activityViewModels<VM>(ViewModelFactoryProducerHolder.injectedFactoryProducer)
