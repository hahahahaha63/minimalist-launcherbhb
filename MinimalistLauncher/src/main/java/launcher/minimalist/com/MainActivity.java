
package launcher.minimalist.com;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Gravity;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity {

    private PackageManager packageManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        packageManager = getPackageManager();

        // Main screen
        LinearLayout mainLayout = new LinearLayout(this);
        mainLayout.setOrientation(LinearLayout.VERTICAL);
        mainLayout.setGravity(Gravity.CENTER);
        mainLayout.setBackgroundColor(Color.WHITE);

        // 2 x 2 grid
        GridLayout grid = new GridLayout(this);
        grid.setColumnCount(2);
        grid.setRowCount(2);

        // Phone
        addAppIcon(
                grid,
                "phone_icon",
                "Phone",
                getPhoneIntent()
        );

        // Messages
        addAppIcon(
                grid,
                "messages_icon",
                "Messages",
                getMessagesIntent()
        );

        // Contacts
        addAppIcon(
                grid,
                "contacts_icon",
                "Contacts",
                getContactsIntent()
        );

        // Settings
        addAppIcon(
                grid,
                "settings_icon",
                "Settings",
                new Intent(Settings.ACTION_SETTINGS)
        );

        mainLayout.addView(grid);

        setContentView(mainLayout);
    }

    private void addAppIcon(
            GridLayout grid,
            String iconName,
            String appName,
            Intent intent
    ) {

        LinearLayout item = new LinearLayout(this);

        item.setOrientation(LinearLayout.VERTICAL);
        item.setGravity(Gravity.CENTER);

        int width = 300;
        int height = 300;

        GridLayout.LayoutParams params =
                new GridLayout.LayoutParams();

        params.width = width;
        params.height = height;

        item.setLayoutParams(params);

        // Icon
        ImageView icon = new ImageView(this);

        int resourceId =
                getResources().getIdentifier(
                        iconName,
                        "drawable",
                        getPackageName()
                );

        if (resourceId != 0) {

            icon.setImageResource(resourceId);

        } else {

            // If custom icon isn't found,
            // show a simple placeholder.
            icon.setImageDrawable(null);
        }

        icon.setLayoutParams(
                new LinearLayout.LayoutParams(
                        150,
                        150
                )
        );

        icon.setScaleType(
                ImageView.ScaleType.CENTER_INSIDE
        );

        item.addView(icon);

        // App name
        TextView text = new TextView(this);

        text.setText(appName);
        text.setTextSize(18);
        text.setTextColor(Color.BLACK);
        text.setGravity(Gravity.CENTER);

        item.addView(text);

        // Open app when tapped
        item.setOnClickListener(v -> {

            try {

                startActivity(intent);

            } catch (Exception e) {

                // If the normal app can't be found,
                // do nothing rather than crashing.
            }
        });

        grid.addView(item);
    }

    private Intent getPhoneIntent() {

        Intent intent =
                new Intent(Intent.ACTION_DIAL);

        return intent;
    }

    private Intent getMessagesIntent() {

        Intent intent =
                new Intent(Intent.ACTION_MAIN);

        intent.addCategory(Intent.CATEGORY_APP_MESSAGING);

        return intent;
    }

    private Intent getContactsIntent() {

        Intent intent =
                new Intent(Intent.ACTION_MAIN);

        intent.addCategory(Intent.CATEGORY_APP_CONTACTS);

        return intent;
    }
}
