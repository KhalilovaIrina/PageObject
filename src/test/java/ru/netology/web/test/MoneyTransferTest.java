package ru.netology.web.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.web.data.DataHelper;
import ru.netology.web.page.DashboardPage;
import ru.netology.web.page.LoginPageV2;

import static com.codeborne.selenide.Selenide.open;
import static ru.netology.web.data.DataHelper.*;

class MoneyTransferTest {
    DashboardPage dashboardPage;
    //LoginPageV2 loginPage;

    @BeforeEach
    void shouldSuccessLogin() {
        open("http://localhost:9999");
        var authInfo = DataHelper.getAuthInfo();
        var loginPage = new LoginPageV2();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        dashboardPage = verificationPage.validVerify(verificationCode);
    }

    @Test
    void shouldTransferFromCard2ToCard1() {
        String rechargeCardId = DataHelper.card1().getId();
        String transferCardNumber = DataHelper.card2().getNumberCard();

        int balanceCard1 = dashboardPage.getCardBalance(card1().getId());
        int balanceCard2 = dashboardPage.getCardBalance(card2().getId());
        int amount = generateValidAmount(balanceCard2);
        dashboardPage
                .chooseRechargeCard(rechargeCardId)
                .validTransferMoney(amount, transferCardNumber);

        Assertions.assertEquals(balanceCard1 + amount, dashboardPage.getCardBalance(card1().getId()));
        Assertions.assertEquals(balanceCard2 - amount, dashboardPage.getCardBalance(card2().getId()));

    }

    @Test
    void shouldTransferFromCard1ToCard2() {
        String rechargeCardId = DataHelper.card2().getId();
        String transferCardNumber = card1().getNumberCard();

        int balanceCard1 = dashboardPage.getCardBalance(card1().getId());
        int balanceCard2 = dashboardPage.getCardBalance(card2().getId());
        int amount = generateValidAmount(balanceCard1);
        dashboardPage
                .chooseRechargeCard(rechargeCardId)
                .validTransferMoney(amount, transferCardNumber);

        Assertions.assertEquals(balanceCard2 + amount, dashboardPage.getCardBalance(card2().getId()));
        Assertions.assertEquals(balanceCard1 - amount, dashboardPage.getCardBalance(card1().getId()));

    }

    @Test
    void shouldTransferFromInvalidCardToCard1() {
        String transferCardNumber = DataHelper.inValidCard().getNumberCard();
        int amount = 200;
        var transferPage = dashboardPage.chooseRechargeCard(card1().getId());

        transferPage.transferMoney(amount, transferCardNumber);
        transferPage.findErrorMessage();
    }

    @Test
    void shouldTransferAboveCurrentBalance() {
        String transferCardNumber = DataHelper.card2().getNumberCard();
        int balanceCard1 = dashboardPage.getCardBalance(card1().getId());
        int balanceCard2 = dashboardPage.getCardBalance(card2().getId());
        int amount = generateInvalidAmount(balanceCard2);

        var transferPage = dashboardPage.chooseRechargeCard(card1().getId());
        transferPage.transferMoney(amount, transferCardNumber);
        transferPage.findErrorMessage();
        Assertions.assertEquals(balanceCard1, dashboardPage.getCardBalance(card1().getId()));
        Assertions.assertEquals(balanceCard2, dashboardPage.getCardBalance(card2().getId()));


    }

    @Test
    void shouldTransferToTheSameCard() {
        String rechargeCardId = card1().getId();
        String transferCardNumber = card1().getNumberCard();
        int balanceCard1 = dashboardPage.getCardBalance(card1().getId());
        int balanceCard2 = dashboardPage.getCardBalance(card2().getId());
        int amount = generateValidAmount(balanceCard1);
        var transferPage = dashboardPage.chooseRechargeCard(card1().getId());
        transferPage.transferMoney(amount, transferCardNumber);
        transferPage.findErrorMessage();
        Assertions.assertEquals(balanceCard1, dashboardPage.getCardBalance(card1().getId()));
        Assertions.assertEquals(balanceCard2, dashboardPage.getCardBalance(card2().getId()));
    }

}

