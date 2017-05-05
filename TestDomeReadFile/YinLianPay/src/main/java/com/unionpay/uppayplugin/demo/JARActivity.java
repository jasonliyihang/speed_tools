package com.unionpay.uppayplugin.demo;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;
import com.unionpay.UPPayAssistEx;

public class JARActivity extends BaseActivity {

    @Override
    public void doStartUnionPayPlugin(Activity activity, String tn, String mode) {
        UPPayAssistEx.startPay(activity, null, null, tn, mode);
    }


    @Override
    public void updateTextView(TextView tv) {
        String txt = "接入指南：\n1:拷贝sdkPro目录下的UPPayAssistEx.jar到libs目录下\n"
                + "2:根据需要拷贝sdkPro/jar/data.bin至工程的assets目录下\n"
                + "3:根据需要拷贝sdkPro/jar/XXX/XXX.so到libs目录下\n"
                + "4:根据需要拷贝sdkPro/jar/UPPayPluginExPro.jar到工程的libs目录下\n"
                + "5:获取tn后通过UPPayAssistEx.startPay(...)方法调用控件";
        tv.setText(txt);
        tv.setVisibility(View.GONE);
    }
}
