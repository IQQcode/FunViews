package top.iqqcode.lib.common.router

import androidx.annotation.StringDef
import top.iqqcode.lib.common.router.AnimationRouter.Companion.NATURAL_ALPHA_ACTIVITY

/**
 * Animation router
 *
 * @constructor Create empty Animation router
 */

@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.FIELD, AnnotationTarget.FUNCTION)
@MustBeDocumented
@Retention(AnnotationRetention.SOURCE)
@StringDef(NATURAL_ALPHA_ACTIVITY)
annotation class AnimationRouter {
    companion object {
        const val NATURAL_ALPHA_ACTIVITY = "/animations/NaturalAlphaActivity"
    }
}