package softuni.exam.service.impl;

import jakarta.xml.bind.JAXBException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.VisitorSeedDto;
import softuni.exam.models.dto.VisitorsSeedDto;
import softuni.exam.models.entity.Attraction;
import softuni.exam.models.entity.Country;
import softuni.exam.models.entity.PersonalData;
import softuni.exam.models.entity.Visitor;
import softuni.exam.repository.VisitorRepository;
import softuni.exam.service.AttractionService;
import softuni.exam.service.CountryService;
import softuni.exam.service.PersonalDataService;
import softuni.exam.service.VisitorService;
import softuni.exam.util.ValidationUtil;
import softuni.exam.util.XmlParser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class VisitorServiceImpl implements VisitorService {

    private final static String XML_IMPORT_PATH = "src/main/resources/files/xml/visitors.xml";
    private final VisitorRepository visitorRepository;
    private final AttractionService attractionService;
    private final CountryService countryService;
    private final PersonalDataService personalDataService;

    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final XmlParser xmlParser;

    @Autowired
    public VisitorServiceImpl(VisitorRepository visitorRepository, AttractionService attractionService, CountryService countryService, PersonalDataService personalDataService, ModelMapper modelMapper, ValidationUtil validationUtil, XmlParser xmlParser) {
        this.visitorRepository = visitorRepository;
        this.attractionService = attractionService;
        this.countryService = countryService;
        this.personalDataService = personalDataService;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.xmlParser = xmlParser;
    }


    @Override
    public boolean areImported() {
        return this.visitorRepository.count() > 0;
    }

    @Override
    public String readVisitorsFileContent() throws IOException {
        return Files.readString(Path.of(XML_IMPORT_PATH));
    }

    @Override
    public String importVisitors() throws JAXBException {
        StringBuilder sb = new StringBuilder();
        VisitorsSeedDto visitorsSeedDtos = this.xmlParser.fromFile(XML_IMPORT_PATH, VisitorsSeedDto.class);

        for (VisitorSeedDto visitorSeedDto: visitorsSeedDtos.getVisitorSeedDtos()) {

            Attraction attraction = this.attractionService.getAttraction(visitorSeedDto.getAttractionId());
            Country country = this.countryService.getCountry(visitorSeedDto.getCountryId());
            PersonalData personalData = this.personalDataService.getPersonalData(visitorSeedDto.getPersonalDataId());

            if (this.visitorRepository.findByFirstNameAndLastName(visitorSeedDto.getFirstName(),
                    visitorSeedDto.getLastName()).isPresent() ||
                    this.visitorRepository.findByPersonalDataId(visitorSeedDto.getPersonalDataId()).isPresent() ||
                    personalData == null ||
                    attraction == null ||
                    country == null ||
                    !this.validationUtil.isValid(visitorSeedDto)) {

                sb.append("Invalid visitor")
                        .append(System.lineSeparator());
                continue;
            }

            Visitor visitor = new Visitor();
            visitor.setFirstName(visitorSeedDto.getFirstName());
            visitor.setLastName(visitorSeedDto.getLastName());
            visitor.setAttraction(attraction);
            visitor.setCountry(country);
            visitor.setPersonalData(personalData);

            attraction.getVisitors().add(visitor);
            country.getVisitors().add(visitor);

            this.visitorRepository.save(visitor);
            sb.append(String.format("Successfully imported visitor %s %s",
                            visitorSeedDto.getFirstName(), visitorSeedDto.getLastName()))
                    .append(System.lineSeparator());
        }

        return sb.toString().trim();
    }
}
