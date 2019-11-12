package com.work.olexii.after_dark.domain;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String text;
    private String tag;
    @ManyToOne
    private User user;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yy HH:mm")
    private LocalDateTime createTime;
    private long epochSecond;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public User getUser() {
        return user;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public long getEpochSecond() {
        return epochSecond;
    }

    public void setEpochSecond(long epochSecond) {
        this.epochSecond = epochSecond;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return id == message.id &&
                Objects.equals(text, message.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, text);
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", tag='" + tag + '\'' +
                ", user=" + user +
                ", createTime=" + createTime +
                ", epochSecond=" + epochSecond +
                '}';
    }
}
