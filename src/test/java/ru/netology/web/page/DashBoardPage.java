package ru.netology.web.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class DashBoardPage {
    private final SelenideElement header = $("[data-test-id='dashboard']");
    private final ElementsCollection cards = $$(".list__item");
    private final String balanceStart = "баланс: ";
    private final String balanceFinish = " р.";

    public DashBoardPage() {
        header.should(Condition.visible).should(Condition.text("Личный кабинет"));
    }

    private SelenideElement getCardByIndex(int index) {
        return cards.get(index - 1);
    }

    public int getCardBalance(int index) {
        var text = getCardByIndex(index).getText();
        return extractBalance(text);
    }

    public TransferPage selectCard(int index) {
        getCardByIndex(index).$("button").click();
        return new TransferPage();
    }

    private int extractBalance(String text) {
        var start = text.indexOf(balanceStart);
        var finish = text.indexOf(balanceFinish);
        var value = text.substring(start + balanceStart.length(), finish);
        return Integer.parseInt(value);
    }
}