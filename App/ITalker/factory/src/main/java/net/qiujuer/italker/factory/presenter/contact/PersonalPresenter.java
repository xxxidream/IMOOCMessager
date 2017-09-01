package net.qiujuer.italker.factory.presenter.contact;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;
import net.qiujuer.italker.factory.Factory;
import net.qiujuer.italker.factory.data.helper.UserHelper;
import net.qiujuer.italker.factory.model.db.User;
import net.qiujuer.italker.factory.persistence.Account;
import net.qiujuer.italker.factory.presenter.BasePresenter;

/**
 * Created by 16571 on 2017/9/1.
 */

public class PersonalPresenter extends BasePresenter<PersonalContract.View> implements PersonalContract.Presenter {

    private User user;
    public PersonalPresenter(PersonalContract.View view) {
        super(view);
    }

    @Override
    public User getUserPersonal() {
        return null;
    }

    @Override
    public void start() {
        super.start();
        //个人页面
        Factory.runOnAsync(new Runnable() {
            @Override
            public void run() {
                PersonalContract.View view = getView();
                if (view!=null){
                    String id = view.getUserId();
                    User user = UserHelper.searchFirstOfN(id);
                    onLoaded(view,user);
                }

            }
        });


    }

    private void onLoaded(final PersonalContract.View view,final User user){
        this.user  = user;
        final boolean isSelf = user.getId().equalsIgnoreCase(Account.getUserId());
        final boolean isFollow = isSelf || user.isFollow();
        final boolean allowSayHello = !isSelf && isFollow;
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
               view.onLoadDone(user);
               view.setFollowStatus(isFollow);
               view.allowSayHello(allowSayHello);
            }
        });
    }

    @Override
    public void destory() {
        super.destory();
    }
}
