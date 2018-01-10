import java.io.Serializable;

class Data implements Serializable {

	private static final long serialVersionUID = 1L;

	String operationType;
	String firstParameter;
	String secondParameter;

	Data(String type, String first, String second) {
		this.operationType = type;
		this.firstParameter = first;
		this.secondParameter = second;
	}

	Data() {
		this.operationType = null;
		this.firstParameter = null;
		this.secondParameter = null;
	}

	public String getOperationType() {
		return operationType;
	}

	public void setOperationType(String operationType) {
		this.operationType = operationType;
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
		return operationType + " " + firstParameter + " " + secondParameter;

	}
}
