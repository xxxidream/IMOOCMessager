package net.qiujuer.italker.push.frags.main;


import net.qiujuer.italker.common.app.Fragment;
import net.qiujuer.italker.common.widget.GalleyView;
import net.qiujuer.italker.push.R;

import butterknife.BindView;

public class ActiveFragment extends Fragment {

    @BindView(R.id.galleyView)
    GalleyView mGalley;

    public ActiveFragment() {
        // Required empty public constructor
    }
    
    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_active;
    }

    @Override
    protected void initData() {
        super.initData();
        mGalley.setup(getLoaderManager(), new GalleyView.SelectedChangedListener() {
            @Override
            public void onSelectedCountChanged(int count) {

            }
        });
    }
}
