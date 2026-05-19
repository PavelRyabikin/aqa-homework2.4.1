package ru.netology.web.test;

import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.Test;
import ru.netology.web.page.LoginPage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.web.data.DataHelper.*;

public class MoneyTransferTest {

    @Test
    void shouldTransferMoneyBetweenOwnCards() {

        var authInfo = getAuthInfo();
        var verificationCode = getVerificationCodeFor(authInfo);
        var loginPage = Selenide.open("http://localhost:9999", LoginPage.class);
        var verificationPage = loginPage.validLogin(authInfo);
        var dashboardPage = verificationPage.validVerify(verificationCode);
        var firstCard = getFirstCardInfo();
        var secondCard = getSecondCardInfo();

        int amount = 123;
        int firstCardBalanceBefore = dashboardPage.getCardBalance(firstCard);
        int secondCardBalanceBefore = dashboardPage.getCardBalance(secondCard);
        int firstCardBalanceAfter = firstCardBalanceBefore - amount;
        int secondCardBalanceAfter = secondCardBalanceBefore + amount;

        var transferPage = dashboardPage.selectCard(secondCard);
        var updatedDashboardPage = transferPage.validTransfer(String.valueOf(amount), firstCard);

        assertEquals(firstCardBalanceAfter, updatedDashboardPage.getCardBalance(firstCard));
        assertEquals(secondCardBalanceAfter, updatedDashboardPage.getCardBalance(secondCard));
    }
}