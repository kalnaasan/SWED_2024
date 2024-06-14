package edu.fra.uas.websitemonitor.observer;

import edu.fra.uas.websitemonitor.model.Version;

public interface Observer {
    void update(Version version);
}
