package ru.netology.web.steps;

import com.codeborne.selenide.Selenide;
import io.cucumber.java.ru.Дано;
import io.cucumber.java.ru.Когда;
import io.cucumber.java.ru.Тогда;
import ru.netology.web.data.DataHelper;
import ru.netology.web.page.DashBoardPage;
import ru.netology.web.page.LoginPage;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MoneyTransferStep {
    private DashBoardPage dashboardPage;

    @Дано("пользователь залогинен с именем {string} и паролем {string}")
    public void shouldLogin(String login, String password) {
        var authInfo = new DataHelper.AuthInfo(login, password);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        var loginPage = Selenide.open("http://localhost:9999", LoginPage.class);
        var verificationPage = loginPage.validLogin(authInfo);

        dashboardPage = verificationPage.validVerify(verificationCode);
    }

    @Когда("пользователь переводит {int} рублей с карты с номером {string} на свою {int} карту с главной страницы")
    public void shouldTransferMoney(int amount, String fromCardNumber, int cardNumber) {
        var fromCard = new DataHelper.CardInfo(fromCardNumber, "");
        var transferPage = dashboardPage.selectCard(cardNumber);

        dashboardPage = transferPage.validTransfer(String.valueOf(amount), fromCard);
    }

    @Тогда("баланс его {int} карты из списка на главной странице должен стать {int} рублей")
    public void shouldCheckBalance(int cardNumber, int expectedBalance) {
        var actualBalance = dashboardPage.getCardBalance(cardNumber);

        assertEquals(expectedBalance, actualBalance);
    }
}