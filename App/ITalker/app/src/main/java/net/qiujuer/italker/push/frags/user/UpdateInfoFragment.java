package net.qiujuer.italker.push.frags.user;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.StringRes;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.yalantis.ucrop.UCrop;

import net.qiujuer.genius.ui.widget.Button;
import net.qiujuer.genius.ui.widget.EditText;
import net.qiujuer.genius.ui.widget.Loading;
import net.qiujuer.italker.common.app.Application;
import net.qiujuer.italker.common.app.PresenterFragment;
import net.qiujuer.italker.common.widget.PortraitView;
import net.qiujuer.italker.factory.presenter.user.UpdateInfoContract;
import net.qiujuer.italker.factory.presenter.user.UpdateInfoPresenter;
import net.qiujuer.italker.push.App;
import net.qiujuer.italker.push.R;
import net.qiujuer.italker.push.activities.MainActivity;
import net.qiujuer.italker.push.frags.media.GalleryFragment;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

/**
 * 更新用户信息的Fragment
 */
public class UpdateInfoFragment extends PresenterFragment<UpdateInfoContract.Presenter> implements UpdateInfoContract.View {
    @BindView(R.id.im_portrait)
    PortraitView mPortrait;
    @BindView(R.id.im_sex)
    ImageView mSex;
    @BindView(R.id.edit_desc)
    EditText mDesc;
    @BindView(R.id.btn_submit)
    Button mSubmit;
    @BindView(R.id.loading)
    Loading mLoading;
    private String mPortraitPath;
    private boolean isMan = true;


    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_update_info;
    }

    public UpdateInfoFragment() {
        // Required empty public constructor
    }

    @OnClick(R.id.im_portrait)
    void onPortraitClick(){
        new GalleryFragment()
                .setListener(new GalleryFragment.OnSelectedListener() {
                    @Override
                    public void onSelectedImage(String path) {
                        UCrop.Options options = new UCrop.Options();
                        //设置图片处理的格式JPEG
                        options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
                        //设置压缩后的图片精度
                        options.setCompressionQuality(96);
                        //得到头像的缓存地址
                        File dPath = Application.getPortraitTmpFile();
                        UCrop.of(Uri.fromFile(new File(path)),Uri.fromFile(dPath))
                                .withAspectRatio(1,1)
                                .withMaxResultSize(520,520)
                                .withOptions(options)
                                .start(getActivity());

                    }
                })
                //show 的时候建议使用getChildFragmentManager
                //tag class 名字
                .show(getChildFragmentManager(),GalleryFragment.class.getName());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //收到从activity传递过来的值，然后取出其中的值
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            final Uri resultUri = UCrop.getOutput(data);
            //渲染
           if(resultUri!=null){
               loadPortrait(resultUri);
           }
        } else if (resultCode == UCrop.RESULT_ERROR) {
            App.showToast(R.string.data_rsp_error_unknown);
            final Throwable cropError = UCrop.getError(data);
        }
    }
    private void loadPortrait(Uri uri){
        mPortraitPath =uri.getPath();
        Glide.with(this)
                .load(uri)
                .asBitmap()
                .centerCrop()
                .into(mPortrait);
    }
    @OnClick(R.id.btn_submit)
    void onSubmitClick(){
        String desc = mDesc.getText().toString();

        //调用p层进行注册
        mPresenter.update(mPortraitPath,desc,isMan);
    }

    @Override
    public void updateSucceed() {
        MainActivity.show(getContext());
        getActivity().finish();
    }

    @Override
    protected UpdateInfoContract.Presenter initPresenter() {
        return new UpdateInfoPresenter(this);
    }
    @Override
    public void showLoading() {
        super.showLoading();
        //正在进行时，正在进行登录，界面不可操作
        //开始Loading
        mLoading.start();
        mDesc.setEnabled(false);
        mPortrait.setEnabled(false);
        mSex.setEnabled(false);
        mSubmit.setEnabled(false);
    }

    @Override
    public void showError(@StringRes int str) {
        super.showError(str);
        //当提示需要显示错误的时候触发，一定是结束了
        mLoading.stop();
        mDesc.setEnabled(true);
        mPortrait.setEnabled(true);
        mSex.setEnabled(true);
        mSubmit.setEnabled(true);
    }

    @OnClick(R.id.im_sex)
    void onSexClick(){
        isMan = !isMan;
        Drawable drawable = getResources().getDrawable(isMan?
        R.drawable.ic_sex_man:R.drawable.ic_sex_woman);
        mSex.setImageDrawable(drawable);
        mSex.getBackground().setLevel(isMan?0:1);
    }
}
