package com.work.olexii.after_dark.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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
    private String race;
    @ManyToOne
    @JoinColumn(name = "user_id")
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

    public String getRace() {
        return race;
    }

    public void setRace(String race) {
        this.race = race;
    }

    public void setClassEnByInt(int classInt) {
        switch (classInt) {
            case 1:
                this.classEn = ClassEn.Warrior;
                break;
            case 2:
                this.classEn = ClassEn.Paladin;
                break;
            case 3:
                this.classEn = ClassEn.Hunter;
                break;
            case 4:
                this.classEn = ClassEn.Rogue;
                break;
            case 5:
                this.classEn = ClassEn.Priest;
                break;
            case 6:
                this.classEn = ClassEn.DeathKnight;
                break;
            case 7:
                this.classEn = ClassEn.Shaman;
                break;
            case 8:
                this.classEn = ClassEn.Mage;
                break;
            case 9:
                this.classEn = ClassEn.Warlock;
                break;
            case 10:
                this.classEn = ClassEn.Monk;
                break;
            case 11:
                this.classEn = ClassEn.Druid;
                break;
            case 12:
                this.classEn = ClassEn.DemonHunter;
                break;
        }
    }

    public void setClassRuByInt(int classInt) {
        switch (classInt) {
            case 1:
                this.classRu = ClassRu.Воин;
                break;
            case 2:
                this.classRu = ClassRu.Паладин;
                break;
            case 3:
                this.classRu = ClassRu.Охотник;
                break;
            case 4:
                this.classRu = ClassRu.Разбойник;
                break;
            case 5:
                this.classRu = ClassRu.Жрец;
                break;
            case 6:
                this.classRu = ClassRu.Рыцарь_смерти;
                break;
            case 7:
                this.classRu = ClassRu.Шаман;
                break;
            case 8:
                this.classRu = ClassRu.Маг;
                break;
            case 9:
                this.classRu = ClassRu.Чернокнижник;
                break;
            case 10:
                this.classRu = ClassRu.Монах;
                break;
            case 11:
                this.classRu = ClassRu.Друид;
                break;
            case 12:
                this.classRu = ClassRu.Охотник_на_демонов;
                break;
        }
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

    public void setRankByInt(int parseInt) {
        switch (parseInt) {
            case 0:
                this.rank = Rank.Гильд_Мастер;
                break;
            case 1:
                this.rank = Rank.Зам;
                break;

            case 2:
                this.rank = Rank.Хранитель;
                break;
            case 3:
                this.rank = Rank.Офицер;
                break;
            case 4:
                this.rank = Rank.Рейдер;
                break;
            case 5:
                this.rank = Rank.Ветеран;
                break;
            case 6:
                this.rank = Rank.Мастер;
                break;
            case 7:
                this.rank = Rank.Защитник;
                break;
            case 8:
                this.rank = Rank.Игрок;
                break;
            case 9:
                this.rank = Rank.Рекрут;
                break;
        }

    }
}
