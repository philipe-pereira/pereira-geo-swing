package br.com.pereiraeng.geo.swing.input;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import br.com.pereiraeng.geo.CoordinateUTM;
import br.com.pereiraeng.geo.GeoCoordinate;
import br.com.pereiraeng.math.swing.input.AngleInput;
import br.com.pereiraeng.swing.input.Input;

public class CoordinateInput extends JPanel implements Input<GeoCoordinate>, ActionListener {
	private static final long serialVersionUID = 1L;

	private JPanel card;

	private AngleInput lon, lat;

	private JCheckBox north;
	private JSpinner z, e, n;

	private JTextField gmaps;

	public CoordinateInput(GeoCoordinate coordinate) {
		super(new BorderLayout());

		ButtonGroup bg = new ButtonGroup();

		JPanel p0 = new JPanel();

		JRadioButton rb = new JRadioButton("Geográficas", false);
		rb.setActionCommand("L");
		rb.addActionListener(this);
		bg.add(rb);
		p0.add(rb);

		rb = new JRadioButton("Mercator", true);
		rb.setActionCommand("M");
		rb.addActionListener(this);
		bg.add(rb);
		p0.add(rb);

		rb = new JRadioButton("Formato Gmaps", true);
		rb.setActionCommand("G");
		rb.addActionListener(this);
		bg.add(rb);
		p0.add(rb);

		super.add(p0, BorderLayout.NORTH);

		// ------------------------------------------

		this.card = new JPanel(new CardLayout());

		p0 = new JPanel();

		p0.add(new JLabel("Lon"));
		p0.add(this.lon = new AngleInput());
		p0.add(new JLabel("Lat"));
		p0.add(this.lat = new AngleInput());

		this.card.add(p0, "L");

		// ------------------------------------------

		p0 = new JPanel();

		p0.add(new JLabel("Z"));
		p0.add(this.z = new JSpinner(new SpinnerNumberModel(1, 1, 60, 1)));
		p0.add(this.north = new JCheckBox("N", false));
		p0.add(new JLabel("E"));
		p0.add(this.e = new JSpinner(new SpinnerNumberModel(1, 1, 1_000_000, 1)));
		p0.add(new JLabel("N"));
		p0.add(this.n = new JSpinner(new SpinnerNumberModel(1, 1, 10_000_000, 1)));

		this.card.add(p0, "M");

		// ------------------------------------------

		this.card.add(this.gmaps = new JTextField(15), "G");

		// ------------------------------------------

		super.add(card, BorderLayout.CENTER);
		((CardLayout) card.getLayout()).show(card, "M");

		// ------------------------------------------

		set(coordinate);
	}

	public CoordinateInput() {
		this(null);
	}

	@Override
	public void set(GeoCoordinate k) {
		if (k != null) {
			switch (formatSelected) {
			case 'L':
				lon.set(k.x);
				lat.set(k.y);
				break;
			case 'M':
				CoordinateUTM utm = CoordinateUTM.longLat2utm(k.x, k.y);
				z.setValue((int) utm.getZone());
				north.setSelected(utm.isHn());
				e.setValue(utm.x);
				n.setValue(utm.y);
				break;
			case 'G':
				gmaps.setText(String.format(Locale.US, "%.15f, %.15f", k.y, k.x));
				break;
			}
		} else {
			switch (formatSelected) {
			case 'L':
				lon.set(0f);
				lat.set(0f);
				break;
			case 'M':
				z.setValue(31);
				north.setSelected(true);
				e.setValue(166021);
				n.setValue(0);
				break;
			case 'G':
				gmaps.setText("0.000000000000000, 0.000000000000000");
				break;
			}
		}
	}

	@Override
	public GeoCoordinate get() {
		GeoCoordinate out = null;
		switch (formatSelected) {
		case 'L':
			out = new GeoCoordinate(lon.get(), lat.get());
			break;
		case 'M':
			out = new GeoCoordinate(CoordinateUTM.utm2longLat(((Integer) this.z.getValue()).byteValue(),
					this.north.isSelected(), (int) this.e.getValue(), (int) this.n.getValue()));
			break;
		case 'G':
			String[] g = gmaps.getText().split(", ");
			if (g.length == 2) {
				try {
					out = new GeoCoordinate(Float.parseFloat(g[1]), Float.parseFloat(g[0]));
				} catch (NumberFormatException e) {
				}
			}
			break;
		}
		return out;
	}

	@Override
	public Component getComponent() {
		return this;
	}

	// ==================== LISTENER ====================

	private transient char formatSelected = 'M';

	@Override
	public void actionPerformed(ActionEvent event) {
		GeoCoordinate c = get();
		String command = event.getActionCommand();
		((CardLayout) card.getLayout()).show(card, command);
		this.formatSelected = command.charAt(0);
		this.set(c);
	}
}
