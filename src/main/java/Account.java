import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

class Account {
    private String accountNumber;
    private List<String> accountHolders;
    private BigDecimal balance;

    public Account(String accountNumber, List<String> accountHolders) {
        this.accountNumber = accountNumber;
        this.accountHolders = new ArrayList<>(accountHolders);
        this.balance = BigDecimal.ZERO;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public List<String> getAccountHolders() {
        return new ArrayList<>(accountHolders);
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void credit(BigDecimal amount) {
        balance = balance.add(amount);
    }

    public void debit(BigDecimal amount) {
        balance = balance.subtract(amount);
    }
}
