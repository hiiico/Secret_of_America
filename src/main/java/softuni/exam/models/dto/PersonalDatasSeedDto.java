package softuni.exam.models.dto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

@XmlRootElement(name = "personal_datas")
@XmlAccessorType(XmlAccessType.FIELD)
public class PersonalDatasSeedDto {

    @XmlElement(name = "personal_data")
    private Set<PersonalDataSeedDto> personalDataSeeds;

    public PersonalDatasSeedDto() {
        this.personalDataSeeds = new LinkedHashSet<>();
    }

    public Set<PersonalDataSeedDto> getPersonalDataSeeds() {
        return personalDataSeeds;
    }

    public void setPersonalDataSeeds(Set<PersonalDataSeedDto> personalDataSeeds) {
        this.personalDataSeeds = personalDataSeeds;
    }
}
