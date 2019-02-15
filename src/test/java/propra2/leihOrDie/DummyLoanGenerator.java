package propra2.leihOrDie;

import propra2.leihOrDie.model.Item;
import propra2.leihOrDie.model.Loan;
import propra2.leihOrDie.model.User;

public class DummyLoanGenerator {

    // TODO: change the generator in a way that it will recieve in the constructor the Item to be loaned, so that the user hat the item is his- will be extractable for use and test
    public Loan generateLoan() throws Exception {
        // secondUser leiht item (Fahrrad) von firstUser aus.
        DummyUserGenerator dummyUserGenerator = new DummyUserGenerator();
        User firstUser = dummyUserGenerator.generateUser();
        User secondUser = dummyUserGenerator.generateUser();
        DummyItemGenerator dummyItemGenerator = new DummyItemGenerator();
        Item item = dummyItemGenerator.generatItem(firstUser);
        int duration = 5;
        String state = "Laufend";


        Loan loan = new Loan(state, duration, secondUser, item);

        return loan;
    }


    public Loan generateAnotherLoan() throws Exception {
        // firstUser leiht item (Kickbike) von secondUser aus.
        DummyUserGenerator dummyUserGenerator = new DummyUserGenerator();
        User firstUser = dummyUserGenerator.generateUser();
        User secondUser = dummyUserGenerator.generateUser();
        DummyItemGenerator dummyItemGenerator = new DummyItemGenerator();
        Item item = dummyItemGenerator.generatAnotherItem(secondUser);
        int duration = 3;
        String state = "Laufend";


        Loan loan = new Loan(state, duration, firstUser, item);

        return loan;
    }


}
