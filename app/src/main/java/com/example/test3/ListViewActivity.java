package com.example.test3;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListViewActivity extends AppCompatActivity {

    private static final String CHANNEL_ID = "animal_list_channel";
    private static final int NOTIFICATION_ID = 1;

    private final String[] animalNames = {"Lion", "Tiger", "Monkey", "Dog", "Cat"};
    private final String[] animalDescriptions = {
            "草原之王，威武雄壮",
            "森林之王，力量与美的象征",
            "聪明的灵长类动物，活泼好动",
            "人类最好的朋友，忠诚可爱",
            "可爱的家养宠物，喜欢玩耍"
    };
    private final int[] animalIcons = {
            R.drawable.lion,
            R.drawable.tiger,
            R.drawable.monkey,
            R.drawable.dog,
            R.drawable.cat
    };

    private ActionMode actionMode;
    private List<Integer> selectedItems = new ArrayList<>();

    // ActionMode 菜单项ID
    private static final int ACTION_MENU_DELETE = 1;
    private static final int ACTION_MENU_SHARE = 2;
    private static final int ACTION_MENU_COPY = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview);

        createNotificationChannel();
        setupListView();
    }

    private void setupListView() {
        List<Map<String, Object>> dataList = new ArrayList<>();

        for (int i = 0; i < animalNames.length; i++) {
            Map<String, Object> item = new HashMap<>();
            item.put("icon", animalIcons[i]);
            item.put("title", animalNames[i]);
            item.put("description", animalDescriptions[i]);
            dataList.add(item);
        }

        String[] from = {"icon", "title", "description"};
        int[] to = {R.id.item_icon, R.id.item_title, R.id.item_subtitle};

        SimpleAdapter adapter = new SimpleAdapter(this, dataList, R.layout.list_item, from, to);
        ListView listView = findViewById(R.id.listView);
        listView.setAdapter(adapter);

        // 设置普通点击事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String toastMessage = "Selected: " + animalNames[position];
                Toast.makeText(ListViewActivity.this, toastMessage, Toast.LENGTH_SHORT).show();
                sendNotification(animalNames[position], animalDescriptions[position]);
            }
        });

        // 设置多选模式和ActionMode
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                // 添加ActionMode菜单项
                menu.add(0, ACTION_MENU_DELETE, 0, "Delete");
                menu.add(0, ACTION_MENU_SHARE, 0, "Share");
                menu.add(0, ACTION_MENU_COPY, 0, "Copy");

                mode.setTitle("1 selected");
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch (item.getItemId()) {
                    case ACTION_MENU_DELETE:
                        deleteSelectedItems();
                        mode.finish();
                        return true;
                    case ACTION_MENU_SHARE:
                        shareSelectedItems();
                        mode.finish();
                        return true;
                    case ACTION_MENU_COPY:
                        copySelectedItems();
                        mode.finish();
                        return true;
                    default:
                        return false;
                }
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                actionMode = null;
                selectedItems.clear();
                // 清除选择状态
                for (int i = 0; i < listView.getCount(); i++) {
                    listView.setItemChecked(i, false);
                }
            }

            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                if (checked) {
                    if (!selectedItems.contains(position)) {
                        selectedItems.add(position);
                    }
                } else {
                    selectedItems.remove((Integer) position);
                }

                // 更新ActionMode标题显示选中数量
                int count = selectedItems.size();
                mode.setTitle(count + " selected");
            }
        });
    }

    private void deleteSelectedItems() {
        if (!selectedItems.isEmpty()) {
            StringBuilder selectedNames = new StringBuilder();
            for (int position : selectedItems) {
                if (selectedNames.length() > 0) {
                    selectedNames.append(", ");
                }
                selectedNames.append(animalNames[position]);
            }
            Toast.makeText(this, "Deleted: " + selectedNames.toString(), Toast.LENGTH_LONG).show();
            // 这里可以添加实际的删除逻辑
        }
    }

    private void shareSelectedItems() {
        if (!selectedItems.isEmpty()) {
            StringBuilder selectedNames = new StringBuilder();
            for (int position : selectedItems) {
                if (selectedNames.length() > 0) {
                    selectedNames.append(", ");
                }
                selectedNames.append(animalNames[position]);
            }
            String shareText = "Sharing: " + selectedNames.toString();
            Toast.makeText(this, shareText, Toast.LENGTH_LONG).show();
            // 这里可以添加实际的分享逻辑
        }
    }

    private void copySelectedItems() {
        if (!selectedItems.isEmpty()) {
            StringBuilder selectedNames = new StringBuilder();
            for (int position : selectedItems) {
                if (selectedNames.length() > 0) {
                    selectedNames.append(", ");
                }
                selectedNames.append(animalNames[position]);
            }
            Toast.makeText(this, "Copied: " + selectedNames.toString(), Toast.LENGTH_LONG).show();
            // 这里可以添加实际的复制逻辑
        }
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Animal List";
            String description = "Animal selection notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void sendNotification(String animalName, String animalDescription) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(animalName)
                .setContentText("You selected: " + animalName)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(animalDescription))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }
}