package net.qiujuer.italker.push.frags.media;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import net.qiujuer.genius.ui.Ui;
import net.qiujuer.italker.common.tools.UiTool;
import net.qiujuer.italker.common.widget.GalleryView;
import net.qiujuer.italker.push.R;

/**
 * 图片选择fragment
 */
public class GalleryFragment extends BottomSheetDialogFragment implements GalleryView.SelectedChangedListener{
    private GalleryView mGallery;
    private OnSelectedListener mListener;


    public GalleryFragment() {
        // Required empty public constructor
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //我们先使用默认的
        return new TransStatusBottomSheetDialog(getContext());//状态栏变黑问题
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        mGallery = (GalleryView) root.findViewById(R.id.galleryView);
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        mGallery.setup(getLoaderManager(),this);
    }

    @Override
    public void onSelectedCountChanged(int count) {
        if(count>0){
            //如果选中一张图片隐藏自己
            dismiss();
            if(mListener!=null) {
                String[] paths = mGallery.getSelectedImagePath();
                //返回第一张
                mListener.onSelectedImage(paths[0]);
                //取消和唤起者之间的引用，加快内存回收
                mListener=null;
            }
        }
    }

    /**
     * 设置事件监听并返回自己
     * @param listener
     * @return
     */
    public GalleryFragment setListener(OnSelectedListener listener) {
        mListener = listener;
        return this;
    }

    public interface OnSelectedListener{
        void onSelectedImage(String path);
    }
    public static class TransStatusBottomSheetDialog extends BottomSheetDialog{
        public TransStatusBottomSheetDialog(@NonNull Context context) {
            super(context);
        }

        public TransStatusBottomSheetDialog(@NonNull Context context, @StyleRes int theme) {
            super(context, theme);
        }

        public TransStatusBottomSheetDialog(@NonNull Context context, boolean cancelable, OnCancelListener cancelListener) {
            super(context, cancelable, cancelListener);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            final Window window = getWindow();
            if(window == null)
                return;
            //得到屏幕高度
            int screenHeight = UiTool.getScreenHeight(getOwnerActivity());
            //得到状态栏高度
            int statusHeight = (int)Ui.dipToPx(getContext().getResources(),UiTool.getStatusBarHeight(getOwnerActivity()));
//            int statusHeight = (int)Ui.dipToPx(getContext().getResources(),25);

            int dialogHeight = screenHeight - statusHeight;
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                    dialogHeight<=0?ViewGroup.LayoutParams.MATCH_PARENT:dialogHeight);
        }
    }
}
