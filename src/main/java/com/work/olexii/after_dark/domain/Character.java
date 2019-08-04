package com.work.olexii.after_dark.domain;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Character {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String name;
    private ClassEn classEn;
    private ClassRu classRu;
    private int level;
    private Rank rank;
    @ManyToOne
    @JoinColumn(name = "user_name")
    private User user;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ClassEn getClassEn() {
        return classEn;
    }

    public void setClassEn(ClassEn classEn) {
        this.classEn = classEn;
    }

    public ClassRu getClassRu() {
        return classRu;
    }

    public void setClassRu(ClassRu classRu) {
        this.classRu = classRu;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public Rank getRank() {
        return rank;
    }

    public void setRank(Rank rank) {
        this.rank = rank;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Character character = (Character) o;
        return id == character.id &&
                level == character.level &&
                Objects.equals(name, character.name) &&
                classEn == character.classEn &&
                classRu == character.classRu &&
                rank == character.rank;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, classEn, classRu, level, rank);
    }

    @Override
    public String toString() {
        return "Character{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", classEn=" + classEn +
                ", classRu=" + classRu +
                ", level=" + level +
                ", rank=" + rank +
                ", user=" + user +
                '}';
    }
}
