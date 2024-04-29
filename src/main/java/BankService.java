import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class BankService {
    Map<String, Account> accounts;

    public BankService() {
        accounts = new HashMap<>();
    }

    public String openAccount(List<String> accountHolders) {
        String accountNumber = UUID.randomUUID().toString();
        Account newAccount = new Account(accountNumber, accountHolders);
        accounts.put(accountNumber, newAccount);
        return accountNumber;
    }

    public List<String> split(String accountNumber) {
        Account jointAccount = accounts.get(accountNumber);
        if (jointAccount == null || jointAccount.getAccountHolders().size() <= 1) {
            return null; // No split needed or account does not exist
        }

        BigDecimal balance = jointAccount.getBalance();
        List<String> accountHolders = jointAccount.getAccountHolders();
        int numberOfHolders = accountHolders.size();
        BigDecimal splitAmount = balance.divide(BigDecimal.valueOf(numberOfHolders), 2, RoundingMode.DOWN);
        BigDecimal totalDistributed = splitAmount.multiply(BigDecimal.valueOf(numberOfHolders));
        BigDecimal remaining = balance.subtract(totalDistributed);

        List<String> newAccountNumbers = new ArrayList<>();
        for (String holder : accountHolders) {
            String newAccountNumber = openAccount(List.of(holder));
            Account newAccount = accounts.get(newAccountNumber);
            newAccount.credit(splitAmount);
            newAccountNumbers.add(newAccountNumber);
        }

        // Distribute remaining cents
        for (int i = 0; i < remaining.intValue(); i++) {
            accounts.get(newAccountNumbers.get(i)).credit(BigDecimal.ONE);
        }

        // Remove the original joint account
        accounts.remove(accountNumber);

        return newAccountNumbers;
    }
}
