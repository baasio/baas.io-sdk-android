/*
 * Copyright 2012 Google Inc.
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

package com.kth.baasio.test.gcm;

import android.content.Context;

import com.google.android.gcm.GCMBroadcastReceiver;
import com.kth.baasio.Baas;

/**
 * @author trevorjohns@google.com (Trevor Johns)
 */
public class GCMRedirectedBroadcastReceiver extends GCMBroadcastReceiver {

    /**
     * Gets the class name of the intent service that will handle GCM messages.
     * Used to override class name, so that GCMIntentService can live in a
     * subpackage.
     */
    @Override
    protected String getGCMIntentServiceClassName(Context context) {
        if(Baas.io().isGcmEnabled())
            return GCMIntentService.class.getCanonicalName();
        else
            return null;
    }
}
