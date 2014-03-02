package core;
public class Transaction {

	Commodity commodity1;
	Commodity commodity2;

	double volume1;
	double volume2;

	public Transaction(double volume1, Commodity commodity1, double volume2,
			Commodity commodity2) {
		this.commodity1 = commodity1;
		this.commodity2 = commodity2;

		this.volume1 = volume1;
		this.volume2 = volume2;
	}

	public Commodity getCommodity1() {
		return commodity1;
	}

	public Commodity getCommodity2() {
		return commodity2;
	}

	public double getVolume1() {
		return volume1;
	}

	public double getVolume2() {
		return volume2;
	}

	public double getRatio() {
		return volume2 / volume1;
	}

	public double getTotalVolume() {
		return volume1 + volume2;
	}

	public Transaction getReversedTransaction() {
		return new Transaction(volume2, commodity2, volume1, commodity1);
	}
}
