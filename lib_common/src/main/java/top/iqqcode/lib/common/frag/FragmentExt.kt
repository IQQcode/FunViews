package top.iqqcode.funviews.base

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager


/**
 * @Author: jiazihui
 * @Date: 2023-02-26 21:22
 * @Description:
 */
object FragmentExt {

    /**
     * Add fragment
     *
     * @param fm
     * @param containerViewId
     * @param fragment
     * @return
     */
    fun addFragment(
        fm: FragmentManager,
        containerViewId: Int,
        fragment: OptFragment,
    ): OptFragment {
        fm.beginTransaction()
            .add(containerViewId, fragment, fragment.javaClass.canonicalName)
            .commitAllowingStateLoss()
        return fragment
    }

    /**
     * Find fragment by tag
     *
     * @param T
     * @param fm
     * @param clazz
     * @return
     */
    fun <T : Fragment?> findFragmentByTag(fm: FragmentManager, clazz: Class<T>): T? {
        return fm.findFragmentByTag(clazz.canonicalName) as T?
    }

    /**
     * Create fragment
     *
     * @return
     */
    fun createFragment(contentLayoutId: Int): OptFragment {
        return OptFragment(contentLayoutId)
    }

}