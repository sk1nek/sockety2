import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

final public class PhoneBook {


	private static ConcurrentHashMap<String, String> phoneBookHashMap = new ConcurrentHashMap<>();

	public String performOperation(Data data) {

		String ret = "ERROR Nieprawidlowy typ operacji.";

		switch (data.operationType.toLowerCase()) {

		case "close":
			ret = close();
			break;

		case "delete":
			ret = delete(data.firstParameter);
			break;

		case "get":
			ret = get(data.firstParameter);
			break;

		case "list":
			ret = list();
			break;

		case "load":
			ret = load(data.firstParameter);
			break;

		case "put":
			ret = put(data.firstParameter, data.secondParameter);
			break;

		case "replace":
			ret = replace(data.firstParameter, data.secondParameter);
			break;

		case "save":
			ret = save(data.firstParameter);
			break;

		default:
			break;

		}

		return ret;
	}

	public String load(String fileName) {

		Path path = Paths.get(fileName);

		try{
			List<String> buffer = Files.readAllLines(path);

			buffer.remove(0); //funkcja list() zwraca niepotrzebny naglowek, pozbywam sie go więc
			buffer.parallelStream().forEach(p-> {
				String[] split = p.split(" : "); //sposob podzialu zastosowany w list()
				phoneBookHashMap.put(split[0], split[1]);
			});

		}catch(IOException ioex){
			ioex.printStackTrace();
			return "ERROR Odczyt danych z pliku niemozliwy";
		}

		return "OK";
	}

	public String save(String fileName) {

		Path path = Paths.get(fileName);

		try{
			Files.write(path, list().getBytes());
		}catch(IOException ioex){
			ioex.printStackTrace();
			return "ERROR Zapis do pliku niemozliwy.";
		}

		return "OK";
	}

	public String get(String name) {

		if(phoneBookHashMap.containsKey(name)){
			return phoneBookHashMap.get(name);
		}

		return "ERROR Nie znaleziono takiej osoby.";
	}

	public String put(String name, String number) {

		if (name == null || name.isEmpty() || number == null || number.isEmpty())
			return "ERROR Przynajmniej jedno z podanych danych jest puste.";

		if(!number.matches("\\d{9}"))
			return "ERROR Numer nie sklada sie z dziewieciu cyfr.";

		if (number.length() != 9)
			return "ERROR Numer zawiera nieprawdi�ow� ilo�� cyfr.";

		phoneBookHashMap.put(name, number);
		return "OK";
	}

	public String replace(String name, String number) {

		if(!phoneBookHashMap.containsKey(name))
			return "ERROR Nie znaleziono osoby w bazie danych.";
		else
			phoneBookHashMap.put(name, number);

		return "OK";
	}

	public String delete(String name) {

		if(phoneBookHashMap.containsKey(name)){
			phoneBookHashMap.remove(name);
			return "OK";
		}else
			return "ERROR Nie znaleziono osoby w bazie danych";
	}

	public String list() {

		StringBuilder ret = new StringBuilder("Phone Book: \n");

		phoneBookHashMap.forEach(
				(k, v) -> ret.append(k)
						.append(" : ")
						.append(v)
						.append('\n')
		);

		return ret.toString();
	}

	public String close() {
		return null;
	}

	public String bye() {
		return null;
	}
}
