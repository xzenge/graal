/*
 * Copyright (c) 2018, 2018, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
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
package com.oracle.svm.core.windows;

import org.graalvm.nativeimage.Feature;
import org.graalvm.nativeimage.ImageSingletons;
import org.graalvm.nativeimage.Platform;
import org.graalvm.nativeimage.Platforms;
import org.graalvm.nativeimage.StackValue;
import org.graalvm.nativeimage.c.function.CEntryPointLiteral;
import org.graalvm.nativeimage.c.type.CCharPointer;
import org.graalvm.nativeimage.c.type.CTypeConversion;
import org.graalvm.nativeimage.impl.ProcessPropertiesSupport;
import org.graalvm.word.Pointer;
import org.graalvm.word.WordFactory;

import com.oracle.svm.core.annotate.AutomaticFeature;
import com.oracle.svm.core.util.VMError;
import com.oracle.svm.core.windows.headers.WinBase;

@Platforms(Platform.WINDOWS.class)
public class WindowsProcessPropertiesSupport implements ProcessPropertiesSupport {

    static final int MAX_PATH = 260;

    @Override
    public String getExecutableName() {
        CCharPointer path = StackValue.get(MAX_PATH, CCharPointer.class);
        Pointer hModule = WinBase.GetModuleHandleA(WordFactory.nullPointer());
        int result = WinBase.GetModuleFileNameA(hModule, path, MAX_PATH);
        return result == 0 ? null : CTypeConversion.toJavaString(path);
    }

    @Override
    public long getProcessID() {
        throw VMError.unimplemented();
    }

    @Override
    public long getProcessID(Process process) {
        throw VMError.unimplemented();
    }

    @Override
    public String getObjectFile(String symbol) {
        throw VMError.unimplemented();
    }

    @Override
    public String getObjectFile(CEntryPointLiteral<?> symbol) {
        throw VMError.unimplemented();
    }

    @Override
    public String setLocale(String category, String locale) {
        throw VMError.unimplemented();
    }

    @Override
    public boolean destroy(long processID) {
        throw VMError.unimplemented();
    }

    @Override
    public boolean destroyForcibly(long processID) {
        throw VMError.unimplemented();
    }

    @Override
    public boolean isAlive(long processID) {
        throw VMError.unimplemented();
    }

    @AutomaticFeature
    public static class ImagePropertiesFeature implements Feature {

        @Override
        public void afterRegistration(AfterRegistrationAccess access) {
            ImageSingletons.add(ProcessPropertiesSupport.class, new WindowsProcessPropertiesSupport());
        }
    }

}
