package com.example.gamal.adnp3_bakingapp.BakingAppWidgets;

import android.content.Intent;
import android.os.IBinder;
import android.widget.RemoteViewsService;
import android.widget.Toast;

public class BakingAppWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new IngredientsListWidgetProvider(this.getApplicationContext(), intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "hello there" + flags, Toast.LENGTH_SHORT).show();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return super.onBind(intent);
    }
}
