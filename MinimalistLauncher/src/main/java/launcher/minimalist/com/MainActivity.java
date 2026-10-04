import android.widget.AbsListView;
package launcher.minimalist.com;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends Activity {

    private PackageManager packageManager;
    private ArrayList<String> packageNames;
    private ArrayList<Drawable> appIcons;
    private IconAdapter adapter;
    private ListView listView;

    private final BroadcastReceiver packageChangeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            fetchAppList();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        listView = new ListView(this);
        listView.setVerticalScrollBarEnabled(false);
        listView.setDivider(null);
        listView.setBackgroundColor(Color.WHITE);

        setContentView(listView);

        ViewGroup.MarginLayoutParams p =
                (ViewGroup.MarginLayoutParams) listView.getLayoutParams();

        p.setMargins(100, 0, 0, 0);

        packageManager = getPackageManager();

        packageNames = new ArrayList<>();
        appIcons = new ArrayList<>();

        adapter = new IconAdapter(this);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            try {
                startActivity(
                        packageManager.getLaunchIntentForPackage(
                                packageNames.get(position)
                        )
                );
            } catch (Exception e) {
                fetchAppList();
            }
        });

        listView.setOnItemLongClickListener((parent, view, position, id) -> {
            try {
                Intent intent =
                        new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);

                intent.setData(
                        Uri.parse("package:" + packageNames.get(position))
                );

                startActivity(intent);

            } catch (ActivityNotFoundException e) {
                fetchAppList();
            }

            return true;
        });

        IntentFilter packageFilter = new IntentFilter();

        packageFilter.addAction(Intent.ACTION_PACKAGE_ADDED);
        packageFilter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        packageFilter.addAction(Intent.ACTION_PACKAGE_CHANGED);
        packageFilter.addAction(Intent.ACTION_PACKAGE_REPLACED);

        packageFilter.addDataScheme("package");

        registerReceiver(packageChangeReceiver, packageFilter);

        fetchAppList();
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(packageChangeReceiver);
        super.onDestroy();
    }

    private void fetchAppList() {

        packageNames.clear();
        appIcons.clear();

        List<ResolveInfo> activities =
                packageManager.queryIntentActivities(
                        new Intent(Intent.ACTION_MAIN, null)
                                .addCategory(Intent.CATEGORY_LAUNCHER),
                        0
                );

        Collections.sort(
                activities,
                new ResolveInfo.DisplayNameComparator(packageManager)
        );

        for (ResolveInfo resolver : activities) {

            String appName =
                    (String) resolver.loadLabel(packageManager);

            if (appName.equals("Settings")
                    || appName.equals("Minimalist Launcher")) {
                continue;
            }

            packageNames.add(
                    resolver.activityInfo.packageName
            );

            appIcons.add(
                    resolver.loadIcon(packageManager)
            );
        }

        adapter.notifyDataSetChanged();
    }

    private class IconAdapter extends BaseAdapter {

        private final Context context;

        IconAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return appIcons.size();
        }

        @Override
        public Object getItem(int position) {
            return appIcons.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(
                int position,
                View convertView,
                ViewGroup parent) {

            ImageView icon;

            if (convertView == null) {

                icon = new ImageView(context);

                int size = 120;

                icon.setLayoutParams(
                        new AbsListView.LayoutParams(
                                size,
                                size
                        )
                );

                icon.setPadding(20, 20, 20, 20);
                icon.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

                icon.setGravity(Gravity.CENTER);

            } else {

                icon = (ImageView) convertView;
            }

            icon.setImageDrawable(
                    appIcons.get(position)
            );

            return icon;
        }
    }
}

