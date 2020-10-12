package me.namee.purchasehelper;

import com.google.common.base.Strings;

import io.realm.Realm;
import io.realm.RealmObject;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class PurchaseConfig extends RealmObject {
    String gmarketId;
    String gmarketPw;
    String naverId;
    String naverPw;
    String naverUrl;
    String naverPayPw;
    String publicId;
    String publicPw;
    String publicUrl;
    float waitingTime = 30;
    float refreshTime = 0.5f;

    public static PurchaseConfig get(Realm realm) {
        PurchaseConfig config = realm.where(PurchaseConfig.class).findFirst();
        if(config == null) {
            realm.beginTransaction();
            config = realm.createObject(PurchaseConfig.class);
            realm.commitTransaction();
        }
        return config;
    }

    public boolean runnableGmarket() {
        return !Strings.isNullOrEmpty(gmarketId) && !Strings.isNullOrEmpty(gmarketPw) && refreshTime > 0 && waitingTime > 0;
    }

    public boolean runnableNaver() {
        return !Strings.isNullOrEmpty(naverId)
                && !Strings.isNullOrEmpty(naverPw)
                && !Strings.isNullOrEmpty(naverUrl)
                && refreshTime > 0 && waitingTime > 0;
    }

    public boolean runnablePublic() {
        return !Strings.isNullOrEmpty(publicId)
                && !Strings.isNullOrEmpty(publicPw)
                && !Strings.isNullOrEmpty(publicUrl)
                && refreshTime > 0 && waitingTime > 0;
    }

    public PurchaseConfig copy() {
        PurchaseConfig config = new PurchaseConfig();
        config.gmarketId = gmarketId;
        config.gmarketPw = gmarketPw;
        config.naverId = naverId;
        config.naverPw = naverPw;
        config.naverUrl = naverUrl;
        config.naverPayPw = naverPayPw;
        config.publicId = publicId;
        config.publicPw = publicPw;
        config.publicUrl = publicUrl;
        config.waitingTime = waitingTime;
        config.refreshTime = refreshTime;
        return config;
    }
}
