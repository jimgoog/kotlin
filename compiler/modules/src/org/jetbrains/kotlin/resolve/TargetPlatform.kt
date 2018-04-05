/*
 * Copyright 2010-2018 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license
 * that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.resolve

import com.intellij.openapi.components.ServiceManager
import org.jetbrains.kotlin.container.StorageComponentContainer
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.storage.LockBasedStorageManager
import java.util.*

abstract class TargetPlatform(val platformName: String) {
    override fun toString() = platformName

    abstract val platformConfigurator: PlatformConfigurator


    abstract fun getDefaultImports(includeKotlinComparisons: Boolean): List<ImportPath>
    open val excludedImports: List<FqName> get() = emptyList()

    abstract val multiTargetPlatform: MultiTargetPlatform

    object Common : TargetPlatform("Default") {
        private val defaultImports =
            LockBasedStorageManager()
                .createMemoizedFunction<Boolean, List<ImportPath>> { includeKotlinComparisons ->
                    ArrayList<ImportPath>().apply {
                        listOf(
                            "kotlin.*",
                            "kotlin.annotation.*",
                            "kotlin.collections.*",
                            "kotlin.ranges.*",
                            "kotlin.sequences.*",
                            "kotlin.text.*",
                            "kotlin.io.*"
                        ).forEach { add(ImportPath.fromString(it)) }

                        if (includeKotlinComparisons) {
                            add(ImportPath.fromString("kotlin.comparisons.*"))
                        }
                    }
                }

        override fun getDefaultImports(includeKotlinComparisons: Boolean): List<ImportPath> = defaultImports(includeKotlinComparisons)

        override val platformConfigurator: PlatformConfigurator
            get() = ServiceManager.getService(CommonPlatformConfigurator::class.java)

        override val multiTargetPlatform: MultiTargetPlatform
            get() = MultiTargetPlatform.Common
    }
}

interface PlatformConfigurator {
    val platformSpecificContainer: StorageComponentContainer
    fun configureModuleComponents(container: StorageComponentContainer)
}

interface CommonPlatformConfigurator : PlatformConfigurator