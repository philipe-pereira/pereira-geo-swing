package br.com.pereiraeng.geo.swing.input.tti;

import java.awt.Component;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JTextField;

import br.com.pereiraeng.geo.GeoCoordinate;
import br.com.pereiraeng.swing.input.Input;

/**
 * Classe do objeto gráfico que permite a entrada de um ângulo geográfico (i.e.,
 * um valor de 0 a 180 graus, em uma das quatro direções cardenais)
 * 
 * @author Philipe PEREIRA
 *
 */
public class AngleTTIinput extends JTextField implements Input<Float>, FocusListener {
	private static final long serialVersionUID = 1L;

	private boolean latitude;

	public static final String LAT_DEGREES_INPUT = "[0-8]\\d", LAT_MINUTES_INPUT = "[0-5]\\d[NSns]\\d+",
			LONG_DEGREES_INPUT = "((0\\d)|(1[0-7]))\\d", LONG_MINUTES_INPUT = "[0-5]\\d[EWLlewOo]\\d+",
			LATITUDE_NULL_INPUT = "00000\\d*", LONGITUDE_NULL_INPUT = "000000\\d*";

	public AngleTTIinput(boolean latitude) {
		this(Float.NaN, latitude);
	}

	public AngleTTIinput(float angle, boolean latitude) {
		this.set(angle, latitude);
		this.addFocusListener(this);
	}

	@Override
	public Float get() {
		return parse(super.getText(), this.latitude);
	}

	@Override
	public void set(Float angle) {
		this.set(angle, this.latitude);
	}

	public void set(float angle, boolean latitude) {
		if (!Float.isNaN(angle))
			super.setText(GeoCoordinate.toStringEdition(angle, latitude));
		else
			super.setText("");
		this.latitude = latitude;
	}

	@Override
	public Component getComponent() {
		return this;
	}

	private float parse(String input, boolean latitude) {
		if (!"".equals(input)) {
			int degrees = 0;
			float minutes = 0.f;
			boolean signal = true;

			String g = null;
			Pattern p;
			Matcher m;

			p = Pattern.compile(latitude ? AngleTTIinput.LATITUDE_NULL_INPUT : AngleTTIinput.LONGITUDE_NULL_INPUT);
			m = p.matcher(input);
			if (m.find())
				return 0f;

			// degrés
			p = Pattern.compile(latitude ? AngleTTIinput.LAT_DEGREES_INPUT : AngleTTIinput.LONG_DEGREES_INPUT);
			m = p.matcher(input);

			if (!m.find())
				return Float.NaN;
			g = m.group();

			degrees = Integer.parseInt(g);

			// minutes
			m.usePattern(
					Pattern.compile(latitude ? AngleTTIinput.LAT_MINUTES_INPUT : AngleTTIinput.LONG_MINUTES_INPUT));

			if (!m.find())
				return Float.NaN;
			g = m.group();

			String minS = g.substring(0, 2), secS = "0." + g.substring(3, g.length());
			char s = g.charAt(2);
			minutes = Integer.parseInt(minS) + (Float.parseFloat(secS));
			signal = latitude ? (s == 'N' || s == 'n') : (s == 'E' || s == 'e' || s == 'L' || s == 'l');

			return (signal ? 1 : -1) * (degrees + minutes / 60.0f);
		} else
			return Float.NaN;
	}

	// -------------------------- LISTENER --------------------------

	@Override
	public void focusGained(FocusEvent event) {
	}

	@Override
	public void focusLost(FocusEvent event) {
		try {
			// se o valor que foi inserido ao longo do processo de edição
			// for válido, não acontece nada (pois o vetor de double's já
			// vinha sendo alterado)
			float angle = Float.parseFloat(getText().replace(',', '.'));
			this.set(angle);
		} catch (NumberFormatException e) {
		}
	}
}