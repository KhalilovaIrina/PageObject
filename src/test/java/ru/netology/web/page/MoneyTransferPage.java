package ru.netology.web.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;

import java.time.Duration;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class MoneyTransferPage {

    private SelenideElement amountField = $("[data-test-id=amount] input");
    private SelenideElement fromField = $("[data-test-id=from] input");
    private SelenideElement transferButton = $("[data-test-id=action-transfer]");
    private SelenideElement errorMessage = $("[data-test-id=error-notification]");



    public DashboardPage validTransferMoney(int amount, String transferCardNumber) {
        transferMoney(amount, transferCardNumber);
        return new DashboardPage();
    }
    public void transferMoney(int amount, String transferCardNumber) {
        amountField.setValue(Integer.toString(amount));
        fromField.setValue(transferCardNumber);
        transferButton.click();
    }

    public void findErrorMessage() {
        errorMessage.shouldHave(Condition.text("Ошибка"));
        errorMessage.shouldBe(visible);
    }

}
