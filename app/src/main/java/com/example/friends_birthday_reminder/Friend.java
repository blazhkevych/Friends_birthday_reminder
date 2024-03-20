package com.example.friends_birthday_reminder;

/**
 * The Friend class represents a friend model.
 * Each friend has a name and a birthday.
 */
public class Friend {
    private String name; // The name of the friend
    private String birthday; // The birthday of the friend

    /**
     * The constructor for the Friend class.
     *
     * @param name     The name of the friend.
     * @param birthday The birthday of the friend.
     */
    public Friend(String name, String birthday) {
        this.name = name;
        this.birthday = birthday;
    }

    /**
     * A method to get the name of the friend.
     *
     * @return The name of the friend.
     */
    public String getName() {
        return name;
    }

    /**
     * A method to get the birthday of the friend.
     *
     * @return The birthday of the friend.
     */
    public String getBirthday() {
        return birthday;
    }

    /**
     * A method to represent the Friend object as a string.
     *
     * @return A string in the format "Name (Birthday)".
     */
    @Override
    public String toString() {
        return name + " (" + birthday + ")";
    }
}