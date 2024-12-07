package softuni.exam.service.impl;

import jakarta.xml.bind.JAXBException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.PersonalDataSeedDto;
import softuni.exam.models.dto.PersonalDatasSeedDto;
import softuni.exam.models.entity.PersonalData;
import softuni.exam.repository.PersonalDataRepository;
import softuni.exam.service.PersonalDataService;
import softuni.exam.util.ValidationUtil;
import softuni.exam.util.XmlParser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

//ToDo - Implement all the methods

@Service
public class PersonalDataServiceImpl implements PersonalDataService {

    private final static String XML_IMPORT_PATH = "src/main/resources/files/xml/personal_data.xml";
    private final PersonalDataRepository personalDataRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final XmlParser xmlParser;

    @Autowired
    public PersonalDataServiceImpl(PersonalDataRepository personalDataRepository, ModelMapper modelMapper, ValidationUtil validationUtil, XmlParser xmlParser) {
        this.personalDataRepository = personalDataRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.xmlParser = xmlParser;
    }


    @Override
    public boolean areImported() {
        return this.personalDataRepository.count() > 0;
    }

    @Override
    public String readPersonalDataFileContent() throws IOException {
        return Files.readString(Path.of(XML_IMPORT_PATH));
    }

    @Override
    public String importPersonalData() throws JAXBException {
        StringBuilder sb = new StringBuilder();
        PersonalDatasSeedDto personalDatasSeedDto = this.xmlParser.fromFile(XML_IMPORT_PATH, PersonalDatasSeedDto.class);

        for (PersonalDataSeedDto personalDataSeedDto: personalDatasSeedDto.getPersonalDataSeeds()) {
            if (this.personalDataRepository.findByCardNumber(personalDataSeedDto.getCardNumber()).isPresent() ||
                    !this.validationUtil.isValid(personalDataSeedDto)) {

                sb.append("Invalid personal data")
                        .append(System.lineSeparator());
                continue;
            }
            this.personalDataRepository.save(this.modelMapper.map(personalDataSeedDto, PersonalData.class));
            sb.append(String.format("Successfully imported personal data for visitor with card number %s",
                            personalDataSeedDto.getCardNumber()))
                    .append(System.lineSeparator());
        }

        return sb.toString().trim();
    }

    @Override
    public PersonalData getPersonalData(Long id) {
        return this.personalDataRepository.findById(id).orElse(null);
    }

}
