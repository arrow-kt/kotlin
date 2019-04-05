/*
 * Copyright 2010-2019 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license
 * that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.codegen;

import com.intellij.testFramework.TestDataPath;
import org.jetbrains.kotlin.test.JUnit3RunnerWithInners;
import org.jetbrains.kotlin.test.KotlinTestUtils;
import org.jetbrains.kotlin.test.TargetBackend;
import org.jetbrains.kotlin.test.TestMetadata;
import org.junit.runner.RunWith;

import java.io.File;
import java.util.regex.Pattern;

/** This class is generated by {@link org.jetbrains.kotlin.generators.tests.TestsPackage}. DO NOT MODIFY MANUALLY */
@SuppressWarnings("all")
@TestMetadata("compiler/testData/codegen/customScript")
@TestDataPath("$PROJECT_ROOT")
@RunWith(JUnit3RunnerWithInners.class)
public class CustomScriptCodegenTestGenerated extends AbstractCustomScriptCodegenTest {
    private void runTest(String testDataFilePath) throws Exception {
        KotlinTestUtils.runTest(this::doTest, TargetBackend.ANY, testDataFilePath);
    }

    public void testAllFilesPresentInCustomScript() throws Exception {
        KotlinTestUtils.assertAllTestsPresentByMetadata(this.getClass(), new File("compiler/testData/codegen/customScript"), Pattern.compile("^(.+)\\.kts$"), TargetBackend.ANY, true);
    }

    @TestMetadata("simpleEnvVars.kts")
    public void testSimpleEnvVars() throws Exception {
        runTest("compiler/testData/codegen/customScript/simpleEnvVars.kts");
    }

    @TestMetadata("stringReceiver.kts")
    public void testStringReceiver() throws Exception {
        runTest("compiler/testData/codegen/customScript/stringReceiver.kts");
    }
}
