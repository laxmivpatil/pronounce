package com.abcm.jwt.DTO;
 
 
import java.util.List;

public class SetPriorityAccentsDTO {

     private List<Long> accentIds; // List of accent IDs in order of priority (1, 2, 3)

    // Getters and Setters
    public List<Long> getAccentIds() {
        return accentIds;
    }

    public void setAccentIds(List<Long> accentIds) {
        this.accentIds = accentIds;
    }
}
