package top.iqqcode.module.components.floats;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.android.arouter.facade.annotation.Route;

import top.iqqcode.lib.common.router.ComponentsRouter;
import top.iqqcode.module.components.R;

/**
 * @author jiazihui
 * @link <a> https://github.com/AnliaLee/ExpandMenu </a>
 */
@Route(path = ComponentsRouter.FLOAT_EXPEND_ACTIVITY)
public class FloatExpandActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_float_expend);
    }
}