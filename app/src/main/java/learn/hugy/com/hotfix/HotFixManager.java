package learn.hugy.com.hotfix;

import android.content.Context;

import java.io.File;

import learn.hugy.com.hotfix.utils.DownloadUtil;
import learn.hugy.com.hotfix.utils.FileUtils;
import learn.hugy.com.hotfix.utils.HotFixUtil;

public class HotFixManager {
    // public static String dexFilePath;
    public static String downloadPatchDexPath;
    private static String patchDexStoragePath;
    private static HotFixManager mManager;
    private static Context mContext;
    public static final String DEX_NAME = "PatchDex.dex";
    public static final String TAG = "HOTFIX";
    public static HotFixManager instance(Context mContext) {
        synchronized (HotFixManager.class) {
            if (mManager == null) {
                mManager = new HotFixManager(mContext);
            }
            return mManager;
        }
    }

    private HotFixManager(Context context) {
        mContext = context;
        patchDexStoragePath = context.getFilesDir().getAbsolutePath() + File.separator + "patch";
    }

    public HotFixManager downloadPatchDexPath(String path) {
        downloadPatchDexPath = path;
        return mManager;
    }

    public HotFixManager init(DownloadUtil.DownCallBack callBack) {
        // 判读是否有 downloadPatchDexPath 目录
        DownloadUtil.download(downloadPatchDexPath, patchDexStoragePath, callBack);
        return mManager;
    }

    public String getDownloadUrl() {
        return downloadPatchDexPath;
    }

    public static File getPatchDexFile() {
        File file = new File(patchDexStoragePath);
        if (file.exists()) {
            return file;
        } else {
            throw new IllegalStateException("NO HOTFIX DEX FILE EXISTS");
        }
    }


    public void copyDexFileToSD(Context mContext) {
        File dst = new File(mContext.getDir("dex", Context.MODE_PRIVATE).getAbsolutePath());
        FileUtils.copyAssetsFile(mContext, "PatchDex.dex", dst);
    }

    public void hotfix(Context mContext) {
        try {
            HotFixUtil.doHotFix(mContext);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
