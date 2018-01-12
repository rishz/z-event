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
    private String organizer;
    private String description;
    private List categories;

    @Override
    public String toString() {
        return "Event{" +
                "event_uuid=" + event_uuid +
                ", name='" + name + '\'' +
                ", date=" + date +
                ", going=" + going +
                ", interested=" + interested +
                ", organizer=" + organizer +
                ", description='" + description + '\'' +
                ", categories=" + categories +
                '}';
    }

    public List getCategories() {
        return categories;
    }

    public void setCategories(List categories) {
        this.categories = categories;
    }

    public UUID getEvent_uuid() {
        return event_uuid;
    }

    public void setEvent_uuid(UUID event_uuid) {
        this.event_uuid = event_uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public List<User> getGoing() {
        return going;
    }

    public void setGoing(List<User> going) {
        this.going = going;
    }

    public List<User> getInterested() {
        return interested;
    }

    public void setInterested(List<User> interested) {
        this.interested = interested;
    }

    public String getOrganizer() {
        return organizer;
    }

    public void setOrganizer(String creator) {
        this.organizer = creator;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
