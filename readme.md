## 一、指示第二个活动的父活动

- 在`AndroidManifest.xml` 中 配置指示主活动是第二个活动的父活动

```xml
<activity
    android:name=".SecondActivity"
    android:label = "Second Activity"
    android:parentActivityName=".MainActivity">
    <meta-data
        android:name="android.support.PARENT_ACTIVITY"
        android:value=".MainActivity" />
    android:exported="true"
</activity>
```

- 使用该属性，我们可以指示主活动是第二个活动的父活动。此关系用于应用中的向上导航：第二个活动的应用栏将具有一个向左箭头，以便用户可以“向上”导航到主活动。

---

## 二、向下一活动传递数据

### 2.1 MainActivity代码实现

- 从文本编辑栏中获取数据；

```java
String message = mMessageEditText.getText().toString();
```

- 把获取到的数据添加到Intent中,可以通过“EXTRA_MESSAGE” `public static final String EXTRA_MESSAGE = "work.icu007.activityandintent.extra.MESSAGE";` 获取数据；

```java
 intent.putExtra(EXTRA_MESSAGE, message);
```

- 带着数据启动服务；

```java
Intent intent = new Intent(this,SecondActivity.class);
String message = mMessageEditText.getText().toString();
intent.putExtra(EXTRA_MESSAGE, message);
startActivity(intent);
```

### 2.2 SecondActivity代码实现

- 获取激活此活动的Intent

```java
Intent intent = getIntent();
```

- 获取传过来的数据

```java
String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
```

- 将传过来的数据展示出来

```java
TextView textView = findViewById(R.id.text_message);
textView.setText(message);
```

---

## 三、向上一活动返回数据

### 3.1在SecondActivity代码实现

- 在类的顶部，添加一个公共常量来定义额外项的键：

```java
public static final String EXTRA_REPLY = "work.icu007.activityandintent.extra.reply";
private EditText mReply;
```

- 从文本编辑栏中获取数据

```java
mReply = findViewById(R.id.editText_second);
String reply = mReply.getText().toString();
```

- 将获取到的数据传入Intent中

```java
Intent replyIntent = new Intent();
replyIntent.putExtra(EXTRA_REPLY, reply);
```

- 将结果设置为 `RESULT_OK` 以指示响应成功. 活动类定义结果代码，包括`RESULT_OK` 和 `RESULT_CANCELLED` 。最后调用finish()以关闭 并返回到 MainActivity当中。

```java
setResult(RESULT_OK,replyIntent);
finish();
```

### 3.2 MainActivity代码实现

- 在类的顶部添加一个公共常量，以定义特定响应类型的键：

```java
 public static final int TEXT_REQUEST = 1;
```

- 添加两个私有变量来保存回复标头和回复元素：`TextView`

```java
private TextView mReplyHeadTextView;
private TextView mReplyTextView;
```

- 重写回调方法：`onActivityResult()`

```java
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TEXT_REQUEST) {
            if (resultCode == RESULT_OK) {
                String reply =
                        data.getStringExtra(SecondActivity.EXTRA_REPLY);
                mReplyHeadTextView.setVisibility(View.VISIBLE);
                mReplyTextView.setText(reply);
                mReplyTextView.setVisibility(View.VISIBLE);
            }
        }
    }
```

- 具体代码实现

```java
public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    public static final String EXTRA_MESSAGE = "work.icu007.activityandintent.extra.MESSAGE";
    private EditText mMessageEditText;
    public static final int TEXT_REQUEST = 1;
    private TextView mReplyHeadTextView;
    private TextView mReplyTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mMessageEditText = findViewById(R.id.editText_main);
        mReplyHeadTextView = findViewById(R.id.text_header_reply);
        mReplyTextView = findViewById(R.id.text_message_reply);
    }

    public void launchSecondActivity(View view) {
        Log.d(LOG_TAG, "Button clicked!");
        Intent intent = new Intent(this,SecondActivity.class);
        String message = mMessageEditText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivityForResult(intent,TEXT_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TEXT_REQUEST) {
            if (resultCode == RESULT_OK) {
                String reply =
                        data.getStringExtra(SecondActivity.EXTRA_REPLY);
                mReplyHeadTextView.setVisibility(View.VISIBLE);
                mReplyTextView.setText(reply);
                mReplyTextView.setVisibility(View.VISIBLE);
            }
        }
    }
}
```

