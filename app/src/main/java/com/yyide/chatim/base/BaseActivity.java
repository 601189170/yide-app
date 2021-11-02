package com.yyide.chatim.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.blankj.utilcode.util.ActivityUtils;
import com.tencent.qcloud.tim.uikit.TUIKit;
import com.tencent.qcloud.tim.uikit.base.IMEventListener;
import com.yyide.chatim.LoginActivity;
import com.yyide.chatim.R;
import com.yyide.chatim.model.UserInfo;
import com.yyide.chatim.net.AppClient;
import com.yyide.chatim.net.DingApiStores;
import com.yyide.chatim.utils.ButtonUtils;
import com.yyide.chatim.utils.ClickUtils;
import com.yyide.chatim.utils.Constants;
import com.yyide.chatim.utils.DemoLog;
import com.yyide.chatim.utils.LoadingTools;
import com.yyide.chatim.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Call;

/**
 * 登录状态的Activity都要集成该类，来完成被踢下线等监听处理。
 */
public abstract class BaseActivity extends AppCompatActivity {
    public Activity mActivity;
    private CompositeDisposable mCompositeDisposable;
    private List<Call> calls;
    private static final String TAG = BaseActivity.class.getSimpleName();
    private Unbinder unbinder;
    private LoadingTools loading;
    protected DingApiStores mDingApiStores;
    protected Disposable mDisposable;
    // 监听做成静态可以让每个子类重写时都注册相同的一份。
    private static IMEventListener mIMEventListener = new IMEventListener() {
        @Override
        public void onForceOffline() {
            //ToastUtil.toastLongMessage("您的帐号已在其它终端登录");
            //logout(BaseApplication.getInstance());
        }
    };

    public abstract int getContentViewID();

    public static void logout(Context context) {
        DemoLog.i(TAG, "logout");
        UserInfo.getInstance().setToken("");
        UserInfo.getInstance().setAutoLogin(false);

        ActivityUtils.finishAllActivities();
        Intent intent = new Intent(context, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(Constants.LOGOUT, true);
        context.startActivity(intent);
    }

    //无参跳转
    protected void jupm(Context context, Class activity) {
        if (!ButtonUtils.isFastDoubleClick(activity.hashCode(), 1500)) {
            startActivity(new Intent(context, activity));
        }
    }

    //带参跳转
    protected void jupm(Context context, Class activity, String key, String obj) {
        Intent intent = new Intent(context, activity);
        intent.putExtra(key, obj);
        startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        DemoLog.i(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(getContentViewID());
        mActivity = this;
        unbinder = ButterKnife.bind(this);
        loading = new LoadingTools(this);
        mDingApiStores = AppClient.getDingRetrofit().create(DingApiStores.class);
        setStatusBar(getResources().getColor(R.color.white));
        //这里注意下 因为在评论区发现有网友调用setRootViewFitsSystemWindows 里面 winContent.getChildCount()=0 导致代码无法继续
        //是因为你需要在setContentView之后才可以调用 setRootViewFitsSystemWindows

        //当FitsSystemWindows设置 true 时，会在屏幕最上方预留出状态栏高度的 padding
//        ImmersionBar.with(this).init();
        TUIKit.addIMEventListener(mIMEventListener);
    }

    protected void setStatusBar(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            getWindow().setStatusBarColor(color);
            getWindow().setNavigationBarColor(color);
            int vis = getWindow().getDecorView().getSystemUiVisibility();
            vis |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            vis |= View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR;
            getWindow().getDecorView().setSystemUiVisibility(vis);
        }
    }

    @Override
    public void setContentView(int layoutResID) {
        setContentView(View.inflate(this, layoutResID, null));
    }

//    @Override
//    public void setContentView(View view) {
//        ((ViewGroup) this.findViewById(R.id.root_content)).addView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//    }

    @Override
    protected void onStart() {
        DemoLog.i(TAG, "onStart");
        super.onStart();
//        boolean login = UserInfo.getInstance().isAutoLogin();
//        if (!login) {
//            BaseActivity.logout(BaseApplication.getInstance());
//        }
    }

    @Override
    protected void onResume() {
        DemoLog.i(TAG, "onResume");
        super.onResume();
        ClickUtils.clear();
    }

    @Override
    protected void onPause() {
        DemoLog.i(TAG, "onPause");
        super.onPause();
    }

    @Override
    protected void onStop() {
        DemoLog.i(TAG, "onStop");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        DemoLog.i(TAG, "onDestroy");
        callCancel();
        onUnsubscribe();
        if (unbinder != null) {
            unbinder.unbind();
        }
        hideLoading();
        loading = null;
        mActivity = null;
        if(mDisposable != null && !mDisposable.isDisposed()){
            mDisposable.dispose();
        }
        super.onDestroy();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        DemoLog.i(TAG, "onNewIntent");
        super.onNewIntent(intent);
    }

    public void addCalls(Call call) {
        if (calls == null) {
            calls = new ArrayList<>();
        }
        calls.add(call);
    }

    private void callCancel() {
        if (calls != null && calls.size() > 0) {
            for (Call call : calls) {
                if (!call.isCanceled())
                    call.cancel();
            }
            calls.clear();
        }
    }

    public void addSubscription(Observable observable, Observer subscriber) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = new CompositeDisposable();
        }
        observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void addSubscription(Disposable mDisposable) {
        if (mCompositeDisposable == null && mCompositeDisposable.isDisposed()) {
            mCompositeDisposable = new CompositeDisposable();
        }
        mCompositeDisposable.add(mDisposable);
    }

    public void onUnsubscribe() {
        LogUtil.d("onUnsubscribe");
        //取消注册，以避免内存泄露
        if (mCompositeDisposable != null) {
            mCompositeDisposable.clear();
            mCompositeDisposable = null;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();//返回
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void showLoading() {
        if (loading != null && !isFinishing()) {
            loading.showLoading();
        }
    }

    public void hideLoading() {
        if (loading != null) {
            loading.closeLoading();
        }
    }

}
