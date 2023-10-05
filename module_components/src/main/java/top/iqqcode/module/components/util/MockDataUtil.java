package top.iqqcode.module.components.util;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import top.iqqcode.module.components.expend.entrance.ChatEntranceTestData;

public class MockDataUtil {

    @NonNull
    public static List<ChatEntranceTestData> getMockData() {
        ChatEntranceTestData data01 = new ChatEntranceTestData();
        data01.setUrl("https://iqqcode-blog.oss-cn-beijing.aliyuncs.com/img-2022/person_virtual_image.png");
        data01.setTitle("公告小喇叭功能01");
        data01.setContent("哈哈哈哈哈哈哈哈啊哈哈哈01");

        ChatEntranceTestData data02 = new ChatEntranceTestData();
        data02.setUrl("https://iqqcode-blog.oss-cn-beijing.aliyuncs.com/img-2022/icon_ala_videotab_live.gif");
        data02.setTitle("公告小喇叭功能02");
        data02.setContent("哈哈哈哈哈哈哈哈啊哈哈哈02");

        ChatEntranceTestData data03 = new ChatEntranceTestData();
        data03.setUrl("https://iqqcode-blog.oss-cn-beijing.aliyuncs.com/img-2022/icon_voiceroom_floatingball.jpg");
        data03.setTitle("公告小喇叭功能03");
        data03.setContent("哈哈哈哈哈哈哈哈啊哈哈哈03");

        ChatEntranceTestData data04 = new ChatEntranceTestData();
        data04.setUrl("https://iqqcode-blog.oss-cn-beijing.aliyuncs.com/img-2022/iqqcode.jpeg");
        data04.setTitle("公告小喇叭功能04");
        data04.setContent("哈哈哈哈哈哈哈哈啊哈哈哈04");

        ChatEntranceTestData data05 = new ChatEntranceTestData();
        data05.setUrl("https://iqqcode-blog.oss-cn-beijing.aliyuncs.com/img-2022/ala_live_129118204.gif");
        data05.setTitle("公告小喇叭功能05");
        data05.setContent("哈哈哈哈哈哈哈哈啊哈哈哈05");

        List<ChatEntranceTestData> list = new ArrayList<>();
        list.add(data01);
        list.add(data02);
        list.add(data03);
        list.add(data04);
        list.add(data05);
        return list;
    }
}