---

## 四、 Activity活动周期

- 活动生命周期中的每个阶段都有一个相应的回调方法：onCreate() onStart() onPause()、onCreate()等。当活动更改状态时，将调用关联的回调方法。
- 活动状态也可以根据设备配置更改而更改，例如，当用户将设备从纵向旋转到横向时。发生这些配置更改时，活动将被销毁并以其默认状态重新创建，并且用户可能会丢失他们在活动中输入的信息。

### 4.1 在主活动中实现回调

1. 在`onCreate()`方法中添加以下日志语句

```java
Log.d(LOG_TAG, "-------");
Log.d(LOG_TAG, "onCreate");
```

2. 同理在其它生命周期回调方法也打印日志

```java
@Override
    protected void onStart() {
        super.onStart();
        Log.d(LOG_TAG, "onStart");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(LOG_TAG, "onPause");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(LOG_TAG, "onRestart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(LOG_TAG, "onResume");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(LOG_TAG, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG, "onDestroy");
    }
```

### 4.2 保存和还原活动实例状态

1. 使用 onSaveInstanceState（） 保存活动实例状态

- 重写 onSaveInstanceState（） 方法

```java
@Override
protected void onSaveInstanceState(@NonNull Bundle outState) {
    super.onSaveInstanceState(outState);
    // 若 replyHeadTextView 是可见状态，则保留其可见状态以及mReplyTextView内的文本
    if (mReplyHeadTextView.getVisibility() == View.VISIBLE){
        // 保存 reply 的可见状态 存在Bundle实例 outState当中，key为reply_visible
        outState.putBoolean("reply_visible",true);
        // 保存mReplyTextView内的文本，存在Bundle实例 outState当中，key为reply_text
        outState.putString("reply_text",mReplyTextView.getText().toString());
    }
}
```

2. 还原活动实例状态

- 在 `onCreate()` 方法中先判断 `savedInstanceState` 是否为空；
- 若`savedInstanceState`不为空再判断 reply 的可见状态 ；
- 若 `reply` 可见，则开始还原保存的实例状态。

```java
// 恢复保存的活动实例状态
if (savedInstanceState != null){
    boolean isVisible = savedInstanceState.getBoolean("reply_visible");
    if (isVisible){
        mReplyHeadTextView.setVisibility(View.VISIBLE);
        mReplyTextView.setText(savedInstanceState.getString("reply_text"));
        mReplyTextView.setVisibility(View.VISIBLE);
    }
}
```

### 4.3 总结

1. 活动生命周期是迁移的一组状态，从首次创建时开始，到 Android 系统回收该资源时结束。
2. 当用户从一个状态导航到另一个应用时，以及应用内部和外部，每个用户都会在生命周期中的状态之间移动。
3. 生命周期中的每个状态都有一个相应的回调方法，可以在类中重写。
4. 生命周期方法是onCreate()、rt()onPause()、onRestart()、onResume()、onStop()还有onDestroy()
5. 重写生命周期回调方法允许我们添加转换到该状态时发生的行为。
6. 设备配置更改（如旋转）会导致销毁并重新创建，就好像它是新的一样。
7. 在onSaveInstanceState()方法中保存Activity实例状态。
8. 实例状态数据以简单的键/值对的形式存储在Bundle .使用这些方法将数据放入Bundle和从中取回数据。
9. 在onCreate() 中恢复实例状态，这是首选方法，或onRestoreInstanceState() 。

---

## 五、隐式Intent

### 5.1 介绍

- 使用隐式意图，我们可以在不知道哪个应用或活动将处理该任务的情况下启动活动。
- 例如，如果我们希望应用拍摄照片、发送电子邮件或在地图上显示位置，则通常不关心哪个应用或活动执行任务。

### 5.2 代码示例

1. 创建一个新应用程序，其中包含一个和三个操作选项：打开网站、在地图上打开位置以及共享文本片段。
2. 使用 隐式 intent 启动 网页时 需要在声明清单中声明：

```xml
<queries>
    <intent>
        <action android:name="android.intent.action.VIEW" />
        <category android:name="android.intent.category.BROWSABLE" />
        <data android:scheme="https" />
    </intent>
</queries>
```

声明后方可使用 `intent.resolveActivity(getPackageManager()) != null`来判断是否有activity能够处理这个intent
3. 实现“打开网站”按钮

