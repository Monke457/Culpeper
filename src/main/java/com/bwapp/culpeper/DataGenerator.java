package com.bwapp.culpeper;

import com.bwapp.culpeper.model.Plant;
import com.bwapp.culpeper.service.PlantRepository;
import com.vaadin.flow.spring.annotation.SpringComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;

@SpringComponent
public class DataGenerator {

    @Bean
    public CommandLineRunner loadData(PlantRepository repository) {
        return(args) -> {
            /*Logger logger = LoggerFactory.getLogger(getClass());
            Plant plant1 = new Plant();
            plant1.setLatinName("Agrimonia eupatoria");
            plant1.setCommonName("Agrimony");
            plant1.setDescription("Dark green with numerous soft hairs. The flower spikes have a spicy odor like apricots");

            repository.save(plant1);

             */
        };
    }
}
