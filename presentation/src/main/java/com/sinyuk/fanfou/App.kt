/*
 *
 *  * Apache License
 *  *
 *  * Copyright [2017] Sinyuk
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *     http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package com.sinyuk.fanfou

import android.app.Activity
import android.app.Application
import android.util.Log
import com.facebook.stetho.Stetho
import com.sinyuk.fanfou.injections.AppInjector
import com.sinyuk.fanfou.utils.GlobalErrorHandler
import dagger.android.AndroidInjector
import dagger.android.HasActivityInjector
import dagger.android.DispatchingAndroidInjector
import io.reactivex.Completable
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


/**
 * Created by sinyuk on 2017/11/28.
 */
class App : Application(), HasActivityInjector {
    @Inject lateinit var dispatchingActivityInjector: DispatchingAndroidInjector<Activity>

    override fun activityInjector(): AndroidInjector<Activity> = dispatchingActivityInjector

    override fun onCreate() {
        super.onCreate()
        AppInjector.init(this)

        RxJavaPlugins.setErrorHandler(GlobalErrorHandler(this))

        initStetho()
    }

    private fun initStetho() {
        Completable.fromCallable({
            Stetho.initialize(Stetho.newInitializerBuilder(applicationContext)
                    .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(applicationContext))
                    .build())
        })
                .subscribeOn(Schedulers.io())
                .doOnError { it.printStackTrace() }
                .subscribe { Log.d("App: ", "initStetho") }
    }
}