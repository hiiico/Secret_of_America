package softuni.exam.models.dto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@XmlRootElement(name = "visitors")
@XmlAccessorType(XmlAccessType.FIELD)
public class VisitorsSeedDto {

    @XmlElement(name = "visitor")
    private List<VisitorSeedDto> visitorSeedDtos;

    public VisitorsSeedDto() {
        this.visitorSeedDtos = new ArrayList<>();
    }

    public List<VisitorSeedDto> getVisitorSeedDtos() {
        return visitorSeedDtos;
    }

    public void setVisitorSeedDtos(List<VisitorSeedDto> visitorSeedDtos) {
        this.visitorSeedDtos = visitorSeedDtos;
    }
}
