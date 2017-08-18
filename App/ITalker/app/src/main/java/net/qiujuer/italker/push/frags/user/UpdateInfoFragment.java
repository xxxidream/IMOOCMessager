package net.qiujuer.italker.push.frags.user;


import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.yalantis.ucrop.UCrop;

import net.qiujuer.italker.common.app.Application;
import net.qiujuer.italker.common.app.Fragment;
import net.qiujuer.italker.common.widget.a.PortraitView;
import net.qiujuer.italker.factory.Factory;
import net.qiujuer.italker.factory.net.UploadHelper;
import net.qiujuer.italker.push.R;
import net.qiujuer.italker.push.frags.media.GalleryFragment;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

/**
 * 更新用户信息的Fragment
 */
public class UpdateInfoFragment extends Fragment {
    @BindView(R.id.im_portrait)
    PortraitView mPortrait;
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
            final Throwable cropError = UCrop.getError(data);
        }
    }
    private void loadPortrait(Uri uri){
        Glide.with(this)
                .load(uri)
                .asBitmap()
                .centerCrop()
                .into(mPortrait);

        final String localPath = uri.getPath();
        Log.e("TAG","localPath: " +localPath);
        Factory.runOnAsync(new Runnable() {
            @Override
            public void run() {
                String url = UploadHelper.uploadPortrait(localPath);
                Log.e("TAG","url: " +url);
            }
        });
    }

}
