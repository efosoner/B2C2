package com.b2c2.automation.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.ArrayList;
import java.util.List;

public class NewsSliderComponent extends BasePage {

    // News slider
    private static final By NEWS_SLIDER_SLIDES = By.cssSelector(".blog-slider_slide");
    private static final By NEWS_SLIDER_TITLES = By.cssSelector(".hm-slider-title");
    private static final By NEWS_SLIDER_DATES = By.cssSelector(".hm-slide-date");

    public NewsSliderComponent(WebDriver driver) {
        super(driver);
    }

    public int getNewsSliderArticleCount() {
        scrollToElement(NEWS_SLIDER_SLIDES);
        wait.until(ExpectedConditions.presenceOfElementLocated(NEWS_SLIDER_SLIDES));
        return driver.findElements(NEWS_SLIDER_SLIDES).size();
    }

    public List<String> getNewsSliderTitles() {
        wait.until(ExpectedConditions.presenceOfElementLocated(NEWS_SLIDER_TITLES));
        return collectNonEmptyText(driver.findElements(NEWS_SLIDER_TITLES));
    }

    public List<String> getNewsSliderDates() {
        return collectNonEmptyText(driver.findElements(NEWS_SLIDER_DATES));
    }

    private List<String> collectNonEmptyText(List<WebElement> elements) {
        List<String> result = new ArrayList<>();
        for (WebElement el : elements) {
            String text = el.getText().trim();
            if (!text.isEmpty()) {
                result.add(text);
            }
        }
        return result;
    }
}
