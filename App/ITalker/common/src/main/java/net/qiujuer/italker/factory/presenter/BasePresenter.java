package net.qiujuer.italker.factory.presenter;

/**
 * Created by admin on 2017/8/18.
 */

public class BasePresenter<T extends  BaseContract.View> implements BaseContract.Presenter{
   private T mView;
    public BasePresenter(T view){
        setView(view);
    }

    /**
     * 设置一个view 子类可以重写
     * @param view
     */
    protected void setView(T view){
        this.mView = view;
        this.mView.setPresenter(this);
    }
    /*
        给子类使用的获取View的方法
     */
    protected final T getView()
    {
        return mView;
    }
    @Override
    public void start() {
        T view = mView;
        if(view!=null){
            view.showLoading();
        }
    }

    @Override
    public void destory() {
        T view = mView;
        mView = null ;
        if(view!=null){
            //把Presenter设置为NULL
            view.setPresenter(null);
        }
    }
}
