package launcher.minimalist.com;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Main screen
        LinearLayout mainLayout = new LinearLayout(this);
        mainLayout.setOrientation(LinearLayout.VERTICAL);
        mainLayout.setGravity(Gravity.CENTER);
        mainLayout.setBackgroundColor(0xFFFFFFFF);

        // First row
        LinearLayout topRow = new LinearLayout(this);
        topRow.setOrientation(LinearLayout.HORIZONTAL);
        topRow.setGravity(Gravity.CENTER);

        // Second row
        LinearLayout bottomRow = new LinearLayout(this);
        bottomRow.setOrientation(LinearLayout.HORIZONTAL);
        bottomRow.setGravity(Gravity.CENTER);

        // Add the four apps
        addAppIcon(
                topRow,
                "phone_icon",
                "Phone",
                new Intent(Intent.ACTION_DIAL)
        );

        addAppIcon(
                topRow,
                "messages_icon",
                "Messages",
                new Intent(Intent.ACTION_MAIN)
                        .addCategory(Intent.CATEGORY_APP_MESSAGING)
        );

        addAppIcon(
                bottomRow,
                "contacts_icon",
                "Contacts",
                new Intent(Intent.ACTION_MAIN)
                        .addCategory(Intent.CATEGORY_APP_CONTACTS)
        );

        addAppIcon(
                bottomRow,
                "settings_icon",
                "Settings",
                new Intent(Settings.ACTION_SETTINGS)
        );

        // Add rows to screen
        mainLayout.addView(topRow);
        mainLayout.addView(bottomRow);

        setContentView(mainLayout);
    }

    private void addAppIcon(
            LinearLayout row,
            String iconName,
            String appName,
            final Intent intent
    ) {

        LinearLayout item = new LinearLayout(this);

        item.setOrientation(LinearLayout.VERTICAL);
        item.setGravity(Gravity.CENTER);

        LinearLayout.LayoutParams itemParams =
                new LinearLayout.LayoutParams(
                        0,
                        300,
                        1
                );

        item.setLayoutParams(itemParams);

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

        // Name underneath icon
        TextView text = new TextView(this);

        text.setText(appName);
        text.setTextSize(18);
        text.setTextColor(0xFF000000);
        text.setGravity(Gravity.CENTER);

        item.addView(text);

        // Open app
        item.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            startActivity(intent);
                        } catch (Exception e) {
                            // App isn't available.
                        }
                    }
                }
        );

        row.addView(item);
    }
}
