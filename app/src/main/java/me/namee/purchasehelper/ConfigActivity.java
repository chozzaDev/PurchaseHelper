package me.namee.purchasehelper;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import io.realm.Realm;
import me.namee.purchasehelper.databinding.ActivityConfigBinding;

public class ConfigActivity extends AppCompatActivity {

    Realm realm;
    PurchaseConfig config;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        Realm.init(this);
        realm = Realm.getDefaultInstance();
        config = PurchaseConfig.get(realm);
        final ActivityConfigBinding b = DataBindingUtil.setContentView(this, R.layout.activity_config);
        b.gmarketId.setText(config.gmarketId);
        b.gmarketPw.setText(config.gmarketPw);
        b.waitingTime.setText(config.waitingTime + "");
        b.refreshTime.setText(config.refreshTime + "");
        b.naverId.setText(config.naverId);
        b.naverPw.setText(config.naverPw);
        b.naverUrl.setText(config.naverUrl);
        b.publicId.setText(config.publicId);
        b.publicPw.setText(config.publicPw);
        b.publicUrl.setText(config.publicUrl);
//        b.naverPayPw.setText(config.naverPayPw);
        b.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                realm.beginTransaction();
                config.gmarketId = b.gmarketId.getText().toString();
                config.gmarketPw = b.gmarketPw.getText().toString();
                config.waitingTime = Float.parseFloat(b.waitingTime.getText().toString());
                config.refreshTime = Float.parseFloat(b.refreshTime.getText().toString());
                config.naverId = b.naverId.getText().toString();
                config.naverPw = b.naverPw.getText().toString();
                config.naverUrl = b.naverUrl.getText().toString();
                config.publicId = b.publicId.getText().toString();
                config.publicPw = b.publicPw.getText().toString();
                config.publicUrl = b.publicUrl.getText().toString();
//                config.naverPayPw = b.naverPayPw.getText().toString();
                realm.commitTransaction();
                Util.toast(getApplicationContext(), "저장되었습니다.");
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }
}