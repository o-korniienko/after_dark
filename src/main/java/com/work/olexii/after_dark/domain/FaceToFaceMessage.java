package com.work.olexii.after_dark.domain;


import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class FaceToFaceMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String text;
    @ManyToOne
    private User toUser;
    @ManyToOne
    private User fromUser;
    private boolean isRead;
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

    public User getFromUser() {
        return fromUser;
    }

    public void setFromUser(User fromUser) {
        this.fromUser = fromUser;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
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


    public User getToUser() {
        return toUser;
    }

    public void setToUser(User toUser) {
        this.toUser = toUser;
    }

    @Override
    public String toString() {
        return "FaceToFaceMessage{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", toUser=" + toUser +
                ", fromUser=" + fromUser +
                ", isRead=" + isRead +
                ", createTime=" + createTime +
                ", epochSecond=" + epochSecond +
                '}';
    }
}
