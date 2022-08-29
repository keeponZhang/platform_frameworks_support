/*
 * Copyright 2018 The Android Open Source Project
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

package androidx.build

import java.io.File
import java.util.Properties

/**
 * Writes the appropriate SDK path to local.properties file.
 */
fun setSdkInLocalPropertiesFile(supportRoot: File) {
    setSdkInLocalPropertiesFile(supportRoot, supportRoot)
}

/**
 * Writes the appropriate SDK path to local.properties file in specified location.
 */
fun setSdkInLocalPropertiesFile(supportRoot: File, propertiesFile: File) {
    val sdkPath = getSdkPath(supportRoot)
    println("setSdkInLocalPropertiesFile sdkPath =$sdkPath")
    if (sdkPath.exists()) {
        val props = File(propertiesFile, "local.properties")
        // gradle always deliminate directories with '/' regardless of the OS.
        // So convert deliminator here.
        val gradlePath = sdkPath.absolutePath.replace(File.separator, "/")
        val expectedContents = "sdk.dir=$gradlePath"
        if (!props.exists() || props.readText(Charsets.UTF_8).trim() != expectedContents) {
            props.printWriter().use { out ->
                out.println(expectedContents)
            }
            println("updated local.properties")
        }
    } else {
        throw Exception("You are using non androidx-master-dev checkout. You need to check out " +
                "androidx-master-dev to work on support library. See go/androidx for details.")
    }
}

/**
 * Returns the appropriate SDK path.
 */
fun getSdkPath(supportRoot: File): File {
    if (true) {
        val properties = Properties()
        val propertiesFile = File(supportRoot, "local.properties")
        if (propertiesFile.exists()) {
            propertiesFile.inputStream().use(properties::load)
        }
        return findSdkLocation(properties, supportRoot)
    } else {
        val osName = System.getProperty("os.name").toLowerCase()
        val isMacOsX = osName.contains("mac os x") || osName.contains("darwin") ||
                osName.contains("osx")
        val platform = if (isMacOsX) "darwin" else "linux"
        // Making an assumption that prebuilts directory is in ../../prebuilts/
        return File(supportRoot.parentFile.parentFile, "prebuilts/fullsdk-$platform")
    }
}

/**
 * Adapted from com.android.build.gradle.internal.SdkHandler
 */
private fun findSdkLocation(properties: Properties, rootDir: File): File {
    var sdkDirProp = properties.getProperty("sdk.dir")
    if (sdkDirProp != null) {
        var sdk = File(sdkDirProp)
        if (!sdk.isAbsolute()) {
            sdk = File(rootDir, sdkDirProp)
        }
        return sdk
    }

    sdkDirProp = properties.getProperty("android.dir")
    if (sdkDirProp != null) {
        return File(rootDir, sdkDirProp)
    }

    val envVar = System.getenv("ANDROID_HOME")
    if (envVar != null) {
        return File(envVar)
    }

    val property = System.getProperty("android.home")
    if (property != null) {
        return File(property)
    }
    throw Exception("Could not find your SDK")
}

private fun isUnbundledBuild(supportRoot: File): Boolean {
    return (File(supportRoot, "unbundled-build")).exists()
}