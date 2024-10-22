package stirling.software.SPDF.EE;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import stirling.software.SPDF.model.ApplicationProperties;

@Configuration
@Lazy
public class EEAppConfig {

    private static final Logger logger = LoggerFactory.getLogger(EEAppConfig.class);

    @Autowired ApplicationProperties applicationProperties;

    @Autowired private LicenseKeyChecker licenseKeyChecker;

    @Bean(name = "runningEE")
    public boolean runningEnterpriseEdition() {
        return licenseKeyChecker.getEnterpriseEnabledResult();
    }
}
