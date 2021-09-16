package com.example.team_project;

import static android.content.Context.MODE_PRIVATE;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import com.example.team_project.main.MainActivity;

public class WidgetProvider extends AppWidgetProvider {
    static String eat;
    static int goal;
    static int left;
    static int a = 0;
    private static final String REFRESH = "refresh";
    static RemoteViews remoteViews;
    static AppWidgetManager appWidgetManager;
    static int[] widgetIds;

    /**
     * 브로드캐스트를 수신할때, Override된 콜백 메소드가 호출되기 직전에 호출됨
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        appWidgetManager = AppWidgetManager.getInstance(context);
        ComponentName myWidget = new ComponentName(context.getPackageName(), WidgetProvider.class.getName());
        widgetIds = appWidgetManager.getAppWidgetIds(myWidget);
        String action = intent.getAction();

        if (action.equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE)) {
            if (widgetIds != null & widgetIds.length > 0) {
                this.onUpdate(context, AppWidgetManager.getInstance(context), widgetIds); // onUpdate 호출
            }
        } else if (action.equals(REFRESH)) {
            widgetRefresh();
        }
    }

    /**
     * 위젯을 갱신할때 호출됨 * * 주의 : Configure Activity를 정의했을때는 위젯 등록시 처음 한번은 호출이 되지 않습니다
     */
    //위젯이 설치될 때마다 호출되는 함수
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, getClass()));
        for (int i = 0; i < appWidgetIds.length; i++) {//생성한 위젯을 모두 확인한다, 그래야 여러개의 위젯 생성시 한번에 업데이트 가능
            updateAppWidget(context, appWidgetManager, appWidgetIds[i]);
        }
        Log.i("lhh", "onUpdate ");

    }

    //위젯의 크기 및 옵션이 변결될 때마다 호출되는 함수
    public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                       int appWidgetId) { /** * 현재 시간 정보를 가져오기 위한 Calendar */

        getSharedPreferencesData(context);

        /** * 레이아웃을 클릭하면 앱으로이동 */
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.mLayout, pendingIntent); /** * 위젯 업데이트 */


        /** 새로고침 버튼 */
        Intent intent2 = new Intent(context, WidgetProvider.class);
        intent2.setAction(REFRESH);
        PendingIntent pendingIntent2 = PendingIntent.getBroadcast(context, 0, intent2, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.refresh_img, pendingIntent2);


        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
    }


    /**
     * 위젯이 처음 생성될때 호출됨 * * 동일한 위젯이 생성되도 최초 생성때만 호출됨
     */
    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        Log.i("lhh", "onEnabled ");

    }

    /**
     * 위젯의 마지막 인스턴스가 제거될때 호출됨 * * onEnabled()에서 정의한 리소스 정리할때
     */
    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
        Log.i("lhh", "onDisabled ");

    }

    /**
     * 위젯이 사용자에 의해 제거될때 호출됨
     */
    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
        Log.i("lhh", "onDeleted ");

    }

    public void widgetRefresh() {
        Log.i("lhh", "Clicked widgetRefresh");
        remoteViews.setViewVisibility(R.id.refresh_img, View.GONE);
        remoteViews.setViewVisibility(R.id.progressBarLL, View.VISIBLE);
        appWidgetManager.updateAppWidget(widgetIds, remoteViews);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        remoteViews.setViewVisibility(R.id.progressBarLL, View.GONE);
        remoteViews.setViewVisibility(R.id.refresh_img, View.VISIBLE);
        appWidgetManager.updateAppWidget(widgetIds, remoteViews);
    }

    public static void getSharedPreferencesData(Context context) {
        SharedPreferences pref = context.getSharedPreferences("sharedpreferences", MODE_PRIVATE);
        eat = pref.getString("nickname", "Unknown");
        goal = pref.getInt("weight", 0);
        left = pref.getInt("calorie", 0);

        Log.i("lhh", "updateAppWidget ");
        remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
        remoteViews.setTextViewText(R.id.eat_calorie, "먹은 칼로리" + eat + a++);
        remoteViews.setTextViewText(R.id.goal_calorie, "먹은 칼로리" + goal);
        remoteViews.setTextViewText(R.id.left_calorie, "먹은 칼로리" + left);
    }

}

