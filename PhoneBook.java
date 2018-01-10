import java.util.concurrent.ConcurrentHashMap;

final public class PhoneBook {

	static enum TypeOfCommand {
		LOAD, SAVE, GET, PUT, REPLACE, DELETE, LIST, CLOSE;
	}

	static ConcurrentHashMap<String, Integer> PhoneBookHashMap = new ConcurrentHashMap<String, Integer>();

	PhoneBook() {
		// PhoneBookHashMap.put("Pawe³", 32323223);
		// PhoneBookHashMap.put("Mariusz", 12345679);
	}

	public void chooseOperation(Data data) {

		switch (data.typeOfCommand) {

		case "CLOSE":
			CLOSE();
			break;

		case "DELETE":
			DELETE(data.firstParameter);
			break;

		case "GET":
			GET(data.firstParameter);
			break;

		case "LIST":
			LIST();
			break;

		case "LOAD":
			LOAD(data.firstParameter);
			break;

		case "PUT":
			PUT(data.firstParameter, data.secondParameter);
			break;

		case "REPLACE":
			REPLACE(data.firstParameter, data.secondParameter);
			break;

		case "SAVE":
			SAVE(data.firstParameter);
			break;

		default:
			break;

		}

	}

	public String LOAD(String fileName) {

		return null;
	}

	public String SAVE(String fileName) {
		// FileWriter fwriter = new FileWriter(fileName);
		// ObjectOutputStream writer = new ObjectOutputStream(fwriter);
		//
		// writer.writeObject(PhoneBookHashMap);
		//
		// writer.close(); //don't forget to close the writer
		return fileName;
	}

	public String GET(String name) {
		return name;
	}

	public String PUT(String name, String number) {
		if (name.equals(null) || name == "" || number.equals(null) || number == "") {
			return "ERROR Przynajmniej jedno z podanych danych jest puste.";
		}

		try {
			Integer.parseInt(number);
		} catch (NumberFormatException e) {
			return "ERROR Z³y format podanego numeru.";
		}

		if (100000000 > Integer.parseInt(number) || 999999999 < Integer.parseInt(number)) {
			return "ERROR Numer zawiera nieprawdi³ow¹ iloœæ cyfr.";
		}

		PhoneBookHashMap.put(name, Integer.parseInt(number));
		return "OK";
	}

	public String REPLACE(String name, String number) {
		return number;
	}

	public String DELETE(String name) {
		PhoneBookHashMap.remove(name);
		return name;
	}

	public void LIST() {
		System.out.println("klasyka");
		System.out.println("klasyka2");
		PhoneBookHashMap.put("xD", 123);
		System.out.println("klasyka3");
		System.out.println("well: " + PhoneBookHashMap);

	}

	public String CLOSE() {
		return null;
	}

	public String BYE() {
		return null;
	}
}
