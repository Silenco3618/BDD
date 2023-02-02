package ru.netology.web.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.netology.web.page.DashboardPage;
import ru.netology.web.page.LoginPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.web.data.DataHelper.*;
import static ru.netology.web.page.DashboardPage.firstCardButton;
import static ru.netology.web.page.DashboardPage.secondCardButton;

class MoneyTransferTest {
    @BeforeEach
    public void setUp() {
        open("http://localhost:9999");
        var loginPage = new LoginPage();
        var authInfo = getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = getVerificationCodeFor(authInfo);
        verificationPage.validVerify(verificationCode);
    }

    @Test
    @DisplayName("Transfer to first card and back")
    public void shouldTransferToFirstCardAndBack() {
        var dashboardPage = new DashboardPage();
        var firstCardBalanceStart = dashboardPage.getFirstCardBalance();
        var secondCardBalanceStart = dashboardPage.getSecondCardBalance();
        int amount = 7_500;

        var transfer = firstCardButton();
        transfer.transferFromCardToCard(amount, getSecondCardNumber());
        var firstCardBalanceResult = firstCardBalanceStart + amount;
        var secondCardBalanceResult = secondCardBalanceStart - amount;

        assertEquals(firstCardBalanceResult, dashboardPage.getFirstCardBalance());
        assertEquals(secondCardBalanceResult, dashboardPage.getSecondCardBalance());
    }

    @Test
    @DisplayName("Transfer to second and back")
    public void shouldTransferToSecondCardAndBack() {
        var dashboardPage = new DashboardPage();
        var firstCardBalanceStart = dashboardPage.getFirstCardBalance();
        var secondCardBalanceStart = dashboardPage.getSecondCardBalance();
        int amount = 7_500;

        var transfer = secondCardButton();
        transfer.transferFromCardToCard(amount, getFirstCardNumber());
        var firstCardBalanceResult = firstCardBalanceStart - amount;
        var secondCardBalanceResult = secondCardBalanceStart + amount;

        assertEquals(firstCardBalanceResult, dashboardPage.getFirstCardBalance());
        assertEquals(secondCardBalanceResult, dashboardPage.getSecondCardBalance());
    }

    @Test
    @DisplayName("Shouldn't transfer money if the amount is more then balance")
    public void shouldNotTransferMoneyIfAmountMoreThenBalance() {
        var dashboardPage = new DashboardPage();
        int amount = 50_000;

        var transfer = firstCardButton();
        transfer.transferFromCardToCard(amount, getSecondCardNumber());
        transfer.getErrorLimit();
    }
}
