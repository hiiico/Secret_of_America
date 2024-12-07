package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.AttractionSeedDto;
import softuni.exam.models.entity.Attraction;
import softuni.exam.models.entity.Country;
import softuni.exam.repository.AttractionRepository;
import softuni.exam.service.AttractionService;
import softuni.exam.service.CountryService;
import softuni.exam.util.ValidationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

//ToDo - Implement all the methods
@Service
public class AttractionServiceImpl implements AttractionService {

    private final static String JSON_IMPORT_PATH = "src/main/resources/files/json/attractions.json";
    private final AttractionRepository attractionRepository;
    private final CountryService countryService;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final Gson gson;

    @Autowired
    public AttractionServiceImpl(AttractionRepository attractionRepository, CountryService countryService, ModelMapper modelMapper, ValidationUtil validationUtil, Gson gson) {
        this.attractionRepository = attractionRepository;
        this.countryService = countryService;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.gson = gson;
    }


    @Override
    public boolean areImported() {
        return this.attractionRepository.count() > 0;
    }

    @Override
    public String readAttractionsFileContent() throws IOException {
        return Files.readString(Path.of(JSON_IMPORT_PATH));
    }

    @Override
    public String importAttractions() throws IOException {
        StringBuilder sb = new StringBuilder();
        AttractionSeedDto[] attractionSeedDtos = this.gson.fromJson(readAttractionsFileContent(), AttractionSeedDto[].class);

        for (AttractionSeedDto attractionSeedDto : attractionSeedDtos) {
            if(this.attractionRepository.findByName(attractionSeedDto.getName()).isPresent() ||
            !this.validationUtil.isValid(attractionSeedDto)) {
                sb.append("Invalid attraction")
                        .append(System.lineSeparator());
                continue;
            }
            Attraction attraction= this.modelMapper.map(attractionSeedDto, Attraction.class);
            Country country = this.countryService.getCountry(attractionSeedDto.getCountry());

            attraction.setCountry(country);
            country.getAttractions().add(attraction);

            this.attractionRepository.save(attraction);
            sb.append(String.format("Successfully imported attraction %s", attractionSeedDto.getName()))
                    .append(System.lineSeparator());
        }
        return sb.toString().trim();
    }

    @Override
    public String exportAttractions() {
        StringBuilder sb = new StringBuilder();

       this.attractionRepository.findByTypeOrTypeAndElevationGreaterThanEqualOrderByNameAscCountry("historical site",
                "archaeological site", 300 )
                .forEach(attraction -> sb.append(String.format(
                          "Attraction with ID%d:\n***%s - %s at an altitude of %dm. somewhere in %s.",
                        attraction.getId(), attraction.getName(), attraction.getDescription(),
                        attraction.getElevation(), attraction.getCountry().getName()
                        ))
                        .append(System.lineSeparator()));

        return sb.toString().trim();
    }

    @Override
    public Attraction getAttraction(Long attractionId) {
        return this.attractionRepository.findById(attractionId).orElse(null);
    }

}
