/*
 * Copyright 2014 NAVER Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.navercorp.pinpoint.profiler.sampler;

import com.navercorp.pinpoint.bootstrap.sampler.Sampler;

/**
 * @author emeroad
 * @author yjqg6666
 */
public class TrueSampler implements Sampler {
    public static final Sampler INSTANCE = new TrueSampler();

    private TrueSampler() {
    }

    @Override
    public boolean isSampling() {
        return true;
    }

    @Override
    public int getSamplingRate() {
        return 100;
    }

    @Override
    public void updateSamplingRate(int rate) {
        //do nothing
    }

    @Override
    public String toString() {
        // To fix sampler name even if the class name is changed.
        return "TrueSampler";
    }
}
