package top.iqqcode.lib.common.router

import androidx.annotation.StringDef
import top.iqqcode.lib.common.router.RouterAbility.Companion.ANIMATIONS_MAIN
import top.iqqcode.lib.common.router.RouterAbility.Companion.ANIMATIONS_PATH_ANIMATOR
import top.iqqcode.lib.common.router.RouterAbility.Companion.COMPONENTS_CUSTOM_TEXT
import top.iqqcode.lib.common.router.RouterAbility.Companion.COMPONENTS_MAIN
import top.iqqcode.lib.common.router.RouterAbility.Companion.CORE_MAIN_FRAME
import top.iqqcode.lib.common.router.RouterAbility.Companion.LAYOUT_MAIN

/**
 * 路由表(main)
 *
 * @constructor Create empty Router ability
 */
@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.FIELD, AnnotationTarget.FUNCTION)
@MustBeDocumented
@Retention(AnnotationRetention.SOURCE)
@StringDef(
    ANIMATIONS_MAIN,
    ANIMATIONS_PATH_ANIMATOR,
    COMPONENTS_MAIN,
    COMPONENTS_CUSTOM_TEXT,
    LAYOUT_MAIN,
    CORE_MAIN_FRAME
)
annotation class RouterAbility {
    companion object {

        // animations
        const val ANIMATIONS_MAIN = "/animations/AnimationMainActivity"
        const val ANIMATIONS_PATH_ANIMATOR = "/animations/PathAnimatorActivity"


        // components
        const val COMPONENTS_MAIN = "/components/ComponentsActivity"
        const val COMPONENTS_CUSTOM_TEXT = "/components/CustomTextActivity"
        const val COMPONENTS_DOCK_BAR = "/components/DockBarActivity"
        const val COMPONENTS_EASTER_AGREE = "/components/EasterAgreeActivity"


        // layout
        const val LAYOUT_MAIN = "/layout/LayoutActivity"

        // main
        const val CORE_MAIN_FRAME = "/core/CoreMainActivity"
    }
}