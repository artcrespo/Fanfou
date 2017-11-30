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

package com.sinyuk.fanfou.abstracts

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable


/**
 * Created by sinyuk on 2017/11/28.
 */
abstract class AbstractFragment : Fragment() {

    private val mCompositeDisposable = CompositeDisposable()

    protected abstract fun layoutId(): Int?

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return if (layoutId() != null) {
            inflater.inflate(layoutId()!!, container, false)
        } else {
            super.onCreateView(inflater, container, savedInstanceState)
        }
    }


    protected fun addDisposable(s: Disposable) {
        mCompositeDisposable.add(s)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (!mCompositeDisposable.isDisposed) {
            mCompositeDisposable.dispose()
        }
    }


}