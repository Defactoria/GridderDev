package leclerc.gridder.activities.chat.gcm;

import com.google.android.gms.iid.InstanceID;
import com.google.android.gms.iid.InstanceIDListenerService;

/**
 * Created by Antoine on 2015-07-02.
 */
public class InstanceIDListener extends InstanceIDListenerService {
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();

        //getToken
    }
}
