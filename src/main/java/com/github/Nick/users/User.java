package com.github.Nick.users;

import java.util.Objects;

//POJO = Plain Ol' Java Object
public class User {

    private final String yellowText = "\u001B[33m";
    private final String defaultText = "\u001B[0m";

    private int id;
    private String name;
    private String task;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(name, user.name) && Objects.equals(task, user.task);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, task);
    }

    @Override
    public String toString() {
        return yellowText + "User " + defaultText + "{" +
                yellowText + "id " + defaultText + "= '" + defaultText + id +  "' " +
                yellowText + "name " + defaultText + "= '" + defaultText + name + "' " +
                yellowText + "task "+ defaultText + "= '" + defaultText + task +
                "'}";
    }
}

/*  set user's id, name, and task from the user or get their 
 * information from the database. Displays the current data
 * from the database useing the above format.
 */