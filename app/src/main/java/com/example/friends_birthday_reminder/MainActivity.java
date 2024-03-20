package com.example.friends_birthday_reminder;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Friend> friendsList;
    private ArrayAdapter<Friend> friendsAdapter;
    private ListView friendsListView;

    private EditText inputName;
    private Button selectBirthdayButton;

    private Calendar calendar;
    private SimpleDateFormat dateFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        friendsList = new ArrayList<>(
                new ArrayList<Friend>() {{
                    add(new Friend("Иван", "01.01.2000"));
                    add(new Friend("Петр", "02.02.2000"));
                    add(new Friend("Сидор", "03.03.2000"));
                }}
        );
        friendsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, friendsList);
        friendsListView = findViewById(R.id.friendsListView);
        friendsListView.setAdapter(friendsAdapter);

        Button addFriendButton = findViewById(R.id.addFriendButton);
        addFriendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddFriendDialog();
            }
        });

        // Обработка нажатия на элемент списка
        friendsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showEditOrDeleteDialog(position);
            }
        });

        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
    }

    // Метод для отображения диалога добавления друга
    private void showAddFriendDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Добавить друга");

        // Поле ввода имени друга
        inputName = new EditText(this);
        inputName.setHint("Имя друга");
        //builder.setView(inputName);

        // Кнопка "Выбрать дату рождения"
        selectBirthdayButton = new Button(this);
        selectBirthdayButton.setText("Выбрать дату рождения");
        selectBirthdayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
        //builder.setView(selectBirthdayButton);

        // Создание LinearLayout и добавление в него inputName и selectBirthdayButton
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(inputName);
        layout.addView(selectBirthdayButton);

        // Использование LinearLayout в качестве представления
        builder.setView(layout);

        // Кнопка "Добавить"
        builder.setPositiveButton("Добавить", (dialog, which) -> {
            String name = inputName.getText().toString().trim();
            if (!name.isEmpty()) {
                if (calendar != null) {
                    String birthday = dateFormat.format(calendar.getTime());
                    Friend newFriend = new Friend(name, birthday);
                    friendsList.add(newFriend);
                    friendsAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(MainActivity.this, "Пожалуйста, выберите дату рождения", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(MainActivity.this, "Пожалуйста, введите имя", Toast.LENGTH_SHORT).show();
            }
        });

        // Кнопка "Отмена"
        builder.setNegativeButton("Отмена", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    // Метод для отображения диалога выбора даты рождения
    private void showDatePickerDialog() {
        new DatePickerDialog(this, dateListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    // Слушатель для выбора даты рождения
    private DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            selectBirthdayButton.setText(dateFormat.format(calendar.getTime()));
        }
    };

    // Метод для отображения диалога редактирования или удаления друга
    private void showEditOrDeleteDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Выберите действие");

        // Кнопка "Редактировать"
        builder.setPositiveButton("Редактировать", (dialog, which) -> {
            // Реализация редактирования опущена для краткости
            Toast.makeText(MainActivity.this, "Редактирование друга", Toast.LENGTH_SHORT).show();
        });

        // Кнопка "Удалить"
        builder.setNegativeButton("Удалить", (dialog, which) -> {
            friendsList.remove(position);
            friendsAdapter.notifyDataSetChanged();
            Toast.makeText(MainActivity.this, "Удаление друга", Toast.LENGTH_SHORT).show();
        });

        // Кнопка "Отмена"
        builder.setNeutralButton("Отмена", (dialog, which) -> dialog.cancel());

        builder.show();
    }
}