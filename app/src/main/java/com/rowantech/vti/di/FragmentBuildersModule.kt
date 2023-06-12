/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.rowantech.vti.di


import com.rowantech.vti.views.*

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class FragmentBuildersModule {
    @ContributesAndroidInjector
    abstract fun contributeFragmentLogin(): FragmentLogin

    @ContributesAndroidInjector
    abstract fun contributeFragmentHome(): FragmentHome

    @ContributesAndroidInjector
    abstract fun contributeFragmentEditProfile(): FragmentEditProfile

    @ContributesAndroidInjector
    abstract fun contributeFragmentNotifications(): FragmentNotifications

    @ContributesAndroidInjector
    abstract fun contributeFragmentBrands(): FragmentBrands

    @ContributesAndroidInjector
    abstract fun contributeFragmentDescEvent(): FragmentDescEvent

    @ContributesAndroidInjector
    abstract fun contributeFragmentDialogNotifications(): FragmentDialogNotifications

    @ContributesAndroidInjector
    abstract fun contributeFragmentRegister(): FragmentRegister

    @ContributesAndroidInjector
    abstract fun contributeFragmentSearchEvent(): FragmentSearchEvent

    @ContributesAndroidInjector
    abstract fun contributeFragmentListEventOffline(): FragmentListEventOffline

    @ContributesAndroidInjector
    abstract fun contributeFragmentListEventOnline(): FragmentListEventOnline

    @ContributesAndroidInjector
    abstract fun contributeFragmentTabsEvent(): FragmentTabsEvent

    @ContributesAndroidInjector
    abstract fun contributeFragmentDetailEvent(): FragmentDetailEvent

    @ContributesAndroidInjector
    abstract fun contributeFragmentCreateTemplate(): FragmentCreateTemplate

    @ContributesAndroidInjector
    abstract fun contributeFragmentProductPayment(): FragmentProductPayment
    @ContributesAndroidInjector
    abstract fun contributeFragmentCreateInvoice(): FragmentCreateInvoice

    @ContributesAndroidInjector
    abstract fun contributeFragmentSuccessInvoice(): FragmentSuccessInvoice

}
