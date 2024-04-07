package top.iqqcode.lib.common.router

import androidx.annotation.StringDef
import top.iqqcode.lib.common.router.ComponentsRouter.Companion.CAROUSEL_DEMO_ACTIVITY
import top.iqqcode.lib.common.router.ComponentsRouter.Companion.EMOTION_LABEL_ACTIVITY
import top.iqqcode.lib.common.router.ComponentsRouter.Companion.EXPEND_BASE_DEMO_ACTIVITY
import top.iqqcode.lib.common.router.ComponentsRouter.Companion.EXPEND_BOX_ACTIVITY
import top.iqqcode.lib.common.router.ComponentsRouter.Companion.EXPEND_CLICK_ACTIVITY
import top.iqqcode.lib.common.router.ComponentsRouter.Companion.FLOAT_EXPEND_ACTIVITY
import top.iqqcode.lib.common.router.ComponentsRouter.Companion.FRS_FLOAT_ENTRANCE_ACTIVITY
import top.iqqcode.lib.common.router.ComponentsRouter.Companion.IM_GROUP_ENTRY_ACTIVITY
import top.iqqcode.lib.common.router.ComponentsRouter.Companion.SPREAD_ACTIVITY
import top.iqqcode.lib.common.router.ComponentsRouter.Companion.TABLE_SUB_LIST_ACTIVITY

/**
 * Components router 自定义View路由
 *
 * @constructor Create empty Components router
 */

@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.FIELD, AnnotationTarget.FUNCTION)
@MustBeDocumented
@Retention(AnnotationRetention.SOURCE)
@StringDef(
    FRS_FLOAT_ENTRANCE_ACTIVITY,
    EXPEND_BASE_DEMO_ACTIVITY,
    EXPEND_CLICK_ACTIVITY,
    SPREAD_ACTIVITY,
    CAROUSEL_DEMO_ACTIVITY,
    FLOAT_EXPEND_ACTIVITY,
    IM_GROUP_ENTRY_ACTIVITY,
    EXPEND_BOX_ACTIVITY,
    EMOTION_LABEL_ACTIVITY,
    TABLE_SUB_LIST_ACTIVITY
)
annotation class ComponentsRouter {
    companion object {
        const val FRS_FLOAT_ENTRANCE_ACTIVITY = "/components/FrsFloatEntranceActivity"
        const val EXPEND_BASE_DEMO_ACTIVITY = "/components/ExpendBaseDemoActivity"
        const val EXPEND_CLICK_ACTIVITY = "/components/ExpendClickActivity"
        const val SPREAD_ACTIVITY = "/components/SpreadActivity"
        const val CAROUSEL_DEMO_ACTIVITY = "/components/CarouselDemoActivity"
        const val FLOAT_EXPEND_ACTIVITY = "/components/FloatExpandActivity"
        const val IM_GROUP_ENTRY_ACTIVITY = "/components/IMGroupEntryActivity"

        const val EXPEND_BOX_ACTIVITY = "/components/ExpendBoxActivity"
        const val EMOTION_LABEL_ACTIVITY = "/components/EmotionLabelActivity"
        const val TABLE_SUB_LIST_ACTIVITY = "/components/TableSubListActivity"
    }
}