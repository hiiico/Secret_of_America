package softuni.exam.models.entity;

import jakarta.persistence.*;
import softuni.exam.models.entity.enums.Gender;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "personal_datas")
public class PersonalData extends BaseEntity{

    @Column
    private int age;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "card_number", nullable = false, unique = true)
    private String cardNumber;

    @Column
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @OneToOne(mappedBy = "personalData")
    private Visitor visitors;

    public PersonalData() {
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Visitor getVisitors() {
        return visitors;
    }

    public void setVisitors(Visitor visitors) {
        this.visitors = visitors;
    }
}
