package xyz.tangram.arch.demo;

import android.arch.lifecycle.LifecycleActivity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import xyz.tangram.arch.ModuleResult;
import xyz.tangram.arch.demo.entity.LoginResult;

/**
 * 创建人：付三
 * 创建时间：2017/8/31 18:13
 */

public class LoginActivity extends LifecycleActivity implements View.OnClickListener {
    private LoginViewModel mLoginViewModel;
    private Button mLoginBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity_layout);

        // step0 获取相关的viewModel
        mLoginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);

        // step1 获取view
        mLoginBtn = (Button) findViewById(R.id.loginBtn);

        // step2 设置监听
        mLoginBtn.setOnClickListener(this);

        // setp3 绑定数据
        mLoginViewModel.loginResult.observe(this, mLoginObserver);
    }

    private Observer<ModuleResult<LoginResult>> mLoginObserver = new Observer<ModuleResult<LoginResult>>() {
        @Override
        public void onChanged(@Nullable ModuleResult<LoginResult> result) {
            Toast.makeText(getApplicationContext(), "data=" + result.data() + " e=" + result.throwable(), Toast.LENGTH_SHORT).show();
        }
    };


    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.loginBtn:
                mLoginViewModel.login("fusang", "abc123456");
                break;

        }
    }

}
