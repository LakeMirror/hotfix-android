package learn.hugy.com.hotfix.utils;

import android.content.Context;
import android.net.UrlQuerySanitizer;
import android.nfc.Tag;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import learn.hugy.com.hotfix.HotFixManager;

public class DownloadUtil {

    private static ExecutorService executors = Executors.newFixedThreadPool(1);

    public static void download(final String path, final String storagePath, final DownCallBack callBack) {
        executors.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(path);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setDoInput(true);
                    httpURLConnection.setRequestMethod("GET");
                    File file = new File(storagePath);
                    if (file.exists()) {
                        File[] files = file.listFiles();
                        for (File file1: files) {
                            file1.delete();
                        }
                    } else {
                        file.mkdir();
                    }
                    httpURLConnection.connect();
                    if (200 == httpURLConnection.getResponseCode()) {
                        Log.i(HotFixManager.TAG, "START DOWNLOAD");
                        InputStream is = httpURLConnection.getInputStream();
                        File downFile = new File(storagePath + File.separator + HotFixManager.DEX_NAME);
                        OutputStream os = new FileOutputStream(downFile);
                        byte[] bts = new byte[1024];
                        int len = -1;
                        while((len = is.read(bts)) != -1) {
                            os.write(bts);
                        }
                        is.close();
                        os.flush();
                        os.close();
                        // FileUtils.unZip(mContext, downFile.getAbsolutePath());
                        callBack.callback();
                    } else {
                        throw new Exception("URL MAY BE WRONG");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public interface DownCallBack{
        void callback();
    }


}
