package com.b2c2.automation.hooks;

import io.cucumber.java.After;
import io.cucumber.java.AfterStep;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.time.Duration;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WebDriverHooks {

    private static final ThreadLocal<WebDriver> DRIVER_THREAD_LOCAL = new ThreadLocal<>();
    private static final long SLOW_MS = Long.parseLong(System.getProperty("slow", "0"));
    private static final String BROWSER = System.getProperty("browser", "chrome").toLowerCase();

    static {
        Logger.getLogger("org.openqa.selenium").setLevel(Level.SEVERE);
    }

    public static WebDriver getDriver() {
        return DRIVER_THREAD_LOCAL.get();
    }

    @Before
    public void setUp() {
        boolean headless = Boolean.parseBoolean(System.getProperty("headless", "true"));
        WebDriver driver = switch (BROWSER) {
            case "edge" -> createEdgeDriver(headless);
            case "firefox" -> createFirefoxDriver(headless);
            default -> createChromeDriverWithFallback(headless);
        };
        driver.manage().window().setSize(new Dimension(1920, 1080));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
        DRIVER_THREAD_LOCAL.set(driver);
    }

    private WebDriver createChromeDriverWithFallback(boolean headless) {
        try {
            return createChromeDriver(headless);
        } catch (Exception e) {
            return createEdgeDriver(headless);
        }
    }

    private WebDriver createChromeDriver(boolean headless) {
        ChromeOptions options = new ChromeOptions();
        if (headless) {
            options.addArguments("--headless=new");
        }
        options.addArguments("--no-sandbox", "--disable-dev-shm-usage", "--window-size=1920,1080");
        return new ChromeDriver(options);
    }

    private WebDriver createEdgeDriver(boolean headless) {
        EdgeOptions options = new EdgeOptions();
        if (headless) {
            options.addArguments("--headless=new");
        }
        options.addArguments("--no-sandbox", "--disable-dev-shm-usage", "--window-size=1920,1080");
        return new EdgeDriver(options);
    }

    private WebDriver createFirefoxDriver(boolean headless) {
        FirefoxOptions options = new FirefoxOptions();
        if (headless) {
            options.addArguments("-headless");
        }
        options.addArguments("--width=1920", "--height=1080");
        return new FirefoxDriver(options);
    }

    @AfterStep
    public void pauseAfterStep() {
        if (SLOW_MS > 0) {
            try { Thread.sleep(SLOW_MS); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        }
    }

    @After(order = 1)
    public void captureScreenshotOnFailure(Scenario scenario) {
        WebDriver driver = DRIVER_THREAD_LOCAL.get();
        if (scenario.isFailed() && driver instanceof TakesScreenshot screenshotter) {
            byte[] screenshot = screenshotter.getScreenshotAs(OutputType.BYTES);
            scenario.attach(screenshot, "image/png", "failure-screenshot");
        }
    }

    @After(order = 0)
    public void tearDown() {
        WebDriver driver = DRIVER_THREAD_LOCAL.get();
        if (driver != null) {
            driver.quit();
            DRIVER_THREAD_LOCAL.remove();
        }
    }
}
