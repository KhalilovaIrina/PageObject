package ru.netology.web.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.web.data.DataHelper;
import ru.netology.web.page.DashboardPage;
import ru.netology.web.page.LoginPageV2;
import ru.netology.web.page.MoneyTransferPage;

import static com.codeborne.selenide.Selenide.open;

class MoneyTransferTest {

    @BeforeEach
    void shouldSuccessLogin() {
        open("http://localhost:9999");
        var loginPage = new LoginPageV2();
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        verificationPage.validVerify(verificationCode);
    }

    @Test
    void shouldTransferFromCard2ToCard1() {
        String rechargeCardId = DataHelper.Card1().getId();
        String transferCardNumber = DataHelper.Card2().getNumberCard();
        int amount = 200;

        int balanceCard1 = new DashboardPage().getCardBalance(DataHelper.Card1().getId());
        int balanceCard2 = new DashboardPage().getCardBalance(DataHelper.Card2().getId());

        new DashboardPage()
                .chooseRechargeCard(rechargeCardId)
                .transferMoney(amount, transferCardNumber);

        Assertions.assertEquals(balanceCard1 + amount, new DashboardPage().getCardBalance(DataHelper.Card1().getId()));
        Assertions.assertEquals(balanceCard2 - amount, new DashboardPage().getCardBalance(DataHelper.Card2().getId()));

    }
    @Test
    void shouldTransferFromCard1ToCard2() {
        String rechargeCardId = DataHelper.Card2().getId();
        String transferCardNumber = DataHelper.Card1().getNumberCard();
        int amount = 200;

        int balanceCard1 = new DashboardPage().getCardBalance(DataHelper.Card1().getId());
        int balanceCard2 = new DashboardPage().getCardBalance(DataHelper.Card2().getId());

        new DashboardPage()
                .chooseRechargeCard(rechargeCardId)
                .transferMoney(amount, transferCardNumber);

        Assertions.assertEquals(balanceCard2 + amount, new DashboardPage().getCardBalance(DataHelper.Card2().getId()));
        Assertions.assertEquals(balanceCard1 - amount, new DashboardPage().getCardBalance(DataHelper.Card1().getId()));

    }
    @Test
    void shouldTransferFromInvalidCardToCard1() {
        String rechargeCardId = DataHelper.Card1().getId();
        String transferCardNumber = DataHelper.InValidCard().getNumberCard();
        int amount = 200;

        new DashboardPage()
                .chooseRechargeCard(rechargeCardId)
                .transferMoney(amount, transferCardNumber);
        new MoneyTransferPage().errorMessage();
    }

    @Test
    void shouldTransferAboveCurrentBalance() {
        String rechargeCardId = DataHelper.Card1().getId();
        String transferCardNumber = DataHelper.Card2().getNumberCard();
        int amount = new DashboardPage().getCardBalance(DataHelper.Card2().getId()) + 1;

        new DashboardPage()
                .chooseRechargeCard(rechargeCardId)
                .transferMoney(amount, transferCardNumber);
        new MoneyTransferPage().errorMessage();
    }

    @Test
    void shouldTransferToTheSameCard() {
        String rechargeCardId = DataHelper.Card1().getId();
        String transferCardNumber = DataHelper.Card1().getNumberCard();
        int amount = 100;

        new DashboardPage()
                .chooseRechargeCard(rechargeCardId)
                .transferMoney(amount, transferCardNumber);
        new MoneyTransferPage().errorMessage();
    }

}

