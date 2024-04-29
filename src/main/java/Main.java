import java.math.BigDecimal;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        BankService bankService = new BankService();

        List<String> holders = List.of("Alice", "Bob", "Charlie");
        String jointAccountNumber = bankService.openAccount(holders);

        Account jointAccount = bankService.accounts.get(jointAccountNumber);
        jointAccount.credit(new BigDecimal("300.00"));

        System.out.println("Initial state of the joint account:");
        System.out.println(jointAccount);

        List<String> newAccountNumbers = bankService.split(jointAccountNumber);

        System.out.println("New accounts after split:");
        newAccountNumbers.forEach(accountNumber -> {
            Account account = bankService.accounts.get(accountNumber);
            System.out.println(account + " - Balance: " + account.getBalance());
        });
    }
}
