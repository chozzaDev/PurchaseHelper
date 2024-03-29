package me.namee.purchasehelper;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.exceptions.RealmMigrationNeededException;

public class MainActivity extends AppCompatActivity {

    Realm realm;
    Button btnGmarketStart;
    Button btnNaverStart;
    Button btnPublicStart;
    Button btnCampingStart;

    FloatingActionButton btnConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Realm.init(this);
        try {
            realm = Realm.getDefaultInstance();
        } catch (RealmMigrationNeededException r) {
            RealmConfiguration config = new RealmConfiguration.Builder().build();
            Realm.deleteRealm(config);
            realm = Realm.getDefaultInstance();
        }
        btnGmarketStart = findViewById(R.id.btnGmarketStart);
        btnGmarketStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PurchaseConfig config = PurchaseConfig.get(realm);
                if(config.runnableGmarket()) {
                    Intent intent = new Intent(MainActivity.this, GMarketActivity.class);
                    startActivity(intent);
                } else {
                    Util.toast(getApplicationContext(), "GMarket 설정이 없습니다.");
                }
            }
        });
        btnNaverStart = findViewById(R.id.btnNaverStart);
        btnNaverStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PurchaseConfig config = PurchaseConfig.get(realm);
                if(config.runnableNaver()) {
                    Intent intent = new Intent(MainActivity.this, NaverActivity.class);
                    startActivity(intent);
                } else {
                    Util.toast(getApplicationContext(), "Naver 설정이 없습니다.");
                }
            }
        });
        btnPublicStart = findViewById(R.id.btnPublicStart);
        btnPublicStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PurchaseConfig config = PurchaseConfig.get(realm);
                if(config.runnablePublic()) {
                    Intent intent = new Intent(MainActivity.this, PublicActivity.class);
                    startActivity(intent);
                } else {
                    Util.toast(getApplicationContext(), "공영쇼핑 설정이 없습니다.");
                }
            }
        });
        btnCampingStart = findViewById(R.id.btnCampingStart);
        btnCampingStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PurchaseConfig config = PurchaseConfig.get(realm);
                if(config.runnableCamping()) {
                    Intent intent = new Intent(MainActivity.this, CampingActivity.class);
                    startActivity(intent);
                } else {
                    Util.toast(getApplicationContext(), "캠핑예약 설정이 없습니다.");
                }
            }
        });

        btnConfig = findViewById(R.id.btnConfig);
        btnConfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ConfigActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    public void goConfig(boolean alert) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("버튼 추가 예제").setMessage("선택하세요.");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Toast.makeText(getApplicationContext(), "OK Click", Toast.LENGTH_SHORT).show();
            }
        });
    }
}