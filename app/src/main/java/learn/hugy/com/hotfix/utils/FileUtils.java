package learn.hugy.com.hotfix.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

public class FileUtils {

    public static final String TAG = "HOTFIX";
    /**
     * 将文件复制到指定目录
     * @param res 源文件
     * @param dst 目标文件
     */
    public void  copyFile(File res, File dst) {
        if (res.exists()) {
            copyFile(res, dst);
        }
    }

    /**
     * 复制 Assets 中的资源文件到指定文件
     * @param context 上下文
     * @param res assets 资源文件名称
     * @param dst 目标文件
     */
    public static void copyAssetsFile(Context context, String res, File dst) {
        try {
            File fileDir = new File(context.getFilesDir().getAbsolutePath() + File.separator + "patch");
            if (!fileDir.exists()) fileDir.mkdir();
            File file = new File(context.getFilesDir().getAbsolutePath() + File.separator + "patch" + File.separator + res);
            InputStream inputStream = context.getAssets().open(res);

            Log.i("copyAssets2Phone", "file:" + file);
            if (dst.exists() || dst.length() == 0) {
                FileOutputStream fos = new FileOutputStream(file);
                int len = -1;
                byte[] buffer = new byte[1024];
                len = inputStream.read(buffer);
                while ((len) != -1) {
                    fos.write(buffer, 0, len);
                    len = inputStream.read(buffer);
                }
                fos.flush();
                inputStream.close();
                fos.close();
            } else {
                throw new Exception("File Not Found Exception");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void unZip(Context mContext, String filePath) {
        try {
            // 定义要解压的文件
            if (!new File(filePath).exists()) {
                Log.i(TAG, "JAR FILE NOT EXISTS");
                return;
            }
            // 使用FileInputStream对象指定要解压的文件
            FileInputStream fis = new FileInputStream(filePath);
            // 第一步：创建JarInputStream对象来读取压缩文件file.jar
            JarInputStream jis = new JarInputStream(fis);
            // 第二步：调用getNextJarEntry方法打开压缩包中的第一个文件（如果有多个压缩包，可多次调用该方法）
            JarEntry jarEntry = jis.getNextJarEntry();
            // 输出已解压的文件
            FileOutputStream fos = new FileOutputStream(
                    mContext.getFilesDir().getAbsolutePath() + File.separator + "patch" + File.separator +  jarEntry.getName());
            byte[] buffer = new byte[8192];
            int count = 0;
            while ((count = jis.read()) >= 0) {
                fos.write(buffer, 0, count);
            }
            jis.closeEntry();
            jis.close();
            fos.close();
            Log.i(TAG, "解压文件成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
