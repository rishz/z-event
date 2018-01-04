package model;

import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
public class Event {
    private UUID event_uuid;
    private String name;
    private Date date;
    private List<User> going;
    private List<User> interested;


}
