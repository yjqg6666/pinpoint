/*
 * Copyright 2024 NAVER Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.navercorp.pinpoint.metric.common.util;

import com.navercorp.pinpoint.common.timeseries.window.TimeWindow;

import java.util.List;

/**
 * @author minwoo-jung
 */
@Deprecated
public class TimeUtils {
    /**
     * @deprecated Since 3.1.0. Use {@link TimeWindow#getTimeseriesWindows()} instead.
     */
    @Deprecated
    public static List<Long> createTimeStampList(TimeWindow timeWindow) {
        return timeWindow.getTimeseriesWindows();
    }

}
