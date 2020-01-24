package com.work.olexii.after_dark.domain;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Recipient {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String emailAddress;
    @ElementCollection(targetClass = RecipientTag.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "recipient_tag", joinColumns = @JoinColumn(name = "recipient_id"))
    @Enumerated(EnumType.STRING)
    private Set<RecipientTag> tags;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public Set<RecipientTag> getTags() {
        return tags;
    }

    public void setTags(Set<RecipientTag> tags) {
        this.tags = tags;
    }

    @Override
    public String toString() {
        return "Recipient{" +
                "id=" + id +
                ", emailAddress='" + emailAddress + '\'' +
                ", tags=" + tags +
                '}';
    }
}
