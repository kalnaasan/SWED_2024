package edu.fra.uas.websitemonitor.strategy;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class Context {
    private final IStrategy htmlComparisonStrategy;
    private final IStrategy sizeComparisonStrategy;
    private final IStrategy textComparisonStrategy;

    public Context(@Qualifier("HtmlComparisonStrategy") IStrategy htmlComparisonStrategy,
                   @Qualifier("SizeComparisonStrategy") IStrategy sizeComparisonStrategy,
                   @Qualifier("TextComparisonStrategy") IStrategy textComparisonStrategy) {
        this.htmlComparisonStrategy = htmlComparisonStrategy;
        this.sizeComparisonStrategy = sizeComparisonStrategy;
        this.textComparisonStrategy = textComparisonStrategy;
    }

    public IStrategy getStrategy(EStrategy EStrategy) {
        if (EStrategy.equals(edu.fra.uas.websitemonitor.strategy.EStrategy.HTML)) return this.htmlComparisonStrategy;
        else if (EStrategy.equals(edu.fra.uas.websitemonitor.strategy.EStrategy.SIZE)) return this.sizeComparisonStrategy;
        return this.textComparisonStrategy;
    }
}
