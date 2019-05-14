package learn.hugy.com.hotfix;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.lang.ref.WeakReference;

import learn.hugy.com.hotfix.test.Test2Utils;
import learn.hugy.com.hotfix.test.TestUtils;
import learn.hugy.com.hotfix.utils.DownloadUtil;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Handler mHandler = new MHandler(this);
    private HotFixManager manager;

    private class MHandler extends Handler {
        private WeakReference<Context> mContext;
        public MHandler(Context mContext) {
            this.mContext = new WeakReference<>(mContext);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            manager.hotfix(MainActivity.this);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        manager = HotFixManager.instance(MainActivity.this)
        .downloadPatchDexPath("http://10.0.2.2:8080/hotfix/PatchDex.dex")
        .init(new DownloadUtil.DownCallBack() {
            @Override
            public void callback() {
                mHandler.obtainMessage().sendToTarget();
            }
        });


    }

    private void initView() {
        Button btOutput = findViewById(R.id.bt_output);
        Button btRepair = findViewById(R.id.bt_repair);
        btOutput.setOnClickListener(this);
        btRepair.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.bt_output:
//                TestUtils.Toast();
//                Test2Utils.say();
//                break;
//            case R.id.bt_repair:
//                HotFixManager manager = HotFixManager.instance(this);
//                manager.copyDexFileToSD(this);
//                manager.hotfix(this);
//                break;
        }
    }
}
