package com.example.friends_birthday_reminder;

public class Friend {
    private String name;
    private String birthday;

    public Friend(String name, String birthday) {
        this.name = name;
        this.birthday = birthday;
    }

    public String getName() {
        return name;
    }

    public String getBirthday() {
        return birthday;
    }

    @Override
    public String toString() {
        return name + " (" + birthday + ")";
    }
}
