package softuni.exam.models.dto;

import com.google.gson.annotations.Expose;
import jakarta.validation.constraints.DecimalMin;
import org.hibernate.validator.constraints.Length;

public class CountrySeedDto {

    @Expose
    @Length(min = 3, max = 40)
    private String  name;

    @Expose
    @DecimalMin(value = "0")
    private double area;

    public CountrySeedDto() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getArea() {
        return area;
    }
    public void setArea(double area) {
        this.area = area;
    }
}
