package net.qiujuer.italker.push;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.drawable.ColorDrawable;
import android.util.Property;
import android.view.View;

import net.qiujuer.genius.res.Resource;
import net.qiujuer.genius.ui.compat.UiCompat;
import net.qiujuer.italker.common.app.Activity;
import net.qiujuer.italker.push.activities.MainActivity;
import net.qiujuer.italker.push.frags.assist.PermissionsFragment;

public class LaunchActivity extends Activity {
    //Drawable
    private ColorDrawable mBgDrawable;


    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_launch;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        //拿到根布局
        View root = findViewById(R.id.activity_launch);
        //获取颜色
        int color = UiCompat.getColor(getResources(),R.color.colorPrimary);
        ColorDrawable drawable = new ColorDrawable(color);
        root.setBackground(drawable);
        mBgDrawable = drawable;
    }

    @Override
    protected void initData() {
        super.initData();
        startAnim(0.8f, new Runnable() {
            @Override
            public void run() {
               skip();
            }
        });
    }
    private void skip(){
        if(PermissionsFragment.haveAll(this,getSupportFragmentManager())){
            MainActivity.show(this);
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    /**
     *属性动画
     * @param endProgress
     * @param endCallback
     */
    private void startAnim(float endProgress,final Runnable endCallback){
        int finalColor = Resource.Color.WHITE;
        //运算当前进度的颜色
        ArgbEvaluator evaluator = new ArgbEvaluator();
        int endColor =(int) evaluator
                .evaluate(endProgress,mBgDrawable.getColor(),finalColor);
        ValueAnimator valueAnimator = ObjectAnimator.ofObject(this,property,evaluator,endColor);
        valueAnimator.setDuration(1500);
        valueAnimator.setIntValues(mBgDrawable.getColor(),endColor);
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                endCallback.run();
            }
        });
        valueAnimator.start();
    }
    private Property<LaunchActivity,Object> property = new Property<LaunchActivity, Object>(Object.class,"color") {
        @Override
        public Object get(LaunchActivity object) {
            return mBgDrawable.getColor();
        }

        @Override
        public void set(LaunchActivity object, Object value) {
           object.mBgDrawable.setColor((int)value);
        }
    };
}
