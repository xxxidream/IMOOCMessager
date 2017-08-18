package net.qiujuer.italker.push.frags.account;


import android.content.Context;

import net.qiujuer.italker.common.app.Fragment;
import net.qiujuer.italker.push.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {
    private AccountTrigger mAccountTrigger;


    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_login;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mAccountTrigger = (AccountTrigger)context;
    }

    @Override
    public void onResume() {
        super.onResume();
        mAccountTrigger.triggerView();
    }
}
