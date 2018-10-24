package com.antelope.android.videoplay;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.play)
    Button mPlay;
    @BindView(R.id.pause)
    Button mPause;
    @BindView(R.id.replay)
    Button mReplay;
    @BindView(R.id.video_view)
    VideoView mVideoView;
    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        //在程序运行时申请权限，android 6.0系统中引入
        //检查权限的方法:ContextCompat.checkSelfPermission(Context,权限名)
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){

            //向用户申请授权
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        } else {
            initVideoPath();
        }
    }

    private void initVideoPath() {
        File file = new File(Environment.getExternalStorageDirectory(),"movie.mp4");
        mVideoView.setVideoPath(file.getPath());
    }

    /**
     * 当用户对请求权限的dialog做出响应之后,系统会调用onRequestPermissionsResult() 方法,传回用户的响应.
     * @param requestCode 为调用requestPermissions()时传入的参数,是app自定义的一个整型值.
     * @param permissions 权限名
     * @param grantResults 授权的结果封装在grantResults参数当中
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    initVideoPath();
                } else {
                    Toast.makeText(MainActivity.this,"拒绝权限将无法使用程序",Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:

        }
    }

    @OnClick({R.id.play, R.id.pause, R.id.replay})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.play:
                if (!mVideoView.isPlaying()){
                    mVideoView.start();
                }
                break;
            case R.id.pause:
                if (mVideoView.isPlaying()){
                    mVideoView.pause();
                }
                break;
            case R.id.replay:
                if (mVideoView.isPlaying()){
                    mVideoView.resume();
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mVideoView != null){
            mVideoView.suspend();
        }
        unbinder.unbind();
    }
}
