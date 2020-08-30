package com.work.olexii.after_dark.domain;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "private_chat")
public class FaceToFaceChatEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @ManyToOne
    private User user1;
    @ManyToOne
    private User user2;



    public FaceToFaceChatEntity() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getUser1() {
        return user1;
    }

    public void setUser1(User user1) {
        this.user1 = user1;
    }

    public User getUser2() {
        return user2;
    }

    public void setUser2(User user2) {
        this.user2 = user2;
    }



    @Override
    public String toString() {
        return "FaceToFaceChatEntity{" +
                "id=" + id +
                ", user1=" + user1 +
                ", user2=" + user2 +
                '}';
    }
}
