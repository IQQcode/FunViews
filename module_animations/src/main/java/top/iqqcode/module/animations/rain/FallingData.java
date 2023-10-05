package top.iqqcode.module.animations.rain;

import androidx.annotation.NonNull;

/**
 * 回复评论彩蛋数据
 * @author jiazihui
 */
public class FallingData {

    /**
     * 不可点击图片
     */
    private String pic;

    /**
     * 可点击图片
     */
    private String picClick;

    /**
     * 可点击图片的跳转
     */
    private String jumpLink;

    private String content;

    @NonNull
    public static FallingData getFakeData() {
        FallingData data = new FallingData();
        data.setPic("https://iqqcode-blog.oss-cn-beijing.aliyuncs.com/img-2022/red_envelope.png");
        data.setPicClick("https://iqqcode-blog.oss-cn-beijing.aliyuncs.com/img-2022/red_money.png");
        data.setJumpLink("bdtiebalive://video/yylive/joinlive?sid=1494935205&amp;ssid=1494935205&amp;templateId=33554530");
        data.setContent("测试一下吧");
        data.setJumpLink("www.jd.com");
        return data;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getPicClick() {
        return picClick;
    }

    public void setPicClick(String picClick) {
        this.picClick = picClick;
    }

    public String getJumpLink() {
        return jumpLink;
    }

    public void setJumpLink(String jumpLink) {
        this.jumpLink = jumpLink;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
