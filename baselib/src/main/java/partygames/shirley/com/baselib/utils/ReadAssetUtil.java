package partygames.shirley.com.baselib.utils;

import android.content.Context;
import android.content.res.AssetManager;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Administrator on 2016/5/26.
 */
public class ReadAssetUtil {

    private Context context;

    public ReadAssetUtil(Context context){
        this.context = context;
    }

    public String read(String fileName) {
        // 把assert里的文件拷到sd卡上相应目录
        AssetManager mag = context.getAssets();
        InputStream in = null;
        int ilen = 0;
        try {
            in = mag.open(fileName);
            ilen = in.available();
        } catch (IOException e) {
            e.printStackTrace();
        }

        byte[] buffer = new byte[ilen];
        try {
            in.read(buffer);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        String str = new String(buffer);
        return str;
    }
}
