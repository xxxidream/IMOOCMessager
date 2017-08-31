package net.qiujuer.italker.push.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import net.qiujuer.italker.common.app.Fragment;
import net.qiujuer.italker.common.app.ToolbarActivity;
import net.qiujuer.italker.push.R;
import net.qiujuer.italker.push.frags.search.SearchGroupFragment;
import net.qiujuer.italker.push.frags.search.SearchUserFragment;

public class SearchActivity extends ToolbarActivity {
    private static final String EXTRA_TYPE = "EXTRA_TYPE";
    public static final int TYPE_USER = 1;
    public static final int TYPE_GROUP = 1;

    private int type;
    private SearchFragment mSearchFragment;

    /**
     * 显示搜索页面
     *
     * @param context 上下文
     * @param type    显示的类型，用户还是群
     */
    public static void show(Context context, int type) {
        Intent intent = new Intent(context, SearchActivity.class);
        intent.putExtra(EXTRA_TYPE, type);
        context.startActivity(intent);
    }

    @Override
    protected boolean initArgs(Bundle bundle) {
        type = bundle.getInt(EXTRA_TYPE);
        return type == TYPE_USER || type == TYPE_GROUP;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        //显示对应的fragment
        Fragment fragment;
        if (type== TYPE_USER){
            SearchUserFragment searchUserFragment = new SearchUserFragment();
            fragment = searchUserFragment;
            mSearchFragment = searchUserFragment;
        }else{
            SearchGroupFragment searchGroupFragment = new SearchGroupFragment();
            fragment = searchGroupFragment;
            mSearchFragment = searchGroupFragment;
        }
        getSupportFragmentManager().beginTransaction()
                .add(R.id.lay_container,fragment)
                .commit();
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_search;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //初始化菜单
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        if (searchView != null) {
            SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    search(query);
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    if(TextUtils.isEmpty(s)){
                        search(s);
                        return true;
                    }
                    return false;
                }
            });
        }
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * 搜索
     * @param query
     */
    private void search(String query) {
        if (mSearchFragment==null)
            return;
        mSearchFragment.search(query);
    }

    /**
     * 搜索的fragment 必须继承的接口
     */
    public interface SearchFragment{
        void search(String content);
    }

}
