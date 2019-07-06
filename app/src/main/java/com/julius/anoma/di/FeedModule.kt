package com.julius.anoma.di

import com.julius.anoma.repository.MockRepository
import com.julius.anoma.repository.Repository
import com.julius.anoma.ui.MainActivityViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val feedModule = module(override = true) {
    single { MockRepository() as Repository }
    viewModel { MainActivityViewModel(get()) }
}