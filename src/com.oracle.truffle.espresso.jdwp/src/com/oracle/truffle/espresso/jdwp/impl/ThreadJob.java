/*
 * Copyright (c) 2019, 2019, Oracle and/or its affiliates. All rights reserved.
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
package com.oracle.truffle.espresso.jdwp.impl;

import java.util.concurrent.Callable;

public final class ThreadJob {

    private final Object jobLock = new Object();
    private final Object thread;
    private final Callable<Object> callable;
    private boolean resultAvailable;
    private JobResult result;

    public ThreadJob(Object guestThread, Callable<Object> task) {
        this.thread = guestThread;
        this.callable = task;
    }

    public Object getThread() {
        return thread;
    }

    public void runJob() {
        result = new JobResult();
        try {
            result.setResult(callable.call());
        } catch (Throwable e) {
            result.setException(e);
        } finally {
            resultAvailable = true;
            synchronized (jobLock) {
                jobLock.notifyAll();
            }
        }
    }

    public JobResult getResult() {
        // let the job finish and return the result when available
        while (!resultAvailable && !Thread.currentThread().isInterrupted()) {
            synchronized (jobLock) {
                try {
                    jobLock.wait(10);
                } catch (InterruptedException e) {
                    // ignore this
                }
            }
        }
        return result;
    }

    public class JobResult {
        private Object result;
        private Throwable exception;

        public Object getResult() {
            return result;
        }

        public Throwable getException() {
            return exception;
        }

        private void setResult(Object obj) {
            this.result = obj;
        }

        private void setException(Throwable throwable) {
            this.exception = throwable;
        }
    }
}
