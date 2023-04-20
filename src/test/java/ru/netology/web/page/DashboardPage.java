package ru.netology.web.page;

import com.codeborne.selenide.ElementsCollection;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class DashboardPage {

    private ElementsCollection cards = $$(".list__item div");
    private ElementsCollection buttons = $$("[data-test-id=action-deposit]");
    private final String balanceStart = "баланс: ";
    private final String balanceFinish = " р.";

    public DashboardPage() {

    }

    public int getCardBalance(String id) {
        var text = cards.findBy(attribute("data-test-id", id)).getText();
        return extractBalance(text);
    }

    private int extractBalance(String text) {
        var start = text.indexOf(balanceStart);
        var finish = text.indexOf(balanceFinish);
        var value = text.substring(start + balanceStart.length(), finish);
        return Integer.parseInt(value);
    }

    public MoneyTransferPage chooseRechargeCard(String rechargeCardId) {
        $("[data-test-id=" + "\'" + rechargeCardId + "\'" + "] [data-test-id=action-deposit]").click();
        return new MoneyTransferPage();
    }


}
