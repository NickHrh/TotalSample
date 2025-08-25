package com.example.totalsample;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewbinding.ViewBinding;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class BaseActivity<VB extends ViewBinding> extends AppCompatActivity {

    protected VB vBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vBinding = initViewBinding();
        setContentView(vBinding.getRoot());

    }


    private VB initViewBinding() {
        try {
            // 获取泛型参数的实际类型
            Type superclass = getClass().getGenericSuperclass();
            if (superclass instanceof ParameterizedType) {
                Type type = ((ParameterizedType) superclass).getActualTypeArguments()[0];
                Class<VB> clazz = (Class<VB>) type;

                // 反射调用 XXXBinding.inflate(LayoutInflater)
                Method inflate = clazz.getMethod("inflate", android.view.LayoutInflater.class);
                return (VB) inflate.invoke(null, getLayoutInflater());
            }
        } catch (Exception e) {
            Log.e("BaseActivity", "MSG:" + e.getMessage());
        }
        throw new RuntimeException("ViewBinding 初始化失败！");
    }


    public VB getViewBinding() {
        return vBinding;
    }
}