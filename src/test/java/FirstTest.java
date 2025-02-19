import io.appium.java_client.AppiumDriver;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.URL;
import java.time.Duration;

public class FirstTest {

    private AppiumDriver driver;

    @BeforeEach
    public void setUp() throws Exception {

        DesiredCapabilities capabilities = new DesiredCapabilities();

        capabilities.setCapability("appium:platformName", "Android");
        capabilities.setCapability("appium:deviceName", "emulator-5554");
        capabilities.setCapability("appium:automationName", "uiautomator2");
        capabilities.setCapability("appium:appPackage", "org.wikipedia");
        capabilities.setCapability("appium:appActivity", ".main.MainActivity");
        capabilities.setCapability("appium:app", "/Users/omartynenko/Desktop/JavaAppiumAutomation/JavaAppiumAutomation/apks/org.wikipedia.apk");

        driver = new AppiumDriver(new URL("http://127.0.0.1:4723"), capabilities);
    }

    @AfterEach
    public void tearDown() {
        driver.quit();
    }

    @Test
    public void firstTest() {
        waitForElementAndClick(
                By.xpath("//*[@resource-id=\"org.wikipedia:id/fragment_onboarding_skip_button\"]"),
                "Cannot find skip button"
        );

        waitForElementAndClick(
                By.xpath("//*[contains(@text, \"Поиск по Википедии\")]"),
                "Cannot find search init Поиск по Википедии"
        );

        waitForElementAndSendKeys(
                By.xpath("//*[@resource-id=\"org.wikipedia:id/search_src_text\"]"),
                "Java",
                "Cannot find search input"
        );

        waitForElementPresent(
                By.xpath("//*[@resource-id=\"org.wikipedia:id/search_results_list\"]//*[@text=\"Java\"]/following-sibling::*[contains(@text, \"язык программирования\")]"),
                "Cannot find search results \"язык программирования\" topic searching by \"Java\"",
                Duration.ofSeconds(15)
        );
    }

    @Test
    public void testCancelSearch() {
        waitForElementAndClick(
                By.id("org.wikipedia:id/fragment_onboarding_skip_button"),
                "Cannot find skip button"
        );

        waitForElementAndClick(
                By.id("org.wikipedia:id/search_container"),
                "Cannot find search init Поиск по Википедии"
        );

        waitForElementAndSendKeys(
                By.xpath("//*[@resource-id=\"org.wikipedia:id/search_src_text\"]"),
                "Java",
                "Cannot find search input"
        );

        waitForElementAndClear(
                By.id("org.wikipedia:id/search_src_text"),
                "Cannot find search field"
        );

        waitForElementNotPresent(
                By.xpath("//*[@resource-id=\"org.wikipedia:id/search_results_list\"]//*[@text=\"Java\"]/following-sibling::*[contains(@text, \"язык программирования\")]"),
                "Search results \"язык программирования\" is still present",
                Duration.ofSeconds(15)
        );

        // При открытии поиска нет кнопки крестик (она появляется только при вводе текста)
        waitForElementAndClick(
                By.xpath("//*[@content-desc=\"Перейти вверх\"]"),
                "Cannot find back button"
        );

        waitForElementNotPresent(
                By.xpath("//*[@content-desc=\"Перейти вверх\"]"),
                "Back button is still present of the page",
                Duration.ofSeconds(5)
        );
    }

    @Test
    public void testCompareArticleTitle() {
        waitForElementAndClick(
                By.id("org.wikipedia:id/fragment_onboarding_skip_button"),
                "Cannot find skip button"
        );

        waitForElementAndClick(
                By.id("org.wikipedia:id/search_container"),
                "Cannot find search init Поиск по Википедии"
        );

        waitForElementAndSendKeys(
                By.xpath("//*[@resource-id=\"org.wikipedia:id/search_src_text\"]"),
                "Java",
                "Cannot find search input"
        );

        waitForElementAndClick(
                By.xpath("//*[@resource-id=\"org.wikipedia:id/search_results_list\"]//*[@text=\"Java\"]/following-sibling::*[contains(@text, \"язык программирования\")]"),
                "Cannot find search results \"язык программирования\" topic searching by \"Java\"",
                Duration.ofSeconds(15)
        );

        // Статья отображается как WebView и я не смог найти быстрого решения как переключиться - где-то есть дефект
        // поэтому тест нажимает на "Поиск по статье" и ищет уникальный текст про Java
        waitForElementAndClick(
                By.id("org.wikipedia:id/page_find_in_article"),
                "Cannot find menu Find in article"
        );

        waitForElementAndSendKeys(
                By.id("org.wikipedia:id/search_src_text"),
                "разработанный компанией Sun Microsystems",
                "Cannot find search input"
        );

        waitForElementPresent(
                By.xpath("//*[@resource-id=\"org.wikipedia:id/find_in_page_match\"][contains(@text, \"1/\")]"),
                "Cannot find substring in article \"разработанный компанией Sun Microsystems\""
        );
    }

    @Test
    public void testSearchFieldAttribute() {
        waitForElementAndClick(
                By.id("org.wikipedia:id/fragment_onboarding_skip_button"),
                "Cannot find skip button"
        );

        waitForElementAndClick(
                By.id("org.wikipedia:id/search_container"),
                "Cannot find search init Поиск по Википедии"
        );

        assertElementHasText(
                By.id("org.wikipedia:id/search_src_text"),
                "Поиск по Википедии",
                "Cannot find search text field",
                Duration.ofSeconds(5)
        );
    }

    private WebElement waitForElementPresent(By by, String errorMessage, Duration timeoutInSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, timeoutInSeconds);
        wait.withMessage(errorMessage + "\n");
        return wait.until(ExpectedConditions.presenceOfElementLocated(by));
    }

    private WebElement waitForElementPresent(By by, String errorMessage) {
        return waitForElementPresent(by, errorMessage, Duration.ofSeconds(5));
    }

    private WebElement waitForElementAndClick(By by, String errorMessage, Duration timeoutInSeconds) {
        WebElement element = waitForElementPresent(by, errorMessage, timeoutInSeconds);
        element.click();
        return element;
    }

    private WebElement waitForElementAndClick(By by, String errorMessage) {
        return waitForElementAndClick(by, errorMessage, Duration.ofSeconds(5));
    }

    private WebElement waitForElementAndSendKeys(By by, String value, String errorMessage) {
        WebElement element = waitForElementPresent(by, errorMessage);
        element.sendKeys(value);
        return element;
    }

    private boolean waitForElementNotPresent(By by, String errorMessage, Duration timeoutInSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, timeoutInSeconds);
        wait.withMessage(errorMessage + "\n");
        return wait.until(ExpectedConditions.invisibilityOfElementLocated(by));
    }

    private WebElement waitForElementAndClear(By by, String errorMessage, Duration timeoutInSeconds) {
        WebElement element = waitForElementPresent(by, errorMessage, timeoutInSeconds);
        element.clear();
        return element;
    }

    private WebElement waitForElementAndClear(By by, String errorMessage) {
        return waitForElementAndClear(by, errorMessage, Duration.ofSeconds(5));
    }

    private String getAttribute(By by, String errorMessage, Duration timeoutInSeconds) {
        WebElement element = waitForElementPresent(by, errorMessage, timeoutInSeconds);
        return element.getAttribute("text");
    }

    private void assertElementHasText(By by, String expectedText, String errorMessage, Duration timeoutInSeconds) {
        String actualText = getAttribute(by, errorMessage, timeoutInSeconds);

        Assertions.assertEquals(
                expectedText,
                actualText,
                "Expected text " + expectedText + " is not equal to " + actualText
        );
    }
}
