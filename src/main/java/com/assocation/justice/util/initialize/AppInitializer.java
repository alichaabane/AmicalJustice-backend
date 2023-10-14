package com.assocation.justice.util.initialize;

import com.assocation.justice.dto.RegionResponsableDTO2;
import com.assocation.justice.dto.ResponsableDTO;
import com.assocation.justice.service.RegionResponsableService;
import com.assocation.justice.service.ResponsableService;
import com.assocation.justice.service.impl.ImageServiceImpl;
import com.assocation.justice.util.enumeration.CategoryResponsable;
import com.assocation.justice.util.enumeration.Region;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class AppInitializer implements CommandLineRunner {

    private final Logger logger = LoggerFactory.getLogger(ImageServiceImpl.class);

    private final ResponsableService responsableService;
    private final RegionResponsableService regionResponsableService;

    @Override
    public void run(String... args) {
        RegionResponsableDTO2 regionResponsableDTO2 = null;
        if (regionResponsableService.getAllRegionResponsables().isEmpty()) {
            logger.info("No Region Responsable found. creating some region responsables");
            regionResponsableDTO2 = new RegionResponsableDTO2(null, "قفصة", "34.4208066" , "8.7731791", Region.قفصة, "76200400");
        }
        if (responsableService.getAllResponsables().isEmpty()) {
        logger.info("No Responsable found. creating some responsables");
        ResponsableDTO responsableDTO = new ResponsableDTO(null, "رمزي بالسعدي" , "98531554","ramzibessadi@yahoo.com",
        "محكمة الإبتدائية بقفصة",CategoryResponsable.رئيس_فرع,  Region.قفصة, regionResponsableDTO2.getNom());
        responsableService.createResponsable(responsableDTO);
        logger.info("Responsable initialized and created successfully");
    }
    }
}
