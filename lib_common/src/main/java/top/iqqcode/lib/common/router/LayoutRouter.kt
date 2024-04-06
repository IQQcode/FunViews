package top.iqqcode.lib.common.router

import androidx.annotation.StringDef
import top.iqqcode.lib.common.router.LayoutRouter.Companion.LAYOUT_ACTIVITY

/**
 * Layout router
 *
 * @constructor Create empty Layout router
 */
@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.FIELD, AnnotationTarget.FUNCTION)
@MustBeDocumented
@Retention(AnnotationRetention.SOURCE)
@StringDef(LAYOUT_ACTIVITY)
annotation class LayoutRouter {
    companion object {
        const val LAYOUT_ACTIVITY = "/layout/LayoutActivity"
    }
}