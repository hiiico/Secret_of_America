package softuni.exam.models.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "countries")
public class Country extends BaseEntity{

    @Column
    private double area;

    @Column(nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "country",fetch = FetchType.EAGER)
    private Set<Attraction> attractions;

    @OneToMany(mappedBy = "country",fetch = FetchType.EAGER)
    private List<Visitor> visitors;

    public Country() {
        this.attractions = new HashSet<>();
        this.visitors = new ArrayList<>();
    }

    public double getArea() {
        return area;
    }

    public void setArea(double area) {
        this.area = area;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Attraction> getAttractions() {
        return attractions;
    }

    public void setAttractions(Set<Attraction> attractions) {
        this.attractions = attractions;
    }

    public List<Visitor> getVisitors() {
        return visitors;
    }

    public void setVisitors(List<Visitor> visitors) {
        this.visitors = visitors;
    }
}
