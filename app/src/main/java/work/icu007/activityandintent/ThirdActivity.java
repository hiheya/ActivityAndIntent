package work.icu007.activityandintent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ShareCompat;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class ThirdActivity extends AppCompatActivity {

    private static final String LOG_TAG = ThirdActivity.class.getSimpleName();
    private EditText mWebsiteEditText;
    private EditText mLocationEditText;
    private EditText mShareTextEditText;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);
        mWebsiteEditText = findViewById(R.id.text_web);
        mLocationEditText = findViewById(R.id.text_location);
        mShareTextEditText = findViewById(R.id.text_message);
    }

    public void openWebsite(View view) {
        // 获取 URL 数据
        String url = mWebsiteEditText.getText().toString();
        // 解析 URI 并且创建Intent实例
        Uri webpage = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
//        Intent intent = new Intent();
//        intent.setAction(Intent.ACTION_VIEW);
//        intent.setData(webpage);
        // 判断是否有activity 能处理这个intent，若存在这样的activity则通过intent来启动activity。
        if (intent.resolveActivity(getPackageManager()) != null) {
            Log.d(LOG_TAG, "openWebsite: " + intent.resolveActivity(getPackageManager()));
            startActivity(intent);
        }else {
            // 若url 无法解析则打印日志
            Log.d(LOG_TAG, "openWebsite: 网页无法解析"+ webpage);
        }
    }

    public void openLocation(View view) {
        // 获取 地址 数据
        String loc = mLocationEditText.getText().toString();
        // 解析 地址为 Uri对象 并且创建Intent实例
        Uri addressUri = Uri.parse("geo:0,0?q=" + loc);

        Intent intent = new Intent(Intent.ACTION_VIEW, addressUri);
//        startActivity(intent);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }else {
            // 若url 无法解析则打印日志
            Log.d(LOG_TAG, "openLocation: 地址无法解析"+ addressUri);
        }
    }

    public void shareMessage(View view) {
        String txt = mShareTextEditText.getText().toString();
        String mimeType = "text/plain";
        ShareCompat.IntentBuilder
                // 启动此共享 （）。ActivityIntentthis
                .from(this)
                // 要共享的项目的 MIME 类型。
                .setType(mimeType)
                // 显示在系统应用选取器上的标题。
                .setChooserTitle("Share this text with:")
                // 要共享的实际文本
                .setText(txt)
                //显示系统应用程序选择器并发送 .Intent
                .startChooser();
    }
}