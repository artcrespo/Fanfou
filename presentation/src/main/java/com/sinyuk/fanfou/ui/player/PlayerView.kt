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

package com.sinyuk.fanfou.ui.player

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.view.View
import com.sinyuk.fanfou.R
import com.sinyuk.fanfou.base.AbstractFragment
import com.sinyuk.fanfou.di.Injectable
import com.sinyuk.fanfou.domain.vo.Player
import com.sinyuk.fanfou.domain.vo.Resource
import com.sinyuk.fanfou.domain.vo.States
import com.sinyuk.fanfou.util.obtainViewModel
import com.sinyuk.fanfou.viewmodel.AccountViewModel
import com.sinyuk.fanfou.viewmodel.FanfouViewModelFactory
import com.sinyuk.myutils.system.ToastUtils
import kotlinx.android.synthetic.main.player_view_header.*
import javax.inject.Inject

/**
 * Created by sinyuk on 2017/12/7.
 */
class PlayerView : AbstractFragment(), Injectable {
    override fun layoutId() = R.layout.player_view


    @Inject lateinit var factory: FanfouViewModelFactory

    private val accountViewModel: AccountViewModel by lazy { obtainViewModel(factory, AccountViewModel::class.java) }

    @Inject lateinit var toast: ToastUtils

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (arguments?.getString("uniqueId") == null) {
            accountViewModel.user.observe(this@PlayerView, playerObserver)
        }

        avatarLayout.setOnClickListener {
            accountViewModel.repo.verifyCredentials(true)
        }
    }

    private val playerObserver by lazy { Observer<Resource<Player>> { subscribe(it) } }

    private fun subscribe(resource: Resource<Player>?) {
        resource?.let {
            when (it.states) {
                States.SUCCESS -> {
                    render(it.data)
                }
                States.ERROR -> {
                    it.message?.let { toast.toastShort(it) }
                }
                else -> {
                }
            }
        }
    }

    private fun render(player: Player?) {
        player?.let {
            screenName.text = it.screenName
            bio.text = it.description
            link.text = it.url
            followerCount.text = it.followersCount.toString()
            followingCount.text = it.friendsCount.toString()
        }
    }
}