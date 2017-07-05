/*
 * Copyright (c) 2016, Oracle and/or its affiliates. All rights reserved.
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
package jdk.tools.jlink.internal;

import java.nio.file.Path;
import java.util.Objects;
import jdk.tools.jlink.plugin.ModuleEntry;

public final class ModuleEntryFactory {
    private ModuleEntryFactory() {}

    public static ModuleEntry create(String path,
            ModuleEntry.Type type, byte[] content) {
        return new ByteArrayModuleEntry(moduleFrom(path), path, type, content);
    }

    public static ModuleEntry create(String path,
            ModuleEntry.Type type, Path file) {
        return new PathModuleEntry(moduleFrom(path), path, type, file);
    }

    public static ModuleEntry create(ModuleEntry original, byte[] content) {
        return new ByteArrayModuleEntry(original.getModule(),
                original.getPath(), original.getType(), content);
    }

    public static ModuleEntry create(ModuleEntry original, Path file) {
        return new PathModuleEntry(original.getModule(),
                original.getPath(), original.getType(), file);
    }

    static String moduleFrom(String path) {
        Objects.requireNonNull(path);
        if (path.isEmpty() || path.charAt(0) != '/') {
            throw new IllegalArgumentException(path + " must start with /");
        }
        int idx = path.indexOf('/', 1);
        if (idx == -1) {
            throw new IllegalArgumentException("/ missing after module: " + path);
        }
        return path.substring(1, idx);
    }

    static String packageFrom(String path) {
        Objects.requireNonNull(path);
        int idx = path.lastIndexOf('/');
        if (idx == -1) {
            throw new IllegalArgumentException("/ missing from path: " + path);
        }
        if (path.startsWith("/")) {
            int jdx = path.indexOf('/', 1);
            if (jdx == -1) {
                throw new IllegalArgumentException("/ missing after module: " + path);
            }
            return path.substring(jdx + 1, idx);
        } else {
            return path.substring(0, idx);
        }
    }
}
