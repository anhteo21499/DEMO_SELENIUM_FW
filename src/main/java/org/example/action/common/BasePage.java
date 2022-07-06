package org.example.action.common;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.Color;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.Set;

public class BasePage {

// dùng cho trang page =========================================================

    public void openPageUrl(WebDriver driver, String pageUrl) {
        driver.get(pageUrl);
    }

    public String getTitlePage(WebDriver driver) {
        return driver.getTitle();
    }

    public String getPageSource(WebDriver driver) {
        return driver.getPageSource();
    }

    public String getPageUrl(WebDriver driver) {
        return driver.getCurrentUrl();
    }

    public void backPage(WebDriver driver) {
        driver.navigate().back();
    }

    public void forwardPage(WebDriver driver) {
        driver.navigate().forward();
    }

    public void refreshPage(WebDriver driver) {
        driver.navigate().refresh();
    }

    public void switchFrame(WebDriver driver, WebElement frameEL) {
        driver.switchTo().frame(frameEL);
    }

    public void switchFrameDefault(WebDriver driver) {
        driver.switchTo().defaultContent();
    }

    public Alert waitAlertShow(WebDriver driver, long timeOutInSeconds) {
        WebDriverWait explicitWait = new WebDriverWait(driver, timeOutInSeconds);
        return explicitWait.until(ExpectedConditions.alertIsPresent());
    }

    public void acceptAlert(WebDriver driver, By alertLC, long timeOutInSeconds) {
        waitAlertShow(driver, timeOutInSeconds).accept();
    }

    public void cancelAlert(WebDriver driver, By alertLC, long timeOutInSeconds) {
        waitAlertShow(driver, timeOutInSeconds).dismiss();
    }

    public String getTextAlert(WebDriver driver, long timeOutInSeconds) {
        return waitAlertShow(driver, timeOutInSeconds).getText();
    }

    public void sendKeyToAlert(WebDriver driver, String key, long timeOutInSeconds) {
        waitAlertShow(driver, timeOutInSeconds).sendKeys(key);
    }

    /**
     * chyển trang với trường hợp có 2 trang , muốn chuyển trang khác thì kiểm tra
     * khác id trang hiện tại thì switch qua trang còn lại
     *
     * @param driver
     * @param currentWindowId
     */
    public void switchWindowById(WebDriver driver, String currentWindowId) {
        Set<String> windowIDs = driver.getWindowHandles();
        for (String id : windowIDs) {
            if (!id.equals(currentWindowId)) {
                driver.switchTo().window(id);
                break;
            }
        }
    }

    public void switchWindowByTitle(WebDriver driver, String title) {
        Set<String> windowIDs = driver.getWindowHandles();
        for (String id : windowIDs) {
            driver.switchTo().window(id);
            if (driver.getTitle().equals(title)) {
                break;
            }
        }
    }

    public void closeAllTabWithoutParentPage(WebDriver driver, String parentId) {
        Set<String> windowIDs = driver.getWindowHandles();
        for (String id : windowIDs) {
            if (!id.equals(parentId)) {
                driver.switchTo().window(id).close();
            }
        }
        driver.switchTo().window(parentId);
    }


//    thao tac voi element ====================================================================


    public void clickToElement(WebDriver driver, String xpathLocator) {
        getWebElement(driver, xpathLocator).click();
    }

    public void sendKeyElement(WebDriver driver, String xpathLocator, String key) {
        WebElement element = getWebElement(driver, xpathLocator);
        element.clear();
        element.sendKeys(key);
    }


    public String getTextElement(WebDriver driver, String xpathLocator) {
        return getWebElement(driver, xpathLocator).getText();
    }

    public String getCssValue(WebDriver driver, String xpathLocator, String propertyName) {
        return getWebElement(driver, xpathLocator).getCssValue(propertyName);
    }

    /**
     * chuyển kiều màu dạng RGBA về dạng hex
     * vd : rgba(123,454,2343) -> #A2344
     *
     * @param rgbaColor
     * @return
     */
    public String getHexColorFromRGBA(String rgbaColor) {
        return Color.fromString(rgbaColor).asHex();
    }

//   dùng cho dropdown ==========================================================

    /**
     * chọn giá trị dropdown với tham số truyền vào
     *
     * @param driver
     * @param xpathLocator
     * @param itemValue
     */
    public void selectItemDropDown(WebDriver driver, String xpathLocator, String itemValue) {
        Select select = new Select(getWebElement(driver, xpathLocator));
        select.selectByValue(itemValue);
    }

    /**
     * lấy giá trị đầu tiên trong dropdown được chọn
     *
     * @param driver
     * @param xpathLocator
     * @return
     */
    public String getValueItem(WebDriver driver, String xpathLocator) {
        Select select = new Select(getWebElement(driver, xpathLocator));
        return select.getFirstSelectedOption().getText();
    }

    /**
     * chọn giá trị value bất kì với tham số mong doi expected
     * B1 : click element dropdown -> chờ để hiện tất cá các item bên trong
     * B2 : lấy giá trị text so sánh với giá trị mong muốn chọn
     * B3 : click giá trị để chọn element
     *
     * @param driver
     * @param xpathParent
     * @param xpathChild
     * @param expectedText
     * @param timeOutInSeconds
     */
    public void selectItemInDropDown(WebDriver driver, String xpathParent, String xpathChild, String expectedText, long timeOutInSeconds) {
        getWebElement(driver, xpathParent).click();
        sleep(1);

        WebDriverWait wait = new WebDriverWait(driver, timeOutInSeconds);
        List<WebElement> elements = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(xpathChild)));
        for (WebElement element : elements) {
            if (element.getText().trim().equals(expectedText)) {
                JavascriptExecutor js = (JavascriptExecutor) driver;
                js.executeScript("arguments[0].scrollIntoView(true);", element);
                element.click();
                break;
            }
        }
    }

    /**
     * kiểm tra xem dropdown có chọn được nhiều giá trị hay không
     *
     * @param driver
     * @param xpathLocator
     * @return
     */
    public boolean isMultipleDropDown(WebDriver driver, String xpathLocator) {
        Select select = new Select(getWebElement(driver, xpathLocator));
        return select.isMultiple();
    }
    
//    dùng class Action thao tác với element
    
    public void hoverToElement(WebDriver driver, String xpathLocator) {
        Actions actions = new Actions(driver);
        actions.moveToElement(getWebElement(driver, xpathLocator)).perform();
    }

    /**
     * lấy element theo xpath
     *
     * @param driver
     * @param xpathLocator
     * @return
     */
    public WebElement getWebElement(WebDriver driver, String xpathLocator) {
        return driver.findElement(By.xpath(xpathLocator));
    }

    public void sleep(long time) {
        try {
            Thread.sleep(time * 1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
