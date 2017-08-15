package net.qiujuer.italker.push.frags.assist;


import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.qiujuer.italker.common.app.Application;
import net.qiujuer.italker.push.R;
import net.qiujuer.italker.push.frags.media.GalleryFragment;

import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * 权限申请弹出框
 */
public class PermissionsFragment extends BottomSheetDialogFragment implements  EasyPermissions.PermissionCallbacks{
    /**
     * 权限回调的标识
     */
    private static final int RC = 0x0100;

    public PermissionsFragment() {
        // Required empty public constructor
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new GalleryFragment.TransStatusBottomSheetDialog(getContext());//状态栏变黑问题
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_permissions, container, false);
        root.findViewById(R.id.btn_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestPerm();
            }
        });
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshState(getView());
    }

    /**
     * 刷新我们的布局中的图片的状态
     * @param root
     */
    private void refreshState(View root){
        if(root == null)
            return;
        Context context= getContext();
        root.findViewById(R.id.im_state_permission_network)
                .setVisibility(haveNetWorkPerm(context)?View.VISIBLE:View.GONE);
        root.findViewById(R.id.im_state_permission_read)
                .setVisibility(haveReadPerm(context)?View.VISIBLE:View.GONE);
        root.findViewById(R.id.im_state_permission_write)
                .setVisibility(haveWritePerm(context)?View.VISIBLE:View.GONE);
        root.findViewById(R.id.im_state_permission_record_audio)
                .setVisibility(haveRecordAudioPerm(context)?View.VISIBLE:View.GONE);
    }

    /**
     * 检查是否有网络权限
     * @param context
     * @return
     */
    private static boolean haveNetWorkPerm(Context context){
        String[] perms = new String[] {
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.ACCESS_WIFI_STATE
        };
        return EasyPermissions.hasPermissions(context,perms);
    }
    /**
     * 检查是否有外部存储读取
     * @param context
     * @return
     */
    private static boolean haveReadPerm(Context context){
        String[] perms = new String[] {
                Manifest.permission.READ_EXTERNAL_STORAGE
        };
        return EasyPermissions.hasPermissions(context,perms);
    }

    /**
     * 检查是否有外部存储写入权限write
     * @param context
     * @return
     */
    private static boolean haveWritePerm(Context context){
        String[] perms = new String[] {
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };
        return EasyPermissions.hasPermissions(context,perms);
    }

    /**
     * 检查是否有录音
     * @param context
     * @return
     */
    private static boolean haveRecordAudioPerm(Context context){
        String[] perms = new String[] {
                Manifest.permission.RECORD_AUDIO
        };
        return EasyPermissions.hasPermissions(context,perms);
    }


    private static void show(FragmentManager manager){
        new PermissionsFragment()
                .show(manager, PermissionsFragment.class.getName());
    }

    public static boolean haveAll(Context context,FragmentManager manager) {
        boolean havaAll = haveNetWorkPerm(context)
                        &&haveReadPerm(context)
                        &&haveWritePerm(context)
                        &&haveRecordAudioPerm(context);
        if(!havaAll) {
            show(manager);
        }
        return havaAll;
    }

    @AfterPermissionGranted(RC)
    private void requestPerm(){
        String[] perms = new String[] {
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO
        };
        if(EasyPermissions.hasPermissions(getContext(),perms)){
            Application.showToast(R.string.label_permission_ok);
            refreshState(getView());
        }else{
            EasyPermissions.requestPermissions(this,getString(R.string.title_assist_permissions),RC,perms);
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }

    /**
     * 权限申请的时候回调的方法，在这个方法中吧对应的权限的申请状态交给EasyPermissions
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
}
