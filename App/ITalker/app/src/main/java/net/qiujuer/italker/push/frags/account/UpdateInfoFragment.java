package net.qiujuer.italker.push.frags.account;


import net.qiujuer.italker.common.app.Fragment;
import net.qiujuer.italker.common.widget.a.PortraitView;
import net.qiujuer.italker.push.R;
import net.qiujuer.italker.push.frags.media.GalleryFragment;

import butterknife.BindView;
import butterknife.OnClick;

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

                    }
                })
                //show 的时候建议使用getChildFragmentManager
                //tag class 名字
                .show(getChildFragmentManager(),GalleryFragment.class.getName());
    }


}
