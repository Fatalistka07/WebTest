import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class Tests extends BaseRunner {

    @Test
    public void test1_emptyData() {
        TinkoffSimFormTester simForm = new TinkoffSimFormTester(driver);
        logger.debug("click on FIO input");
        simForm.getFioTI().click();
        logger.debug("click on mobile phone input");
        simForm.getPhoneMobileTI().click();
        logger.debug("click on mobile e-mail input");
        simForm.getEmailTI().click();
        logger.debug("click on agreement");
        simForm.getAgreementCB().setDeactivate();

        logger.debug("click on submit button");
        simForm.getSubmitBT().click();

        assertEquals("Укажите ваше ФИО", simForm.getFioTI().getErrorText());
        assertEquals("Необходимо указать номер телефона", simForm.getPhoneMobileTI().getErrorText());
        assertEquals("Для продолжения нужно согласие с условиями", simForm.getAgreementCB().getErrorText());
    }

    @Test
    public void test21_tabs() {
        logger.debug("Go to google");
        driver.get("https://www.google.ru/");

        logger.debug("Type to search 'мобайл тинькофф'");
        WebElement search = driver.findElement(By.xpath("//input[@class='gLFyf gsfi']"));
        search.sendKeys("мобайл тинькофф");
        logger.debug("Select suggested variant with 'тарифы'");
        driver.findElement(By.xpath("//div[@class='suggestions-inner-container']/descendant::b[contains(text(),'тарифы')]")).click();

        logger.debug("Choose required variant");
        String googleWinHandle = driver.getWindowHandle();
        driver.findElement(By.xpath("//a[@href='https://www.tinkoff.ru/mobile-operator/tariffs/']")).click();

        logger.debug("Switch to opened tab");
        ArrayList<String> tabs;
        tabs = new ArrayList<>(driver.getWindowHandles());
        driver.switchTo().window(tabs.get(tabs.size() - 1));

        assertEquals("https://www.tinkoff.ru/mobile-operator/tariffs/", driver.getCurrentUrl());

        logger.debug("Go back to tab with google and close it");
        driver.switchTo().window(googleWinHandle).close();
        tabs = new ArrayList<>(driver.getWindowHandles());
        driver.switchTo().window(tabs.get(tabs.size() - 1));

        assertEquals("https://www.tinkoff.ru/mobile-operator/tariffs/", driver.getCurrentUrl());
    }

    @Test
    public void test22_geoMsk() {
        TinkoffSimFormTester simForm = new TinkoffSimFormTester(driver);
        logger.debug("Change region on 'Москва и Московская обл.'");
        simForm.changeRegion("Москва и Московская обл.");

        assertEquals("Москва и Московская область",
                driver.findElement(By.cssSelector("div.MvnoRegionConfirmation__title_DOqnW")).getText());

        logger.debug("Refresh page");
        driver.navigate().refresh();

        assertEquals("Москва и Московская область",
                driver.findElement(By.cssSelector("div.MvnoRegionConfirmation__title_DOqnW")).getText());
        String mskPrice = simForm.getPrice();
        logger.debug("price for 'Москва и Московская обл.' = " + mskPrice);

        logger.debug("Change region on 'Краснодарский кр.'");
        simForm.changeRegion("Краснодарский кр.");
        String krdPrice = simForm.getPrice();
        logger.debug("price for 'Краснодарский кр.' = " + krdPrice);
        assertNotEquals(mskPrice, krdPrice);
    }

    @Test
    public void test23_activeDelivery() {
        TinkoffSimFormTester simForm = new TinkoffSimFormTester(driver);
        logger.debug("Change region on 'Москва и Московская обл.'");
        simForm.changeRegion("Москва и Московская обл.");

        TinkoffSimFormTester.Button requestSimButton = simForm.getSimRequestBT();
        assertFalse(requestSimButton.isEnabled());

        logger.debug("Fill correct FIO");
        simForm.getFioTI().fillText("Иванов Иван Иванович");
        logger.debug("Fill correct mobile phone");
        simForm.getPhoneMobileTI().fillText("9876543210");
        logger.debug("Fill correct e-mail");
        simForm.getEmailTI().fillText("abc@mail.ru");
        logger.debug("uncheck messangers");
        simForm.getMessengersCB().setDeactivate();
        logger.debug("uncheck social networks");
        simForm.getSocialNetsCB().setDeactivate();
        assertTrue(requestSimButton.isEnabled());

        logger.debug("click on submit button");
        requestSimButton.click();

        assertTrue(simForm.getDeliverRequestBT().isEnabled());
    }


}

