/*
 * Copyright 2010-2015 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jetbrains.kotlin.jvm.compiler;

import com.intellij.openapi.util.io.FileUtil;
import junit.framework.TestCase;
import org.jetbrains.kotlin.cli.common.ExitCode;
import org.jetbrains.kotlin.cli.jvm.K2JVMCompiler;
import org.jetbrains.kotlin.codegen.forTestCompile.ForTestCompileRuntime;
import org.jetbrains.kotlin.load.kotlin.PackageClassUtils;
import org.jetbrains.kotlin.name.FqName;
import org.jetbrains.kotlin.test.JetTestUtils;
import org.junit.Assert;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

public class CompileEnvironmentTest extends TestCase {
    public void testSmokeWithCompilerJar() throws IOException {
        File tempDir = FileUtil.createTempDirectory("compilerTest", "compilerTest");

        try {
            File stdlib = ForTestCompileRuntime.runtimeJarForTests();
            File jdkAnnotations = JetTestUtils.getJdkAnnotationsJar();
            File resultJar = new File(tempDir, "result.jar");
            ExitCode rv = new K2JVMCompiler().exec(
                    System.out,
                    "-module", JetTestUtils.getTestDataPathBase() + "/compiler/smoke/Smoke.ktm",
                    "-d", resultJar.getAbsolutePath(),
                    "-no-stdlib",
                    "-classpath", stdlib.getAbsolutePath(),
                    "-no-jdk-annotations",
                    "-annotations", jdkAnnotations.getAbsolutePath()
            );
            Assert.assertEquals("compilation completed with non-zero code", ExitCode.OK, rv);
            FileInputStream fileInputStream = new FileInputStream(resultJar);
            try {
                JarInputStream is = new JarInputStream(fileInputStream);
                try {
                    List<String> entries = listEntries(is);
                    assertTrue(entries.contains("Smoke/" + PackageClassUtils.getPackageClassName(new FqName("Smoke")) + ".class"));
                    assertEquals(2, entries.size());
                }
                finally {
                    is.close();
                }
            }
            finally {
                fileInputStream.close();
            }
        }
        finally {
            FileUtil.delete(tempDir);
        }
    }

    public void testSmokeWithCompilerOutput() throws IOException {
        File tempDir = FileUtil.createTempDirectory("compilerTest", "compilerTest");
        try {
            File out = new File(tempDir, "out");
            File stdlib = ForTestCompileRuntime.runtimeJarForTests();
            File jdkAnnotations = JetTestUtils.getJdkAnnotationsJar();
            ExitCode exitCode = new K2JVMCompiler().exec(
                    System.out,
                    JetTestUtils.getTestDataPathBase() + "/compiler/smoke/Smoke.kt",
                    "-d", out.getAbsolutePath(),
                    "-no-stdlib",
                    "-classpath", stdlib.getAbsolutePath(),
                    "-no-jdk-annotations",
                    "-annotations", jdkAnnotations.getAbsolutePath()
            );
            Assert.assertEquals(ExitCode.OK, exitCode);
            assertEquals(1, out.listFiles().length);
            assertEquals(2, out.listFiles()[0].listFiles().length);
        }
        finally {
            FileUtil.delete(tempDir);
        }
    }

    private static List<String> listEntries(JarInputStream is) throws IOException {
        List<String> entries = new ArrayList<String>();
        while (true) {
            JarEntry jarEntry = is.getNextJarEntry();
            if (jarEntry == null) {
                break;
            }
            entries.add(jarEntry.getName());
        }
        return entries;
    }
}
