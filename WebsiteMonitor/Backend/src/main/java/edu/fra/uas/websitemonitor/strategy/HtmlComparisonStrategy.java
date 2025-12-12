package edu.fra.uas.websitemonitor.strategy;

import edu.fra.uas.websitemonitor.model.Version;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Qualifier("HtmlComparisonStrategy")
@Slf4j
public class HtmlComparisonStrategy implements IStrategy {
    @Override
    public boolean execute(Version oldVersion, Version newVersion) {
        log.info("Html Strategy...");
        return !oldVersion.getContent().equals(newVersion.getContent());
    }
}
