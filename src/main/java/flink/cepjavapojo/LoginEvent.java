package flink.cepjavapojo;

//@Data
//@AllArgsConstructor
//@NoArgsConstructor
public class LoginEvent {
    private int id;
    private String userName;
    private String eventType;
    private long eventTime;

    public LoginEvent() {
    }

    public LoginEvent(int id, String userName, String eventType, long eventTime) {
        this.id = id;
        this.userName = userName;
        this.eventType = eventType;
        this.eventTime = eventTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public long getEventTime() {
        return eventTime;
    }

    public void setEventTime(long eventTime) {
        this.eventTime = eventTime;
    }


    @Override
    public String toString() {
        return "LoginEvent{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", eventType='" + eventType + '\'' +
                ", eventTime=" + eventTime +
                '}';
    }
}
