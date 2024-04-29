import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;

public class BankServiceTest {
    private BankService bankService;

    @BeforeEach
    public void setUp() {
        bankService = new BankService();
    }

    @Test
    public void testEqualSplit() {
        String accountNumber = bankService.openAccount(List.of("Alice", "Bob"));
        Account account = bankService.accounts.get(accountNumber);
        account.credit(new BigDecimal("100.00"));
        List<String> newAccounts = bankService.split(accountNumber);

        BigDecimal aliceBalance = bankService.accounts.get(newAccounts.get(0)).getBalance();
        BigDecimal bobBalance = bankService.accounts.get(newAccounts.get(1)).getBalance();

        assertEquals(new BigDecimal("50.00"), aliceBalance);
        assertEquals( new BigDecimal("50.00"), bobBalance);
    }

    @Test
    public void testMaxOneCentGainOrLoss() {
        String accountNumber = bankService.openAccount(List.of("Alice", "Bob", "Charlie"));
        Account account = bankService.accounts.get(accountNumber);
        account.credit(new BigDecimal("100.00"));
        bankService.split(accountNumber);

        BigDecimal expectedTotal = new BigDecimal("100.00");
        BigDecimal totalAfterSplit = bankService.accounts.values().stream()
                .map(Account::getBalance)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal difference = totalAfterSplit.subtract(expectedTotal).abs();
        assertTrue(difference.compareTo(new BigDecimal("0.01")) <= 0);
    }


    @Test
    public void testNonExistentAccount() {
        List<String> newAccounts = bankService.split("nonexistent");
        assertNull( newAccounts);
    }

    @Test
    public void testSingleHolderNoSplit() {
        String accountNumber = bankService.openAccount(List.of("Alice"));
        Account account = bankService.accounts.get(accountNumber);
        account.credit(new BigDecimal("50.00"));
        List<String> newAccounts = bankService.split(accountNumber);

        assertNull( newAccounts);
        assertEquals(new BigDecimal("50.00"), bankService.accounts.get(accountNumber).getBalance());
    }

    @Test
    public void testRedistributionOfRemainingCents() {
        String accountNumber = bankService.openAccount(List.of("Alice", "Bob", "Charlie"));
        Account account = bankService.accounts.get(accountNumber);
        account.credit(new BigDecimal("100.00"));
        bankService.split(accountNumber);

        BigDecimal actualBalance = bankService.accounts.values().iterator().next().getBalance();
        BigDecimal expectedBalance = new BigDecimal("33.34");
        BigDecimal delta = actualBalance.subtract(expectedBalance).abs();

        assertTrue(delta.compareTo(new BigDecimal("0.01")) <= 0, "The balance difference should be no more than one cent");
    }
}
