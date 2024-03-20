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

/**
 * MainActivity is the main screen of the application.
 * It displays a list of friends and provides options to add, edit, or delete a friend.
 */
public class MainActivity extends AppCompatActivity {

    private ArrayList<Friend> friendsList; // List of friends
    private ArrayAdapter<Friend> friendsAdapter; // Adapter for the list of friends
    private ListView friendsListView; // ListView to display the list of friends

    private EditText inputName; // Input field for the name of a friend
    private Button selectBirthdayButton; // Button to select the birthday of a friend

    private Calendar calendar; // Calendar to store the selected birthday
    private SimpleDateFormat dateFormat; // Format for displaying the birthday

    /**
     * Called when the activity is starting.
     * This is where most initialization should go.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down then this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle).
     */
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

        // Initialize the list of friends and the adapter
        friendsList = new ArrayList<>(
                new ArrayList<Friend>() {{
                    add(new Friend("Ivan", "01.01.2000"));
                    add(new Friend("Petr", "02.02.2000"));
                    add(new Friend("Sergey", "03.03.2000"));
                }}
        );
        friendsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, friendsList);
        friendsListView = findViewById(R.id.friendsListView);
        friendsListView.setAdapter(friendsAdapter);

        // Set up the "Add Friend" button
        Button addFriendButton = findViewById(R.id.addFriendButton);
        addFriendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddFriendDialog();
            }
        });

        // Set up the item click listener for the list of friends
        friendsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showEditOrDeleteDialog(position);
            }
        });

        // Initialize the calendar and the date format
        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
    }

    /**
     * Shows a dialog to add a new friend.
     */
    private void showAddFriendDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add a friend");

        // Friend's name input field
        inputName = new EditText(this);
        inputName.setHint("Friend's name");

        // Button "Select date of birth"
        selectBirthdayButton = new Button(this);
        selectBirthdayButton.setText("Select date of birth");
        selectBirthdayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        // Create a LinearLayout and add inputName and selectBirthdayButton to it
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(inputName);
        layout.addView(selectBirthdayButton);

        // Using LinearLayout as a view
        builder.setView(layout);

        // "Add" button
        builder.setPositiveButton("Add", (dialog, which) -> {
            String name = inputName.getText().toString().trim();
            if (!name.isEmpty()) {
                if (calendar != null) {
                    String birthday = dateFormat.format(calendar.getTime());
                    Friend newFriend = new Friend(name, birthday);
                    friendsList.add(newFriend);
                    friendsAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(MainActivity.this, "Please select date of birth", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(MainActivity.this, "Please enter a name", Toast.LENGTH_SHORT).show();
            }
        });

        // Cancel button
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    /**
     * Shows a date picker dialog to select the birthday of a friend.
     */
    private void showDatePickerDialog() {
        new DatePickerDialog(this, dateListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    /**
     * Listener for the date picker dialog.
     * Updates the selected birthday when a date is set.
     */
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

    /**
     * Shows a dialog to edit or delete a friend.
     *
     * @param position The position of the friend in the list.
     */
    private void showEditOrDeleteDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose an action");

        // "Edit" button
        builder.setPositiveButton("Edit", (dialog, which) -> {
            showEditFriendDialog(position);
        });

        // Button "Delete"
        builder.setNegativeButton("Delete", (dialog, which) -> {
            friendsList.remove(position);
            friendsAdapter.notifyDataSetChanged();
            Toast.makeText(MainActivity.this, "Removing a friend", Toast.LENGTH_SHORT).show();
        });

        // Cancel button
        builder.setNeutralButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    /**
     * Shows a dialog to edit a friend.
     *
     * @param position The position of the friend in the list.
     */
    private void showEditFriendDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit friend");

        // Friend's name input field
        inputName = new EditText(this);
        inputName.setText(friendsList.get(position).getName());

        // Button "Select date of birth"
        selectBirthdayButton = new Button(this);
        selectBirthdayButton.setText(friendsList.get(position).getBirthday());
        selectBirthdayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        // Create a LinearLayout and add inputName and selectBirthdayButton to it
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(inputName);
        layout.addView(selectBirthdayButton);

        // Using LinearLayout as a view
        builder.setView(layout);

        // "Save" button
        builder.setPositiveButton("Save", (dialog, which) -> {
            String name = inputName.getText().toString().trim();
            if (!name.isEmpty()) {
                if (calendar != null) {
                    String birthday = dateFormat.format(calendar.getTime());
                    Friend updatedFriend = new Friend(name, birthday);
                    friendsList.set(position, updatedFriend);
                    friendsAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(MainActivity.this, "Please select date of birth", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(MainActivity.this, "Please enter a name", Toast.LENGTH_SHORT).show();
            }
        });

        // Cancel button
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }
}