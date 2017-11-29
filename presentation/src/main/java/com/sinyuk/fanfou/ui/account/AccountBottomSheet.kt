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

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.view.animation.FastOutSlowInInterpolator
import android.support.v7.widget.AppCompatRadioButton
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.daimajia.swipe.SimpleSwipeListener
import com.daimajia.swipe.SwipeLayout
import com.sinyuk.fanfou.R
import com.sinyuk.fanfou.abstracts.AbstracBottomSheetFragment
import com.sinyuk.fanfou.domain.entities.User
import com.sinyuk.fanfou.injections.Injectable
import com.sinyuk.fanfou.lives.AutoClearedValue
import com.sinyuk.fanfou.utils.QuickSwipeAdapter
import com.sinyuk.fanfou.utils.obtainViewModel
import com.sinyuk.fanfou.viewmodels.ViewModelFactory
import kotlinx.android.synthetic.main.account_bottomsheet.*
import kotlinx.android.synthetic.main.account_selectable_list_footer.view.*
import javax.inject.Inject

/**
 * Created by sinyuk on 2017/11/28.
 */
class AccountBottomSheet : AbstracBottomSheetFragment(), Injectable {

    override fun layoutId(): Int? = R.layout.account_bottomsheet


    @Inject lateinit var factory: ViewModelFactory

    private lateinit var accountViewModel: AccountViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListView()

        accountViewModel = obtainViewModel(factory, AccountViewModel::class.java).apply { allAccounts.observe(this@AccountBottomSheet, accountsOB) }
    }

    private lateinit var adapter: AutoClearedValue<AccountAdapter>

    private fun initListView() {
        val layoutManager = LinearLayoutManager(context)
        layoutManager.isAutoMeasureEnabled = true
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)
        val accountAdapter = AccountAdapter(R.layout.account_selectable_list_item, null)
        accountAdapter.onItemChildClickListener = BaseQuickAdapter.OnItemChildClickListener { _, view, position ->
            when (view.id) {
                R.id.checkbox -> adapter.get()?.checkedPosition = position
                R.id.deleteButton -> onDelete()
            }
        }
        val footer: View = LayoutInflater.from(context).inflate(R.layout.account_selectable_list_footer, recyclerView, false)
        footer.addNewAccount.setOnClickListener { addNewAccount() }
        accountAdapter.addFooterView(footer)
        accountAdapter.setHeaderFooterEmpty(false, true)
        recyclerView.adapter = accountAdapter
        adapter = AutoClearedValue(this, accountAdapter)

    }

    private fun addNewAccount() {

    }

    private fun onDelete() {

    }

    @Suppress("SENSELESS_COMPARISON")
    private val accountsOB: Observer<List<User>> = Observer { t ->
        // TODO: 要求当前登录用户在第一位
        if (adapter.get() != null) {
            adapter.get()!!.setNewData(t)
        }
    }

    class AccountAdapter constructor(res: Int, data: List<User>?) : QuickSwipeAdapter<User, BaseViewHolder>(res, data) {
        override fun getSwipeLayoutResourceId(position: Int): Int = R.id.swipeLayout

        override fun convert(helper: BaseViewHolder, item: User?) {
            helper.setText(R.id.id, item?.id)
            helper.setText(R.id.screenName, "@" + item?.screenName)
            helper.getView<AppCompatRadioButton>(R.id.checkbox).isChecked = checkedPosition == helper.adapterPosition
            helper.getView<SwipeLayout>(R.id.swipeLayout).isSwipeEnabled = checkedPosition != helper.adapterPosition
            helper.getView<SwipeLayout>(R.id.swipeLayout).showMode = SwipeLayout.ShowMode.PullOut
            helper.getView<SwipeLayout>(R.id.swipeLayout).addSwipeListener(object : SimpleSwipeListener() {
                override fun onOpen(layout: SwipeLayout?) {
                    layout?.findViewById<View>(R.id.deleteIcon)?.apply {
                        YoYo.with(Techniques.Tada).interpolate(FastOutSlowInInterpolator()).duration(500).delay(500).playOn(this)
                    }
                }
            })
            helper.addOnClickListener(R.id.checkbox)
            helper.addOnClickListener(R.id.deleteButton)

        }

        var checkedPosition = 0
            set(value) {
                if (field != value && value in 0..(itemCount - 1)) {
                    val temp = field
                    field = value
                    notifyItemChanged(value)
                    if (temp != RecyclerView.NO_POSITION) {
                        notifyItemChanged(temp)
                    }
                }
            }

    }
}