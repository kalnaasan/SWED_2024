package edu.fra.uas.websitemonitor.strategy;

import edu.fra.uas.websitemonitor.model.Version;
import org.springframework.stereotype.Component;

@Component
public interface IStrategy {
    boolean execute(Version oldVersion, Version newVersion);
}
