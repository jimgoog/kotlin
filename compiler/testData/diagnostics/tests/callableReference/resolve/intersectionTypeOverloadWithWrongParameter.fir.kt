// !WITH_NEW_INFERENCE
// !DIAGNOSTICS: -UNUSED_PARAMETER

import kotlin.reflect.KCallable

class Foo {
    fun <T> installRoute(handler: T) where T : (String) -> Any?, T : KCallable<*> {
    }

    fun <T> installRoute(handler: T) where T : () -> Any?, T : KCallable<*> {
    }

    fun foo() {
        <!INAPPLICABLE_CANDIDATE!>installRoute<!><Any>(::route)
    }

}

fun route(s: String): Any? = null