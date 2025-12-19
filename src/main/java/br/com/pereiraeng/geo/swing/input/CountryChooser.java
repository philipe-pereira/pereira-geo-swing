package br.com.pereiraeng.geo.swing.input;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;

import javax.swing.JComboBox;
import javax.swing.JOptionPane;

public class CountryChooser {

	public static int getCountryInt() {

		LinkedHashMap<String, Integer> nomes = new LinkedHashMap<>();
		try {
			InputStream stream = CountryChooser.class.getResourceAsStream("/pays.txt");
			BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.ISO_8859_1));
			String str = null;
			while ((str = reader.readLine()) != null) {
				String[] t = str.split("\t");
				nomes.put(t[0].trim(), Integer.parseInt(t[3]));
			}
			stream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// -----------------------------------------------

		JComboBox<String> combobox = new JComboBox<>(nomes.keySet().toArray(new String[nomes.size()]));
		JOptionPane.showMessageDialog(null, combobox, "Escolha o país", JOptionPane.PLAIN_MESSAGE);
		combobox.setSelectedIndex(-1);

		// -----------------------------------------------

		return nomes.get(combobox.getSelectedItem());
	}
}
