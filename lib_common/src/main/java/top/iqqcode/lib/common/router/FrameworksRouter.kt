package top.iqqcode.lib.common.router

import androidx.annotation.StringDef
import top.iqqcode.lib.common.router.FrameworksRouter.Companion.HANDLERS_WORKS_ACTIVITY


@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.FIELD, AnnotationTarget.FUNCTION)
@MustBeDocumented
@Retention(AnnotationRetention.SOURCE)
@StringDef(HANDLERS_WORKS_ACTIVITY)
annotation class FrameworksRouter {
    companion object {

        const val HANDLERS_WORKS_ACTIVITY = "/handlers/FrameworksMainActivity"
    }
}