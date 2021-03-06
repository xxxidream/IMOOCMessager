package net.qiujuer.italker.push.frags.search;


import android.support.annotation.StringRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import net.qiujuer.genius.ui.Ui;
import net.qiujuer.genius.ui.compat.UiCompat;
import net.qiujuer.genius.ui.drawable.LoadingCircleDrawable;
import net.qiujuer.genius.ui.drawable.LoadingDrawable;
import net.qiujuer.italker.common.app.PresenterFragment;
import net.qiujuer.italker.common.widget.EmptyView;
import net.qiujuer.italker.common.widget.PortraitView;
import net.qiujuer.italker.common.widget.recycler.RecyclerAdapter;
import net.qiujuer.italker.factory.model.card.UserCard;
import net.qiujuer.italker.factory.presenter.contact.FollowContract;
import net.qiujuer.italker.factory.presenter.contact.FollowPresenter;
import net.qiujuer.italker.factory.presenter.search.SearchContract;
import net.qiujuer.italker.factory.presenter.search.SearchUserPresenter;
import net.qiujuer.italker.push.R;
import net.qiujuer.italker.push.activities.PersonalActivity;
import net.qiujuer.italker.push.activities.SearchActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * 搜索人的实现
 */
public class SearchUserFragment extends PresenterFragment<SearchContract.Presenter>
        implements SearchActivity.SearchFragment,SearchContract.UserView{

    @BindView(R.id.empty)
    EmptyView mEmptyView;

    @BindView(R.id.recycler)
    RecyclerView mRecycler;

    private RecyclerAdapter<UserCard> mAdapter;

    public SearchUserFragment() {
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);
        mRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecycler.setAdapter(mAdapter = new RecyclerAdapter<UserCard>(){
            @Override
            protected int getItemViewType(int position, UserCard userCard) {
//                返回cell的布局id
                return R.layout.cell_search_list;
            }

            @Override
            protected ViewHolder<UserCard> onCreateViewHolder(View root, int viewType) {
                return new SearchUserFragment.ViewHolder(root);
            }
        });
        mEmptyView.bind(mRecycler);
        setPlaceHolderView(mEmptyView);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_search_user;
    }
    @Override
    protected SearchContract.Presenter initPresenter() {
        return new SearchUserPresenter(this);
    }

    @Override
    protected void initData() {
        super.initData();
        search("");
    }

    @Override
    public void search(String content) {
        //Activity->Fragment->Presenter->Net
        mPresenter.search(content);
    }

    @Override
    public void onSearchDone(List<UserCard> userCards) {
        //数据加载成功
        mAdapter.replace(userCards);
        mPlaceHolderView.triggerOkOrEmpty(mAdapter.getItemCount()>0);
    }

    class ViewHolder extends RecyclerAdapter.ViewHolder<UserCard>
    implements FollowContract.View{
        @BindView(R.id.im_portrait)
        PortraitView mPortrait;

        @BindView(R.id.txt_name)
        TextView mName;

        @BindView(R.id.im_follow)
        ImageView mFollow;

        private FollowContract.Presenter mPresenter;
        public ViewHolder(View itemView) {
            super(itemView);
            new FollowPresenter(this);
        }
        @Override
        protected void onBind(UserCard userCard) {
//            Glide.with(SearchUserFragment.this)
//                    .load(userCard.getPortrait())
//                    .centerCrop()
//                    .into(mPortrait);
            mPortrait.setup(Glide.with(SearchUserFragment.this),userCard);
            mName.setText(userCard.getName());
            mFollow.setEnabled(!userCard.isFollow());
        }



        @OnClick(R.id.im_follow)
        void onFollowClick(){
            //发起关注
            mPresenter.follow(mData.getId());
        }
        @OnClick(R.id.im_portrait)
        void onPortraitClick(){
            PersonalActivity.show(getContext(),mData.getId());
        }
        @Override
        public void onFollowSucceed(UserCard userCard) {
            if (mFollow.getDrawable() instanceof LoadingDrawable){
                ((LoadingDrawable) mFollow.getDrawable()).stop();
                mFollow.setImageResource(R.drawable.sel_opt_done_add);
            }
            updateData(userCard);
        }

        @Override
        public void showError(@StringRes int str) {
            if (mFollow.getDrawable() instanceof LoadingDrawable){
                LoadingDrawable drawable =   (LoadingDrawable) mFollow.getDrawable();
                drawable.setProgress(1);
                drawable.stop();
            }
        }

        @Override
        public void showLoading() {
            int minSize = (int)Ui.dipToPx(getResources(),22);
            int maxSize = (int)Ui.dipToPx(getResources(),30);
            LoadingDrawable drawable = new LoadingCircleDrawable(minSize,maxSize);

            int [] color = new int[]{UiCompat.getColor(getResources(),R.color.white_alpha_208)};
            drawable.setBackgroundColor(0);
            drawable.setForegroundColor(color);
            mFollow.setImageDrawable(drawable);
            drawable.start();
        }

        @Override
        public void setPresenter(FollowContract.Presenter presenter) {
            mPresenter = presenter;
        }
    }
}
