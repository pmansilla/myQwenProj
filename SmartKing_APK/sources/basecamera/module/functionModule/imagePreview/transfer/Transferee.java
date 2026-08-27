package basecamera.module.functionModule.imagePreview.transfer;

import android.R;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.widget.AbsListView;
import android.widget.ImageView;
import basecamera.module.functionModule.imagePreview.loader.ImageLoader;
import basecamera.module.functionModule.imagePreview.loader.UniversalImageLoader;
import basecamera.module.functionModule.imagePreview.style.index.CircleIndexIndicator;
import basecamera.module.functionModule.imagePreview.style.progress.ProgressBarIndicator;
import basecamera.module.functionModule.imagePreview.transfer.TransferLayout;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class Transferee implements DialogInterface.OnShowListener, DialogInterface.OnKeyListener, TransferLayout.OnLayoutResetListener {
    private Context context;
    private boolean shown;
    private TransferConfig transConfig;
    private Dialog transDialog;
    private TransferLayout transLayout;
    private OnTransfereeStateChangeListener transListener;

    /* loaded from: classes.dex */
    public interface OnTransfereeLongClickListener {
        void onLongClick(ImageView imageView, int i);
    }

    /* loaded from: classes.dex */
    public interface OnTransfereeStateChangeListener {
        void onDismiss();

        void onShow();
    }

    private Transferee(Context context) {
        this.context = context;
        creatLayout();
        createDialog();
    }

    private void checkConfig() {
        if (this.transConfig.isSourceEmpty()) {
            throw new IllegalArgumentException("the parameter sourceImageList can't be empty");
        }
        this.transConfig.setNowThumbnailIndex(this.transConfig.getNowThumbnailIndex() < 0 ? 0 : this.transConfig.getNowThumbnailIndex());
        this.transConfig.setOffscreenPageLimit(this.transConfig.getOffscreenPageLimit() <= 0 ? 1 : this.transConfig.getOffscreenPageLimit());
        this.transConfig.setDuration(this.transConfig.getDuration() <= 0 ? 300L : this.transConfig.getDuration());
        this.transConfig.setProgressIndicator(this.transConfig.getProgressIndicator() == null ? new ProgressBarIndicator() : this.transConfig.getProgressIndicator());
        this.transConfig.setIndexIndicator(this.transConfig.getIndexIndicator() == null ? new CircleIndexIndicator() : this.transConfig.getIndexIndicator());
        this.transConfig.setImageLoader(this.transConfig.getImageLoader() == null ? UniversalImageLoader.with(this.context.getApplicationContext()) : this.transConfig.getImageLoader());
    }

    public static void clear(ImageLoader imageLoader) {
        imageLoader.clearCache();
    }

    private void creatLayout() {
        this.transLayout = new TransferLayout(this.context);
        this.transLayout.setOnLayoutResetListener(this);
    }

    private void createDialog() {
        this.transDialog = new AlertDialog.Builder(this.context, getDialogStyle()).setView(this.transLayout).create();
        this.transDialog.setOnShowListener(this);
        this.transDialog.setOnKeyListener(this);
    }

    private void fillByListView(List<ImageView> list) {
        AbsListView listView = this.transConfig.getListView();
        int childCount = listView.getChildCount();
        for (int i = 0; i < childCount; i++) {
            list.add((ImageView) listView.getChildAt(i).findViewById(this.transConfig.getImageId()));
        }
        fillPlaceHolder(list, listView.getCount(), listView.getFirstVisiblePosition(), listView.getLastVisiblePosition());
    }

    private void fillByRecyclerView(List<ImageView> list) {
        int i;
        RecyclerView recyclerView = this.transConfig.getRecyclerView();
        int childCount = recyclerView.getChildCount();
        int i2 = 0;
        for (int i3 = 0; i3 < childCount; i3++) {
            list.add((ImageView) recyclerView.getChildAt(i3).findViewById(this.transConfig.getImageId()));
        }
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        int itemCount = layoutManager.getItemCount();
        if (layoutManager instanceof GridLayoutManager) {
            GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            i2 = gridLayoutManager.findFirstVisibleItemPosition();
            i = gridLayoutManager.findLastVisibleItemPosition();
        } else if (layoutManager instanceof LinearLayoutManager) {
            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
            i2 = linearLayoutManager.findFirstVisibleItemPosition();
            i = linearLayoutManager.findLastVisibleItemPosition();
        } else {
            i = 0;
        }
        fillPlaceHolder(list, itemCount, i2, i);
    }

    private void fillOriginImages() {
        ArrayList arrayList = new ArrayList();
        if (this.transConfig.getRecyclerView() != null) {
            fillByRecyclerView(arrayList);
        } else if (this.transConfig.getListView() != null) {
            fillByListView(arrayList);
        } else if (this.transConfig.getImageView() != null) {
            arrayList.add(this.transConfig.getImageView());
        }
        this.transConfig.setOriginImageList(arrayList);
    }

    private void fillPlaceHolder(List<ImageView> list, int i, int i2, int i3) {
        if (i2 > 0) {
            while (i2 > 0) {
                list.add(0, null);
                i2--;
            }
        }
        if (i3 < i) {
            for (int i4 = (i - 1) - i3; i4 > 0; i4--) {
                list.add(null);
            }
        }
    }

    public static Transferee getDefault(Context context) {
        return new Transferee(context);
    }

    private int getDialogStyle() {
        return Build.VERSION.SDK_INT > 19 ? R.style.Theme.Translucent.NoTitleBar.Fullscreen : R.style.Theme.Translucent.NoTitleBar;
    }

    public Transferee apply(TransferConfig transferConfig) {
        if (!this.shown) {
            this.transConfig = transferConfig;
            fillOriginImages();
            checkConfig();
            this.transLayout.apply(transferConfig);
        }
        return this;
    }

    public void dismiss() {
        if (this.shown) {
            this.transLayout.dismiss(this.transConfig.getNowThumbnailIndex());
            this.shown = false;
        }
    }

    public boolean isShown() {
        return this.shown;
    }

    @Override // android.content.DialogInterface.OnKeyListener
    public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
        if (i == 4 && keyEvent.getAction() == 1 && !keyEvent.isCanceled()) {
            dismiss();
        }
        return true;
    }

    @Override // basecamera.module.functionModule.imagePreview.transfer.TransferLayout.OnLayoutResetListener
    public void onReset() {
        this.transDialog.dismiss();
        if (this.transListener != null) {
            this.transListener.onDismiss();
        }
        this.shown = false;
    }

    @Override // android.content.DialogInterface.OnShowListener
    public void onShow(DialogInterface dialogInterface) {
        this.transLayout.show();
    }

    public void setOnTransfereeStateChangeListener(OnTransfereeStateChangeListener onTransfereeStateChangeListener) {
        this.transListener = onTransfereeStateChangeListener;
    }

    public void show() {
        if (this.shown) {
            return;
        }
        this.transDialog.show();
        if (this.transListener != null) {
            this.transListener.onShow();
        }
        this.shown = true;
    }

    public void show(OnTransfereeStateChangeListener onTransfereeStateChangeListener) {
        if (this.shown) {
            return;
        }
        this.transDialog.show();
        this.transListener = onTransfereeStateChangeListener;
        this.transListener.onShow();
        this.shown = true;
    }
}
