package net.qiujuer.italker.push.frags.search;


import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import net.qiujuer.italker.common.app.PresenterFragment;
import net.qiujuer.italker.common.widget.EmptyView;
import net.qiujuer.italker.common.widget.PortraitView;
import net.qiujuer.italker.common.widget.recycler.RecyclerAdapter;
import net.qiujuer.italker.factory.model.card.UserCard;
import net.qiujuer.italker.factory.presenter.search.SearchContract;
import net.qiujuer.italker.factory.presenter.search.SearchUserPresenter;
import net.qiujuer.italker.push.R;
import net.qiujuer.italker.push.activities.SearchActivity;

import java.util.List;

import butterknife.BindView;

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
                return new SearchUserFragment.viewHolder(root);
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

    class viewHolder extends RecyclerAdapter.ViewHolder<UserCard>{
        @BindView(R.id.im_portrait)
        PortraitView mPortrait;

        @BindView(R.id.txt_name)
        TextView mName;

        @BindView(R.id.im_follow)
        ImageView mFollow;

        @Override
        protected void onBind(UserCard userCard) {
            Glide.with(SearchUserFragment.this)
                    .load(userCard.getPortrait())
                    .centerCrop()
                    .into(mPortrait);
            mName.setText(userCard.getName());
            mFollow.setEnabled(!userCard.isFollow());
        }

        public viewHolder(View itemView) {
            super(itemView);
        }
    }
}
