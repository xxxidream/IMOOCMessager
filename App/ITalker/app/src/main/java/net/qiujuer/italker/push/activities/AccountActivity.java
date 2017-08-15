package net.qiujuer.italker.push.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.yalantis.ucrop.UCrop;

import net.qiujuer.italker.common.app.Activity;
import net.qiujuer.italker.common.app.Fragment;
import net.qiujuer.italker.push.R;
import net.qiujuer.italker.push.frags.account.UpdateInfoFragment;

public class AccountActivity extends Activity {
    private Fragment mCurFragment;

    /**
     * 账户activity显示的入口
     * @param context
     */
    public static void show(Context context){
        context.startActivity(new Intent(context,AccountActivity.class));
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_account;
    }

    @Override
    protected void initWidows() {
        super.initWidows();
        mCurFragment = new UpdateInfoFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.lay_container,mCurFragment)
                .commit();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            final Uri resultUri = UCrop.getOutput(data);
            //渲染
            if(resultUri!=null){
                mCurFragment.onActivityResult(requestCode,resultCode,data);
            }
        } else if (resultCode == UCrop.RESULT_ERROR) {
            final Throwable cropError = UCrop.getError(data);
        }
    }
}
