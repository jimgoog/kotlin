/*
 * Copyright 2000-2018 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license
 * that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.resolve

import com.intellij.openapi.util.Key
import org.jetbrains.kotlin.container.StorageComponentContainer
import org.jetbrains.kotlin.container.composeContainer
import org.jetbrains.kotlin.container.useInstance
import org.jetbrains.kotlin.platform.PlatformToKotlinClassMap
import org.jetbrains.kotlin.psi.KtFile
import org.jetbrains.kotlin.psi.UserDataProperty
import org.jetbrains.kotlin.resolve.calls.checkers.*
import org.jetbrains.kotlin.resolve.calls.results.TypeSpecificityComparator
import org.jetbrains.kotlin.resolve.checkers.*
import org.jetbrains.kotlin.resolve.lazy.DelegationFilter
import org.jetbrains.kotlin.resolve.scopes.SyntheticScopes
import org.jetbrains.kotlin.types.DynamicTypesSettings


class CommonPlatformConfiguratorImpl : CommonPlatformConfigurator, PlatformConfiguratorBase(
    DynamicTypesSettings(), listOf(), listOf(), listOf(), listOf(), listOf(),
    IdentifierChecker.Default, OverloadFilter.Default, PlatformToKotlinClassMap.EMPTY, DelegationFilter.Default,
    OverridesBackwardCompatibilityHelper.Default,
    DeclarationReturnTypeSanitizer.Default
) {
    override fun configureModuleComponents(container: StorageComponentContainer) {
        container.useInstance(SyntheticScopes.Empty)
        container.useInstance(TypeSpecificityComparator.NONE)
    }
}

private val DEFAULT_DECLARATION_CHECKERS = listOf(
    DataClassDeclarationChecker(),
    ConstModifierChecker,
    UnderscoreChecker,
    InlineParameterChecker,
    InfixModifierChecker(),
    SinceKotlinAnnotationValueChecker,
    RequireKotlinAnnotationValueChecker,
    ReifiedTypeParameterAnnotationChecker(),
    DynamicReceiverChecker,
    DelegationChecker(),
    KClassWithIncorrectTypeArgumentChecker,
    SuspendOperatorsCheckers,
    InlineClassDeclarationChecker
)

private val DEFAULT_CALL_CHECKERS = listOf(
    CapturingInClosureChecker(), InlineCheckerWrapper(), SafeCallChecker(),
    DeprecatedCallChecker, CallReturnsArrayOfNothingChecker(), InfixCallChecker(), OperatorCallChecker(),
    ConstructorHeaderCallChecker, ProtectedConstructorCallChecker, ApiVersionCallChecker,
    CoroutineSuspendCallChecker, BuilderFunctionsCallChecker, DslScopeViolationCallChecker, MissingDependencyClassChecker,
    CallableReferenceCompatibilityChecker(), LateinitIntrinsicApplicabilityChecker,
    UnderscoreUsageChecker, AssigningNamedArgumentToVarargChecker(),
    PrimitiveNumericComparisonCallChecker, LambdaWithSuspendModifierCallChecker
)
private val DEFAULT_TYPE_CHECKERS = emptyList<AdditionalTypeChecker>()
private val DEFAULT_CLASSIFIER_USAGE_CHECKERS = listOf(
    DeprecatedClassifierUsageChecker(), ApiVersionClassifierUsageChecker, MissingDependencyClassChecker.ClassifierUsage
)
private val DEFAULT_ANNOTATION_CHECKERS = listOf<AdditionalAnnotationChecker>(
    ExperimentalMarkerDeclarationAnnotationChecker
)


abstract class PlatformConfiguratorBase(
    private val dynamicTypesSettings: DynamicTypesSettings,
    additionalDeclarationCheckers: List<DeclarationChecker>,
    additionalCallCheckers: List<CallChecker>,
    additionalTypeCheckers: List<AdditionalTypeChecker>,
    additionalClassifierUsageCheckers: List<ClassifierUsageChecker>,
    additionalAnnotationCheckers: List<AdditionalAnnotationChecker>,
    private val identifierChecker: IdentifierChecker,
    private val overloadFilter: OverloadFilter,
    private val platformToKotlinClassMap: PlatformToKotlinClassMap,
    private val delegationFilter: DelegationFilter,
    private val overridesBackwardCompatibilityHelper: OverridesBackwardCompatibilityHelper,
    private val declarationReturnTypeSanitizer: DeclarationReturnTypeSanitizer
) : PlatformConfigurator {
    private val declarationCheckers: List<DeclarationChecker> = DEFAULT_DECLARATION_CHECKERS + additionalDeclarationCheckers
    private val callCheckers: List<CallChecker> = DEFAULT_CALL_CHECKERS + additionalCallCheckers
    private val typeCheckers: List<AdditionalTypeChecker> = DEFAULT_TYPE_CHECKERS + additionalTypeCheckers
    private val classifierUsageCheckers: List<ClassifierUsageChecker> =
        DEFAULT_CLASSIFIER_USAGE_CHECKERS + additionalClassifierUsageCheckers
    private val annotationCheckers: List<AdditionalAnnotationChecker> = DEFAULT_ANNOTATION_CHECKERS + additionalAnnotationCheckers

    override val platformSpecificContainer = composeContainer(this::class.java.simpleName) {
        useInstance(dynamicTypesSettings)
        declarationCheckers.forEach { useInstance(it) }
        callCheckers.forEach { useInstance(it) }
        typeCheckers.forEach { useInstance(it) }
        classifierUsageCheckers.forEach { useInstance(it) }
        annotationCheckers.forEach { useInstance(it) }
        useInstance(identifierChecker)
        useInstance(overloadFilter)
        useInstance(platformToKotlinClassMap)
        useInstance(delegationFilter)
        useInstance(overridesBackwardCompatibilityHelper)
        useInstance(declarationReturnTypeSanitizer)
    }
}

fun createContainer(id: String, platform: TargetPlatform, init: StorageComponentContainer.() -> Unit) =
    composeContainer(id, platform.platformConfigurator.platformSpecificContainer, init)


var KtFile.targetPlatform: TargetPlatform? by UserDataProperty(Key.create("TARGET_PLATFORM"))