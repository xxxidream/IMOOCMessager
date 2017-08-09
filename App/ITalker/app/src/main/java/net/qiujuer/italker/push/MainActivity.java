package net.qiujuer.italker.push;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.ViewTarget;

import net.qiujuer.italker.common.app.Activity;
import net.qiujuer.italker.common.widget.a.PortraitView;
import net.qiujuer.italker.push.helper.NavHelper;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends Activity  implements  BottomNavigationView.OnNavigationItemSelectedListener{
    @BindView(R.id.appbar)
    View mLayAppbar;

    @BindView(R.id.im_portrait)
    PortraitView mPortrait;

    @BindView(R.id.txt_title)
    TextView mTitle;

    @BindView(R.id.lay_container)
    FrameLayout mContainer;

    @BindView(R.id.navigation)
    BottomNavigationView mNavigation;

    private NavHelper mNavHelper;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        //初始化底部工具类
        mNavHelper = new NavHelper();
        Glide.with(this)
                .load(R.drawable.bg_src_morning)
                .centerCrop()
                .into(new ViewTarget<View,GlideDrawable>(mLayAppbar) {
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                        this.view.setBackground(resource.getCurrent());
                    }
                });
        mNavigation.setOnNavigationItemSelectedListener(this);
    }

    @Override
    protected void initData() {
        super.initData();
    }
    @OnClick(R.id.im_search)
    void onSearchMenuClick(){

    }
    @OnClick(R.id.btn_action)
    void onActionClick(){

    }

    /***
     * 当我们的底部导航栏被点击的时候触发
     * @param item MenuItem
     * @return true 代表我们能够处理这个点击
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
       //转接事件流到工具类中
        return mNavHelper.performClickMenu(item.getItemId());
    }
}
