package edu.fra.uas.websitemonitor.strategy;

import edu.fra.uas.websitemonitor.model.Version;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;


@Component
@Qualifier("TextComparisonStrategy")
@Slf4j
public class TextComparisonStrategy implements IStrategy {
    @Override
    public boolean execute(Version oldVersion, Version newVersion) {
        log.info("Text Strategy...");
        String oldText = this.removeHTMLTags(oldVersion.getContent());
        String newText = this.removeHTMLTags(newVersion.getContent());

        log.info("Old version: {}", oldText);
        log.info("New version: {}", newText);
        return !oldText.equals(newText);
    }

    private String removeHTMLTags(String content){
        // Parse the HTML content with JSoup
        Document doc = Jsoup.parse(content);

        // Select the body element and get its text content
        Element body = doc.body();
        String bodyText = body.text();
        return bodyText.trim();
    }
}
