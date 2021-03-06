/*
 * Copyright (c) 2017, 2019, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */
package com.oracle.truffle.espresso.jni;

import com.oracle.truffle.espresso.ffi.NativeSignature;
import com.oracle.truffle.espresso.ffi.NativeType;
import com.oracle.truffle.espresso.meta.Meta;
import com.oracle.truffle.espresso.substitutions.SubstitutionProfiler;

public abstract class JniSubstitutor extends SubstitutionProfiler {

    public abstract static class Factory {
        public abstract JniSubstitutor create(Meta meta);

        private final String methodName;
        private final NativeSignature nativeSignature;
        private final int parameterCount;

        public Factory(String methodName, NativeSignature nativeSignature, int parameterCount) {
            this.methodName = methodName;
            this.nativeSignature = nativeSignature;
            this.parameterCount = parameterCount;
        }

        public final String methodName() {
            return methodName;
        }

        public final NativeSignature jniNativeSignature() {
            return nativeSignature;
        }

        public final int getParameterCount() {
            return parameterCount;
        }

        public final NativeType returnType() {
            return jniNativeSignature().getReturnType();
        }
    }

    public abstract Object invoke(JniEnv env, Object[] args);
}
