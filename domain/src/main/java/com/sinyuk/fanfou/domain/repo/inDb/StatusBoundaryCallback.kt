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

package com.sinyuk.fanfou.domain.repo.inDb

import android.arch.paging.PagedList
import android.support.annotation.MainThread
import com.android.paging.PagingRequestHelper
import com.sinyuk.fanfou.domain.AppExecutors
import com.sinyuk.fanfou.domain.DO.Status
import com.sinyuk.fanfou.domain.TIMELINE_HOME
import com.sinyuk.fanfou.domain.api.RestAPI
import com.sinyuk.fanfou.domain.util.createStatusLiveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * This boundary callback gets notified when user reaches to the edges of the list such that the
 * database cannot provide any more data.
 * <p>
 * The boundary callback might be called multiple times for the same direction so it does its own
 * rate limiting using the PagingRequestHelper class.
 */
class StatusBoundaryCallback(
        private val webservice: RestAPI,
        private val handleResponse: (MutableList<Status>?) -> Unit,
        private val appExecutors: AppExecutors,
        private val networkPageSize: Int)
    : PagedList.BoundaryCallback<Status>() {

    val helper = PagingRequestHelper(appExecutors.networkIO())
    val networkState = helper.createStatusLiveData()

    /**
     * Database returned 0 items. We should query the backend for more items.
     */
    @MainThread
    override fun onZeroItemsLoaded() {
        helper.runIfNotRunning(PagingRequestHelper.RequestType.INITIAL) {
            webservice.fetch_from_path(path = TIMELINE_HOME, count = networkPageSize).enqueue(createWebserviceCallback(it))
        }
    }

    /**
     * User reached to the end of the list.
     */
    @MainThread
    override fun onItemAtEndLoaded(itemAtEnd: Status) {
        helper.runIfNotRunning(PagingRequestHelper.RequestType.AFTER) {
          webservice.fetch_from_path(path = TIMELINE_HOME, count = networkPageSize, max = itemAtEnd.id).enqueue(createWebserviceCallback(it))
        }
    }

    /**
     * every time it gets new items, boundary callback simply inserts them into the database and
     * paging library takes care of refreshing the list if necessary.
     */
    private fun insertItemsIntoDb(
            response: Response<MutableList<Status>>,
            it: PagingRequestHelper.Request.Callback) {
        appExecutors.diskIO().execute {
            handleResponse(response.body())
            it.recordSuccess()
        }
    }

    override fun onItemAtFrontLoaded(itemAtFront: Status) {
        // ignored, since we only ever append to what's in the DB
    }

    private fun createWebserviceCallback(it: PagingRequestHelper.Request.Callback)
            : Callback<MutableList<Status>> {
        return object : Callback<MutableList<Status>> {
            override fun onFailure(
                    call: Call<MutableList<Status>>,
                    t: Throwable) {
                it.recordFailure(t)
            }

            override fun onResponse(
                    call: Call<MutableList<Status>>,
                    response: Response<MutableList<Status>>) {
                insertItemsIntoDb(response, it)
            }
        }
    }
}