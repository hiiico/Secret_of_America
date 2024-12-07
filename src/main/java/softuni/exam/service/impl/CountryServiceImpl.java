package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.CountrySeedDto;
import softuni.exam.models.entity.Country;
import softuni.exam.repository.CountryRepository;
import softuni.exam.service.CountryService;
import softuni.exam.util.ValidationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

//ToDo - Implement all the methods
@Service
public class CountryServiceImpl implements CountryService {

    private final static String JSON_IMPORT_PATH = "src/main/resources/files/json/countries.json";
    private final CountryRepository countryRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final Gson gson;

    @Autowired
    public CountryServiceImpl(CountryRepository countryRepository, ModelMapper modelMapper, ValidationUtil validationUtil, Gson gson) {
        this.countryRepository = countryRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.gson = gson;
    }


    @Override
    public boolean areImported() {
        return this.countryRepository.count() > 0;
    }

    @Override
    public String readCountryFileContent() throws IOException {
        return Files.readString(Path.of(JSON_IMPORT_PATH));
    }

    @Override
    public String importCountries() throws IOException {
        StringBuilder sb = new StringBuilder();

        CountrySeedDto[] countrySeedDtos = this.gson.fromJson(readCountryFileContent(), CountrySeedDto[].class);

        for (CountrySeedDto countrySeedDto : countrySeedDtos) {
            if(this.countryRepository.findByName(countrySeedDto.getName()).isPresent() ||
            !this.validationUtil.isValid(countrySeedDto)) {

                sb.append("Invalid country")
                        .append(System.lineSeparator());
                continue;
            }

            this.countryRepository.save(this.modelMapper.map(countrySeedDto, Country.class));
            sb.append(String.format("Successfully imported country %s",
                    countrySeedDto.getName()))
                    .append(System.lineSeparator());
        }
        return sb.toString().trim();
    }

    @Override
    public Country getCountry(Long id) {
        return this.countryRepository.findById(id).orElse(null);
    }

}
