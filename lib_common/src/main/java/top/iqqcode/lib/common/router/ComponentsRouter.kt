package top.iqqcode.lib.common.router

import androidx.annotation.StringDef
import top.iqqcode.lib.common.router.ComponentsRouter.Companion.FRS_FLOAT_ENTRANCE_ACTIVITY

/**
 * Components router
 *
 * @constructor Create empty Components router
 */

@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.FIELD, AnnotationTarget.FUNCTION)
@MustBeDocumented
@Retention(AnnotationRetention.SOURCE)
@StringDef(FRS_FLOAT_ENTRANCE_ACTIVITY)
annotation class ComponentsRouter {
    companion object {
        const val FRS_FLOAT_ENTRANCE_ACTIVITY = "/components/FrsFloatEntranceActivity"
    }
}