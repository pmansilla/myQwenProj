package com.czw.smartkit.debugModule;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import com.czw.friendly.mail.SendMailUtil;
import com.czw.smartkit.R;
import com.czw.smartkit.base.TitleActivity;
import com.czw.smartkit.preferenceModule.SharePreferenceDevice;
import com.czw.utils.LogUtil;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import me.panpf.sketch.uri.FileUriModel;
import ycble.runchinaup.log.ycBleLog;

/* loaded from: classes.dex */
public class DebugLogActivity extends TitleActivity {

    @BindView(R.id.log_text_tv)
    TextView log_text_tv;
    File file = null;
    BufferedReader reader = null;
    final int MSG_WHAT_LOAD_FILE_SUCCCESS = 1;
    final int MSG_WHAT_LOAD_FILE_FAILURE = 2;
    private Handler handler = new Handler() { // from class: com.czw.smartkit.debugModule.DebugLogActivity.3
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            super.handleMessage(message);
            switch (message.what) {
                case 1:
                    DebugLogActivity.this.dismissLoadingDialog();
                    DebugLogActivity.this.log_text_tv.setText((String) message.obj);
                    return;
                case 2:
                    DebugLogActivity.this.dismissLoadingDialog();
                    DebugLogActivity.this.log_text_tv.setText("");
                    return;
                default:
                    return;
            }
        }
    };

    private synchronized void loadFile() {
        showLoadingDialog("正在加载");
        new Thread(new Runnable() { // from class: com.czw.smartkit.debugModule.DebugLogActivity.1
            @Override // java.lang.Runnable
            public void run() {
                SharePreferenceDevice.read();
                DebugLogActivity.this.file = new File(ycBleLog.getBleLogFileDir() + FileUriModel.SCHEME + "smartking.txt");
                StringBuilder sb = new StringBuilder();
                sb.append("debug==file:");
                sb.append(DebugLogActivity.this.file.getPath());
                LogUtil.e(sb.toString());
                try {
                    DebugLogActivity.this.reader = new BufferedReader(new FileReader(DebugLogActivity.this.file));
                    StringBuilder sb2 = new StringBuilder();
                    while (true) {
                        String readLine = DebugLogActivity.this.reader.readLine();
                        if (readLine == null) {
                            DebugLogActivity.this.reader.close();
                            DebugLogActivity.this.handler.sendMessage(DebugLogActivity.this.handler.obtainMessage(1, sb2.toString()));
                            return;
                        } else {
                            sb2.append(readLine);
                            sb2.append("\n");
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    DebugLogActivity.this.handler.sendEmptyMessage(2);
                }
            }
        }).start();
    }

    private void showEditTextDialog() {
        final QMUIDialog.EditTextDialogBuilder editTextDialogBuilder = new QMUIDialog.EditTextDialogBuilder(this);
        editTextDialogBuilder.setTitle("为了方便联系请留个联系方式吧").setCanceledOnTouchOutside(false).setCancelable(false).setPlaceholder("电话/邮箱/qq").setInputType(1).addAction(R.string.cancel, new QMUIDialogAction.ActionListener() { // from class: com.czw.smartkit.debugModule.DebugLogActivity.5
            @Override // com.qmuiteam.qmui.widget.dialog.QMUIDialogAction.ActionListener
            public void onClick(QMUIDialog qMUIDialog, int i) {
                qMUIDialog.dismiss();
            }
        }).addAction(R.string.ok, new QMUIDialogAction.ActionListener() { // from class: com.czw.smartkit.debugModule.DebugLogActivity.4
            @Override // com.qmuiteam.qmui.widget.dialog.QMUIDialogAction.ActionListener
            public void onClick(QMUIDialog qMUIDialog, int i) {
                qMUIDialog.dismiss();
                DebugLogActivity.this.sendFileMail(DebugLogActivity.this.getResources().getString(R.string.app_name_sk), editTextDialogBuilder.getEditText().getText().toString());
            }
        }).create().show();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @OnClick({R.id.refresh_log_file_btn, R.id.clear_log_file_btn, R.id.send_log_file_btn})
    public void click(View view) {
        int id = view.getId();
        if (id == R.id.clear_log_file_btn) {
            LogUtil.e("debug===删除文件");
            ycBleLog.clearLogFile();
            loadFile();
        } else if (id == R.id.refresh_log_file_btn) {
            LogUtil.e("debug===加载文件");
            loadFile();
        } else {
            if (id != R.id.send_log_file_btn) {
                return;
            }
            showEditTextDialog();
        }
    }

    @Override // com.czw.smartkit.base.BaseActivity
    public void initView() {
        this.titleBar.setTitle("日志查看");
        setLoadingDialogCancelable(true);
    }

    @Override // com.czw.smartkit.base.BaseActivity
    public int loadLayout() {
        return R.layout.activity_debug_log;
    }

    public void sendFileMail(String str, String str2) {
        showLoadingDialog("正在发送邮件");
        LogUtil.e("debug===发送邮件");
        this.file = new File(ycBleLog.getBleLogFileDir() + FileUriModel.SCHEME + "smartking.txt");
        StringBuilder sb = new StringBuilder();
        sb.append("debug==file:");
        sb.append(this.file.getPath());
        LogUtil.e(sb.toString());
        SendMailUtil.send(this.file, "635669470@qq.com", str, str2, new SendMailUtil.SendMailCallback() { // from class: com.czw.smartkit.debugModule.DebugLogActivity.2
            @Override // com.czw.friendly.mail.SendMailUtil.SendMailCallback
            public void onSend(final boolean z) {
                DebugLogActivity.this.runOnUiThread(new Runnable() { // from class: com.czw.smartkit.debugModule.DebugLogActivity.2.1
                    @Override // java.lang.Runnable
                    public void run() {
                        DebugLogActivity.this.dismissLoadingDialog();
                        if (z) {
                            DebugLogActivity.this.showSuccessDialog("邮件发送成功");
                        } else {
                            DebugLogActivity.this.showSuccessDialog("邮件发送失败");
                        }
                    }
                });
            }
        });
    }
}
