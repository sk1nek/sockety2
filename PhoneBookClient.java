import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

class PhoneBookClient extends JFrame implements ActionListener, Runnable {

	private String[] commands = { "load", "save", "get", "put", "replace", "delete", "list", "close", "bye" };
	private JTextField firstParameterField = new JTextField(10);
	private JTextField secondParameterField = new JTextField(10);
	private JTextArea textArea = new JTextArea(15, 18);
	private JComboBox<String> commandComboBox = new JComboBox<>(commands);
	private JButton acceptCommandButton = new JButton("Akceptuj");

	private static final int SERVER_PORT = 25000;
	private String name;
	private String serverHost;
	private Socket socket;
	private ObjectOutputStream outputStream;
	private ObjectInputStream inputStream;

	private static final long serialVersionUID = 1L;

	PhoneBookClient(String name, String host) {

		super(name);
		this.name = name;
		this.serverHost = host;
		setSize(500, 340);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setResizable(false);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent event) {
				// try {
				// outputStream.close();
				// inputStream.close();
				// socket.close();
				// } catch (IOException e) {
				// System.out.println(e);
				// }
			}

			@Override
			public void windowClosed(WindowEvent event) {
				windowClosing(event);
			}
		});

		JPanel panel = new JPanel();
		JLabel messageLabel = new JLabel("Napisz:");
		JLabel textAreaLabel = new JLabel("Dialog:");
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);

		panel.add(messageLabel);
		panel.add(commandComboBox);
		panel.add(firstParameterField);
		panel.add(secondParameterField);
		panel.add(acceptCommandButton);

		firstParameterField.addActionListener(this);
		secondParameterField.addActionListener(this);
		acceptCommandButton.addActionListener(this);
		fieldsManagment(true, false, "nazwa pliku", "");
		commandComboBox.addItemListener(event -> {
            fieldsCleaner();
            Object item = event.getItem();

            textArea.setText("Affected items: " + item.toString());

            if (event.getStateChange() == ItemEvent.SELECTED) {
                configureOnCommandComboBoxItemSwitch(item.toString());
                textArea.setText(item.toString() + " selected.");
            }
        });

		panel.add(textAreaLabel);
		JScrollPane scroll_bars = new JScrollPane(textArea, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		panel.add(scroll_bars);
		setContentPane(panel);
		setVisible(true);
		new Thread(this).start();
	}

	public static void main(String[] args) {
		String name;
		String host;

		host = JOptionPane.showInputDialog("Podaj adres serwera");
		name = JOptionPane.showInputDialog("Podaj nazwe klienta");
		if (name != null && !name.equals("")) {
			new PhoneBookClient(name, host);
		}
	}

	synchronized public void printReceivedMessage(String message) {
		String tmp_text = textArea.getText();
		textArea.setText(tmp_text + ">>> " + message + "\n");
	}

	synchronized public void printSentMessage(String message) {
		String text = textArea.getText();
		textArea.setText(text + "<<< " + message + "\n");
	}

	private void closeClient(){
		try{
			inputStream.close();
			outputStream.close();
			socket.close();
		}catch(IOException ioex){
			ioex.printStackTrace();
		}
	}

	@Override
	public void run() {
		if (serverHost.equals("")) {
			serverHost = "localhost";
		}
		try {
			socket = new Socket(serverHost, SERVER_PORT);
			inputStream = new ObjectInputStream(socket.getInputStream());
			outputStream = new ObjectOutputStream(socket.getOutputStream());
			outputStream.writeObject(name);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Polaczenie sieciowe dla klienta nie moze byc utworzone");
			setVisible(false);
			dispose(); // zwolnienie zasob�w graficznych
						// okno graficzne nie zostanie utworzone
			return;
		}
		try {
			while (true) {
				// Data data = (Data) inputStream.readObject();
				// printReceivedMessage(data.getFirstParameter());
				// printReceivedMessage("XDD");
				if ("fh".equals("exit")) {
					inputStream.close();
					outputStream.close();
					socket.close();
					setVisible(false);
					dispose();
					break;
				}
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Polaczenie sieciowe dla klienta zostalo przerwane");
			setVisible(false);
			dispose();
		}
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		Data data;
		Object source = evt.getSource();

		if (source == acceptCommandButton) {
			try {
				data = new Data((String) commandComboBox.getSelectedItem(), firstParameterField.getText(),
						secondParameterField.getText());
				outputStream.writeObject(data);
				printSentMessage(data.toString());
				if (data.toString().equals("exit")) {
					inputStream.close();
					outputStream.close();
					socket.close();
					setVisible(false);
					dispose();
					return;
				}
			} catch (IOException e) {
				System.out.println("Wyjatek klienta " + e);
			}
		}
		repaint();

	}

	public void configureOnCommandComboBoxItemSwitch(String typeOfCommand) {

		switch (typeOfCommand) {
		case "load":
			fieldsManagment(true, false, "nazwa pliku", "");
			break;

		case "save":
			fieldsManagment(true, false, "nazwa pliku", "");
			break;

		case "get":
			fieldsManagment(true, false, "imię", "");
			break;

		case "put":
			fieldsManagment(true, true, "imię", "numer");
			break;

		case "replace":
			fieldsManagment(true, true, "imię", "numer");
			break;

		case "delete":
			fieldsManagment(true, false, "imię", "");
			break;

		case "list":
			fieldsManagment(false, false, "", "");
			break;

		case "close":
			fieldsManagment(false, false, "", "");
			break;

		case "bye":
			fieldsManagment(false, false, "", "");
			break;
		}
	}

	public void fieldsManagment(boolean firstParameterFieldEditable, boolean secondParamterFieldEditable,
			String firstToolTip, String secondToolTip) {
		firstParameterField.setEditable(firstParameterFieldEditable);
		secondParameterField.setEditable(secondParamterFieldEditable);

		firstParameterField.setToolTipText(firstToolTip);
		secondParameterField.setToolTipText(secondToolTip);
	}

	public void fieldsCleaner() {
		firstParameterField.setText("");
		secondParameterField.setText("");
	}

	public void sendOutPut(String typeOfCommand) {
		switch (typeOfCommand) {
		case "load":
			try {
				outputStream.writeObject(typeOfCommand);
				outputStream.writeObject(firstParameterField.getText());
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;

		case "save":
			try {
				outputStream.writeObject(typeOfCommand);
				outputStream.writeObject(firstParameterField.getText());
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;

		case "get":
			try {
				outputStream.writeObject(typeOfCommand);
				outputStream.writeObject(firstParameterField.getText());
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;

		case "put":
			try {
				outputStream.writeObject(typeOfCommand);
				outputStream.writeObject(firstParameterField.getText());
				outputStream.writeObject(secondParameterField.getText());
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;

		case "replace":
			try {
				outputStream.writeObject(typeOfCommand);
				outputStream.writeObject(firstParameterField.getText());
				outputStream.writeObject(secondParameterField.getText());
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;

		case "delete":
			try {
				outputStream.writeObject(typeOfCommand);
				outputStream.writeObject(firstParameterField.getText());
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;

		case "list":
			try {
				outputStream.writeObject(typeOfCommand);
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;

		case "close":
			try {
				outputStream.writeObject(typeOfCommand);
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;

		case "bye":
			try {
				outputStream.writeObject(typeOfCommand);
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		}
	}
}
