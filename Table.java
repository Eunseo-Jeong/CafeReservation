package com.example.lastse;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class Table {

    public int table_num;
    public boolean available;
    public int capacity;
    public String time;
    public String reserved;

    public int starCount = 0;
    public Map<String, Boolean> stars = new HashMap<>();

    public Table() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public Table(int table_num, int capacity) {
        this.table_num = table_num;
        this.available = true;
        this.capacity = capacity;
        this.reserved = "none";
        this.time ="none";
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("table_num", table_num);
        result.put("available", available);
        result.put("capacity", capacity);

        return result;
    }
    // [END post_to_map]

}