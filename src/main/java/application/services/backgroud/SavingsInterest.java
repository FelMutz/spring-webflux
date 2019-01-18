package application.services.backgroud;


import application.domain.Account;
import application.domain.enums.AccountType;
import application.services.AccountService;
import application.services.Roud;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
@EnableScheduling
public class SavingsInterest{
    @Autowired
    AccountService accountService;

    //@Scheduled(cron = "0 0 0 1 * *")
    @Scheduled(cron = "0 */1 * * * *")
    public void savingsInterest() {

        accountService.findByAccountType(AccountType.SAVING)
                .map(account -> calcInterestMonthlyOfSavingsAccounts(account))
                .flatMap(account -> accountService.updateAccount(account))
                .subscribe();
    }

    public Account calcInterestMonthlyOfSavingsAccounts(Account account){
        account.setBalance(Roud.roudBalance(account.getBalance() + account.getBalance() * 6 / 100));
        return account;
    }
}
