import java.io.Serializable;

class Data implements Serializable {

	private static final long serialVersionUID = 1L;

	String typeOfCommand;
	String firstParameter;
	String secondParameter;

	Data(String type, String first, String second) {
		this.typeOfCommand = type;
		this.firstParameter = first;
		this.secondParameter = second;
	}

	public Data() {
		this.typeOfCommand = null;
		this.firstParameter = null;
		this.secondParameter = null;
	}

	public String getTypeOfCommand() {
		return typeOfCommand;
	}

	public void setTypeOfCommand(String typeOfCommand) {
		this.typeOfCommand = typeOfCommand;
	}

	public String getFirstParameter() {
		return firstParameter;
	}

	public void setFirstParameter(String firstParameter) {
		this.firstParameter = firstParameter;
	}

	public String getSecondParameter() {
		return secondParameter;
	}

	public void setSecondParameter(String secondParameter) {
		this.secondParameter = secondParameter;
	}

	@Override
	public String toString() {
		return typeOfCommand + " " + firstParameter + " " + secondParameter;

	}
}
