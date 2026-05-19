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
        var firstCard = DataHelper.getFirstCardInfo();
        var secondCard = DataHelper.getSecondCardInfo();
        DataHelper.CardInfo fromCard;
        DataHelper.CardInfo toCard;

        if (fromCardNumber.equals(firstCard.getNumber())) {
            fromCard = firstCard;
        } else {
            fromCard = secondCard;
        }

        if (cardNumber == 1) {
            toCard = firstCard;
        } else {
            toCard = secondCard;
        }

        var transferPage = dashboardPage.selectCard(toCard);
        dashboardPage = transferPage.validTransfer(String.valueOf(amount), fromCard);
    }

    @Тогда("баланс его {int} карты из списка на главной странице должен стать {int} рублей")
    public void shouldCheckBalance(int cardNumber, int expectedBalance) {
        DataHelper.CardInfo card;

        if (cardNumber == 1) {
            card = DataHelper.getFirstCardInfo();
        } else {
            card = DataHelper.getSecondCardInfo();
        }

        var actualBalance = dashboardPage.getCardBalance(card);
        assertEquals(expectedBalance, actualBalance);
    }
}