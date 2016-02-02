/**
 * Copyright (C) 2016 The Android Open Source Project
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

package android.support.v4.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.view.PointerIcon;

class PointerIconCompatApi24 {
    public static Object getSystemIcon(Context context, int style) {
        return PointerIcon.getSystemIcon(context, style);
    }

    public static Object createCustomIcon(Bitmap bitmap, float hotSpotX, float hotSpotY) {
        return PointerIcon.createCustomIcon(bitmap, hotSpotX, hotSpotY);
    }

    public static Object loadCustomIcon(Resources resources, int resourceId) {
        return PointerIcon.loadCustomIcon(resources, resourceId);
    }
}
