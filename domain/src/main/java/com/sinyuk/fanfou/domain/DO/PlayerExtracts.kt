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

package com.sinyuk.fanfou.domain.DO

import android.arch.persistence.room.Ignore
import android.os.Parcel
import android.os.Parcelable

/**
 * Created by sinyuk on 2017/12/1.
 *
 */
data class PlayerExtracts @JvmOverloads constructor(
        var uniqueId: String = "",
        var id: String = "",
        var screenName: String? = "",
        var profileImageUrl: String? = "",
        var profileImageUrlLarge: String? = "",
        var profileBackgroundImageUrl: String? = "") : Parcelable {
    @Ignore constructor(player: Player) : this(
            uniqueId = player.uniqueId,
            id = player.id,
            screenName = player.screenName,
            profileImageUrl = player.profileImageUrl,
            profileImageUrlLarge = player.profileImageUrlLarge,
            profileBackgroundImageUrl = player.profileBackgroundImageUrl
    )

    constructor(source: Parcel) : this(
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(uniqueId)
        writeString(id)
        writeString(screenName)
        writeString(profileImageUrl)
        writeString(profileImageUrlLarge)
        writeString(profileBackgroundImageUrl)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<PlayerExtracts> = object : Parcelable.Creator<PlayerExtracts> {
            override fun createFromParcel(source: Parcel): PlayerExtracts = PlayerExtracts(source)
            override fun newArray(size: Int): Array<PlayerExtracts?> = arrayOfNulls(size)
        }
    }
}