- 在activity_third.xml当中创建一个edittext以及button控件

```xml
    <EditText
        android:id="@+id/text_web"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginStart="8dp"
        android:layout_marginTop="16dp"
        android:hint="input the website"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent" />

    <Button
        android:id="@+id/btn_website"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginStart="8dp"
        android:layout_marginTop="16dp"
        android:text="Open website"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@+id/text_web"
        android:onClick="openWebsite"/>
```

- 在 `android:onClick="openWebsite"`处按住alt+enter在ThirdActivity.java中生成openWebsite方法。

```java
public void openWebsite(View view) {
        // 获取 URL 数据
        String url = mWebsiteEditText.getText().toString();
        // 解析 URI 并且创建Intent实例
        Uri webpage = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        //Intent intent = new Intent();
        //intent.setAction(Intent.ACTION_VIEW);
        //intent.setData(webpage);
        // 判断是否有activity 能处理这个intent，若存在这样的activity则通过intent来启动activity。
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }else {
            // 若url 无法解析则打印日志
            Log.d(LOG_TAG, "openWebsite: 网页无法解析"+ webpage);
        }
    }
```

- 在ThirdActivity.java中声明`private EditText mWebsiteEditText;`控件。

```java
private EditText mWebsiteEditText;
mWebsiteEditText = findViewById(R.id.text_web);
```

4. 实现“打开位置”按钮

- 在activity_third.xml当中创建一个edittext以及button控件

```xml
    <EditText
        android:id="@+id/text_location"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginStart="8dp"
        android:layout_marginTop="16dp"
        android:hint="input the Location"
        android:textStyle="bold"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@id/btn_website" />

    <Button
        android:id="@+id/btn_location"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginStart="8dp"
        android:layout_marginTop="16dp"
        android:text="open Location"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@+id/text_location"
        android:onClick="openLocation"/>
```

- 在 `android:onClick="openLocation"`处按住alt+enter在ThirdActivity.java中生成openLocation方法。

```java
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
```

- 在ThirdActivity.java中声明`private EditText mLocationEditText`控件。

```java
private EditText mLocationEditText;
mLocationEditText = findViewById(R.id.text_location);
```

5. 实现“共享此文本”按钮

- 在activity_third.xml当中创建一个edittext以及button控件

```xml
    <EditText
        android:id="@+id/text_message"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginStart="8dp"
        android:layout_marginTop="16dp"
        android:hint="input the massage"
        android:textStyle="bold"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@id/btn_location" />

    <Button
        android:id="@+id/btn_message"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginStart="8dp"
        android:layout_marginTop="16dp"
        android:text="share message"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@id/text_message"
        android:onClick="shareMessage"/>
```

- 在`android:onClick="shareMessage"`处按住alt+enterThirdActivity.java中生成shareMessage方法。

```java
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
```



| **方法**            | **描述**                                  |
| --------------------- | ------------------------------------------- |
| `from()`            | 启动此共享 （）。`Activity``Intent``this` |
| `setType()`         | 要共享的项目的 MIME 类型。                |
| `setChooserTitle()` | 显示在系统应用选取器上的标题。            |
| `setText()`         | 要共享的实际文本                          |
| `startChooser()`    | 显示系统应用程序选择器并发送 .`Intent`    |

- 这种格式将所有构建器的 setter 方法串在一个语句中，是创建和启动 `Intent`.可以将任何其他方法添加到此列表中
6. 接收隐式意向
- 我们还可以 响应从其他应用发送的隐式请求。我们需要在`AndroidManifest.xml`中定义一个筛选器，以指示您有兴趣处理的隐式类型。
- 为了将我们的请求与设备上安装的特定应用进行匹配，Android 系统会将我们的隐式应用与 其过滤器表示他们可以执行该操作进行匹配。如果安装了多个匹配的应用程序，则会向用户显示一个应用程序选择器，让他们选择要用于处理该的应用程序。
  - 如果只有一个Activity匹配，Android 让Activity自己处理该Intent。
  - 如果有多个匹配项，Android 会显示一个应用选择器，允许用户选择他们希望执行该操作的应用。
- 将创建一个非常简单的应用，该应用接收用于打开网页 URI 的隐式Intent应用。当由隐式Intent激活时，该应用程序将请求的 URI 显示为TextView。
