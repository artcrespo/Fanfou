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

package com.sinyuk.fanfou.ui.account

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.sinyuk.fanfou.R
import com.sinyuk.fanfou.abstracts.AbstractActivity
import com.sinyuk.fanfou.ui.HomeActivity
import com.sinyuk.fanfou.utils.addFragmentInActivity
import com.sinyuk.fanfou.utils.obtainViewModel
import com.sinyuk.fanfou.viewmodels.ViewModelFactory
import javax.inject.Inject

/**
 * Created by sinyuk on 2017/11/28.
 */
class SignActivity : AbstractActivity() {
    companion object {
        @JvmStatic
        fun start(context: Context) {
            val intent = Intent(context, SignActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun beforeInflate() {
    }

    override fun layoutId(): Int? = R.layout.abstract_activity


    @Inject lateinit var factory: ViewModelFactory

    private lateinit var accountViewModel: AccountViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        accountViewModel = obtainViewModel(factory, AccountViewModel::class.java)

        addFragmentInActivity(SignInView(), R.id.fragment_container, false)
    }
}