import java.util.LinkedList;

public abstract class Commodity {

	LinkedList<Transaction> transactions;

	public void addTransaction(Transaction transaction) {
		transactions.add(transaction);
	}

	public LinkedList<Transaction> getTransactions() {
		return transactions;
	}

}
