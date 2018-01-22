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

package com.sinyuk.fanfou.glide

import android.content.Context
import com.bumptech.glide.annotation.GlideExtension
import com.bumptech.glide.annotation.GlideOption
import com.bumptech.glide.request.RequestOptions
import com.sinyuk.myutils.ConvertUtils
import jp.wasabeef.glide.transformations.RoundedCornersTransformation

/**
 * Created by sinyuk on 2018/1/10.
 *
 */
@GlideExtension
object MyGlideExt {
    @GlideOption
    @JvmStatic
    fun avatar(options: RequestOptions) {
        options.optionalCircleCrop()
    }

    @GlideOption
    @JvmStatic
    fun illustrationThumb(options: RequestOptions, context: Context) {
        options.centerCrop().apply(RequestOptions.bitmapTransform(RoundedCornersTransformation(ConvertUtils.dp2px(context, 8f), 0)))
    }


    @GlideOption
    @JvmStatic
    fun illustrationLarge(options: RequestOptions, context: Context) {
        options.centerCrop().apply(RequestOptions.bitmapTransform(RoundedCornersTransformation(ConvertUtils.dp2px(context, 16f), 0)))
    }
}