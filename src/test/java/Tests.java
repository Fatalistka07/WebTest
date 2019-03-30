import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class Tests extends BaseRunner {

    @Test
    public void test1(){
        driver.get("https://www.tinkoff.ru/mobile-operator/tariffs/");
        driver.findElement(By.xpath("//input[@name='fio']")).click();
        driver.findElement(By.xpath("//input[@name='phone_mobile']")).click();
        driver.findElement(By.xpath("//input[@name='email']")).click();
        driver.findElement(By.xpath("//div[@class='Checkbox__icon_2Lt_j']")).click();

        // через Actions почему-то работает быстрее
        Actions actions = new Actions(driver);
        actions.moveToElement(driver.findElement(By.xpath("//button[.//text()='Оставить заявку']"))).click();
        actions.perform();

        assertEquals("Укажите ваше ФИО", driver.findElement(By.xpath("//div[@class='ui-form__row ui-form__row_dropdownFIO app-form-step__fio-field ui-form__row_default-error-view-visible']//div[@class='ui-form-field-error-message ui-form-field-error-message_ui-form']")).getText());
        assertEquals("Необходимо указать номер телефона", driver.findElement(By.xpath("//div[@class='ui-form__row ui-form__row_tel ui-form__row_default-error-view-visible']//div[@class='ui-form-field-error-message ui-form-field-error-message_ui-form']")).getText());
        assertEquals("Для продолжения нужно согласие с условиями", driver.findElement(By.xpath("//div[@class='ui-form__row ui-form__row_checkbox styles__agreementCheckboxTitle_2NQKP ui-form__row_default-error-view-visible']//div[@class='ui-form-field-error-message ui-form-field-error-message_ui-form']")).getText());
    }

    @Test
    public void test2(){
        driver.get("https://www.tinkoff.ru/mobile-operator/tariffs/");

        WebElement fio = driver.findElement(By.cssSelector("input[name='fio']"));
        fio.click();
        fio.clear();
        fio.sendKeys("Иванн");

        WebElement phone = driver.findElement(By.cssSelector("input[name='phone_mobile']"));
        phone.click();
        phone.clear();
        phone.sendKeys("+7(932) 400-");

        WebElement email = driver.findElement(By.cssSelector("input[name='email']"));
        email.click();
        email.clear();
        email.sendKeys("---");

        driver.findElement(By.cssSelector("div.Checkbox__icon_2Lt_j")).click();

        driver.findElement(By.cssSelector("div.BlockingButton__blockingButton_N-UUk button")).click();

        assertEquals("Недостаточно информации. Введите фамилию, имя и отчество через пробел (Например: Иванов Иван Алексеевич)",
                driver.findElement(By.cssSelector("div.ui-form__row_dropdownFIO div div.ui-form-field-error-message")).getText());

        assertEquals("Номер телефона должен состоять из 10 цифр, начиная с кода оператора",
                driver.findElement(By.cssSelector("div.ui-form__row_tel div div.ui-form-field-error-message")).getText());

        assertEquals("Введите корректный адрес эл. почты",
                driver.findElement(By.cssSelector("div.ui-form__row_dropdownSuggest div div.ui-form-field-error-message")).getText());

        assertEquals("Для продолжения нужно согласие с условиями",
                driver.findElement(By.cssSelector("div.styles__agreementCheckboxTitle_2NQKP div.ui-form-field-error-message")).getText());
    }


    @Test
    public void test21_tabs() {
        driver.get("https://www.google.ru/");

        WebElement search = driver.findElement(By.xpath("//input[@class='gLFyf gsfi']"));
        search.sendKeys("мобайл тинькофф");
        driver.findElement(By.xpath("//div[@class='suggestions-inner-container']/descendant::b[contains(text(),'тарифы')]")).click();

        String googleWinHandle = driver.getWindowHandle();
        driver.findElement(By.xpath("//a[@href='https://www.tinkoff.ru/mobile-operator/tariffs/']")).click();

        ArrayList<String> tabs;
        tabs = new ArrayList<>(driver.getWindowHandles());
        driver.switchTo().window(tabs.get(tabs.size() - 1));

        assertEquals("https://www.tinkoff.ru/mobile-operator/tariffs/", driver.getCurrentUrl());

        driver.switchTo().window(googleWinHandle).close();
        tabs = new ArrayList<>(driver.getWindowHandles());
        driver.switchTo().window(tabs.get(tabs.size() - 1));

        assertEquals("https://www.tinkoff.ru/mobile-operator/tariffs/", driver.getCurrentUrl());
    }


    @Test
    public void test22_geoMsk() {
        TinkoffSimFormTester simForm = new TinkoffSimFormTester(driver);
        simForm.changeRegion("Москва и Московская обл.");

        assertEquals("Москва и Московская область",
                driver.findElement(By.cssSelector("div.MvnoRegionConfirmation__title_DOqnW")).getText());

        driver.navigate().refresh();

        assertEquals("Москва и Московская область",
                driver.findElement(By.cssSelector("div.MvnoRegionConfirmation__title_DOqnW")).getText());
        String mskPrice = simForm.getPrice();
        simForm.changeRegion("Краснодарский кр.");
        String krdPrice = simForm.getPrice();
        assertNotEquals(mskPrice, krdPrice);
    }


    @Test
    public void test23_activeDelivery() {
        TinkoffSimFormTester simForm = new TinkoffSimFormTester(driver);
        simForm.changeRegion("Москва и Московская обл.");

        TinkoffSimFormTester.Button requestSimButton = simForm.getSimRequestBT();
        assertFalse(requestSimButton.isEnabled());

        simForm.getFioTI().fillText("Иванов Иван Иванович");
        simForm.getPhoneMobileTI().fillText("9876543210");
        simForm.getEmailTI().fillText("abc@mail.ru");
        simForm.getMessengersCB().setDeactivate();
        simForm.getSocialNetsCB().setDeactivate();
        assertTrue(requestSimButton.isEnabled());
        requestSimButton.click();

        assertTrue(simForm.getDeliverRequestBT().isEnabled());

    }


}

