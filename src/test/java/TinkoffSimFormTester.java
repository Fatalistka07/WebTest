import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

class TinkoffSimFormTester {
    TinkoffSimFormTester(WebDriver driver) {
        this.driver = driver;
        driver.get("https://www.tinkoff.ru/mobile-operator/tariffs/");
    }

    private WebDriver driver;

    class ElementWrapper {
        private String elementXPath;

        ElementWrapper(String xPath) {
            elementXPath = xPath;
        }

        WebElement getElement() {
            return driver.findElement(By.xpath(elementXPath));
        }
    }

    public class CheckBox extends ElementWrapper {

        CheckBox(String checkBoxText) {
            super("//div[@class='CheckboxWithDescription__checkbox_2E0r_' and label[contains(text(),'" + checkBoxText + "')]]");
        }

        boolean isChecked() {
            return getElement().findElement(By.xpath("./descendant::input")).isSelected();
        }

        public String getText() {
            return getElement().findElement(By.xpath("./label")).getText();
        }

        public void setActive() {
            if (!isChecked())
                getElement().findElement(By.xpath("./div")).click();
        }

        void setDeactivate() {
            if (isChecked())
                getElement().findElement(By.xpath("./div")).click();
        }
    }

    public class TextInput extends ElementWrapper {
        TextInput(String inputName) {
            super("//input[@name='" + inputName + "']");
        }

        public String getText() {
            return getElement().getText();
        }

        void fillText(String text) {
            getElement().sendKeys(text);
        }

        public void clear() {
            getElement().clear();
        }
    }

    class Button extends ElementWrapper {
        Button(String buttonText) {
            super("//button[span[descendant::text()='" + buttonText + "']]");
        }

        void click() {
            getElement().click();
        }

        boolean isEnabled() {
            return getElement().isEnabled();
        }
    }

    private boolean firstRegion = true;

    void changeRegion(String regionName) {
        driver.get("https://www.tinkoff.ru/mobile-operator/tariffs/");
        if (firstRegion) {

            driver.findElement(By.xpath("//span[@class='MvnoRegionConfirmation__option_v9PfP MvnoRegionConfirmation__optionRejection_1NrnL']")).click();
            firstRegion = false;
        } else {
            driver.findElement(By.xpath("//div[@class='MvnoRegionConfirmation__title_DOqnW']")).click();
        }
        driver.findElement(By.xpath("//div[text()='" + regionName + "']")).click();

    }

    TextInput getFioTI() {
        return new TextInput("fio");
    }

    TextInput getPhoneMobileTI() {
        return new TextInput("phone_mobile");
    }

    TextInput getEmailTI() {
        return new TextInput("email");
    }

    CheckBox getMessengersCB() {
        return new CheckBox("Мессенджеры");
    }

    CheckBox getSocialNetsCB() {
        return new CheckBox("Социальные сети");
    }

    Button getSimRequestBT() {
        return new Button("Заказать сим-карту");
    }

    Button getDeliverRequestBT() {
        return new Button("Заказать доставку");
    }

    String getPrice() {
        return driver.findElement(By.xpath("//h3[contains(text(),'Общая цена:')]")).getText().substring(11);
    }

}

