package br.com.pereiraeng.geo.swing.input.tti;

import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import br.com.pereiraeng.geo.GeoCoordinate;
import br.com.pereiraeng.math.geometry.Coordinate;
import br.com.pereiraeng.physics.Grandeza;
import br.com.pereiraeng.physics.swing.MedidaInput;
import br.com.pereiraeng.swing.Grade;
import br.com.pereiraeng.swing.input.Input;

/**
 * Classe do objeto gráfico que permite a inserção e edição de coordenadas
 * geográficas
 * 
 * @author Philipe PEREIRA
 *
 */
public class CoordinateTTIinput extends Grade implements DocumentListener, ActionListener, Input<GeoCoordinate> {
	private static final long serialVersionUID = 1L;

	private GeoCoordinate referencial;

	private boolean azimutais, relativas;

	// ---------------------------------------------

	/**
	 * Campo onde será indicado as coordenadas atualmente digitadas pelo usuário
	 */
	private JTextField mainField;

	private AngleTTIinput latitudeInput, longitudeInput;

	private MedidaInput azimutInput;
	private MedidaInput distanceInput;

	protected JRadioButton coordAbs, coordRel;
	protected ButtonGroup bg;

	/**
	 * Construtor do objeto gráfico que permite a inserção e edição de
	 * coordenadas
	 */
	public CoordinateTTIinput() {
		this(null);
	}

	/**
	 * Construtor do objeto gráfico que permite a inserção e edição de
	 * coordenadas
	 * 
	 * @param azimutais
	 *            <code>true</code> para que as coordenadas sejam inseridas como
	 *            sendo uma distância numa dada direção de um dado ponto de
	 *            {@link #setReference(Coordinate) referência}
	 * @param relativas
	 *            <code>true</code> para dar a opção ao usuário de inserir as
	 *            coordenadas relativas a partir de um dado ponto de
	 *            {@link #setReference(Coordinate) referência}
	 */
	public CoordinateTTIinput(boolean azimutais, boolean relativas) {
		this(null, azimutais, relativas);
	}

	/**
	 * Construtor do objeto gráfico que permite a inserção e edição de
	 * coordenadas
	 * 
	 * @param coordonnees
	 *            coordenadas iniciais
	 */
	public CoordinateTTIinput(GeoCoordinate coordonnees) {
		this(coordonnees, false, false);
	}

	/**
	 * Construtor do objeto gráfico que permite a inserção e edição de
	 * coordenadas
	 * 
	 * @param coordonnees
	 *            coordenadas iniciais
	 * @param azimutais
	 *            <code>true</code> para que as coordenadas sejam inseridas como
	 *            sendo uma distância numa dada direção de um dado ponto de
	 *            {@link #setReference(Coordinate) referência}
	 * @param relativas
	 *            <code>true</code> para dar a opção ao usuário de inserir as
	 *            coordenadas relativas a partir de um dado ponto de
	 *            {@link #setReference(Coordinate) referência}
	 */
	public CoordinateTTIinput(GeoCoordinate coordonnees, boolean azimutais, boolean relativas) {
		this.azimutais = azimutais;
		this.relativas = relativas;

		Font courierNew = new Font("Courier New", Font.PLAIN, 15);

		this.mainField = new JTextField();
		this.mainField.setHorizontalAlignment(JTextField.CENTER);
		this.mainField.setFont(courierNew);
		this.mainField.setEditable(false);
		super.add(mainField, 0, 0, 2, 1);

		this.latitudeInput = new AngleTTIinput(true);
		this.latitudeInput.setColumns(9);
		this.latitudeInput.setFont(courierNew);
		this.latitudeInput.getDocument().addDocumentListener(this);
		super.add(latitudeInput, 0, 1, 1, 1);

		this.longitudeInput = new AngleTTIinput(false);
		this.longitudeInput.setColumns(9);
		this.longitudeInput.setFont(courierNew);
		this.longitudeInput.getDocument().addDocumentListener(this);
		super.add(longitudeInput, 1, 1, 1, 1);

		if (this.azimutais) {
			this.distanceInput = new MedidaInput(0., 3, Grandeza.COMPRIMENTO);
			this.distanceInput.setColumns(7);
			this.distanceInput.setFont(courierNew);
			this.distanceInput.addDocumentListener(this);
			super.add(distanceInput, 0, 2, 1, 1);

			this.azimutInput = new MedidaInput(0., 0, Grandeza.ANGULO);
			this.azimutInput.setColumns(7);
			this.azimutInput.setFont(courierNew);
			this.azimutInput.addDocumentListener(this);
			super.add(azimutInput, 1, 2, 1, 1);
		}

		if (this.relativas) {
			bg = new ButtonGroup();
			coordAbs = new JRadioButton("absolutas");
			coordAbs.setSelected(true);
			coordAbs.addActionListener(this);
			bg.add(coordAbs);
			super.add(coordAbs, 2, 0, 1, 1);

			coordRel = new JRadioButton("relativas");
			coordRel.setEnabled(false);
			coordRel.addActionListener(this);
			bg.add(coordRel);
			super.add(coordRel, 2, 1, 1, 1);
		}

		this.set(coordonnees);
	}

	@Override
	public GeoCoordinate get() {
		return new GeoCoordinate(this.longitudeInput.get(), this.latitudeInput.get());
	}

	@Override
	public void set(GeoCoordinate coordinates) {
		if (coordinates != null) {
			this.mainField.setText(coordinates.toStringEdition());

			if (this.relativas) {
				this.latitudeInput.set(coordinates.y - this.getReference().y);
				this.longitudeInput.set(coordinates.x - this.getReference().x);
			} else {
				this.latitudeInput.set(coordinates.y);
				this.longitudeInput.set(coordinates.x);
			}

			if (this.azimutais) {
				double distance = this.getReference().getDistance(coordinates);
				int azimut = (int) this.getReference().getAzimut(coordinates);

				this.distanceInput.set(distance);
				this.azimutInput.set((double) azimut);
			}
		} else
			clear();
		this.refreshRadioButtons();
	}

	public void clear() {
		this.mainField.setText("");

		this.latitudeInput.set(Float.NaN);
		this.longitudeInput.set(Float.NaN);

		if (this.azimutais) {
			this.distanceInput.set((Double) null);
			this.azimutInput.set((Double) null);
		}
	}

	@Override
	public Component getComponent() {
		return this;
	}

	private void refresh() {
		this.mainField.setText(get().toStringEdition());
	}

	// ------------------- REFERÊNCIA -------------------

	public void setReference(GeoCoordinate referencial) {
		this.referencial = referencial;
		refreshRadioButtons();
	}

	protected GeoCoordinate getReference() {
		GeoCoordinate c = null;

		if (this.coordAbs.isSelected())
			c = new GeoCoordinate(0f, 0f);
		else if (this.coordRel.isSelected())
			c = this.referencial;

		return c;
	}

	protected void refreshRadioButtons() {
		if (this.coordRel != null)
			this.coordRel.setEnabled(this.referencial != null);
	}

	// ------------------- LISTENER -------------------

	@Override
	public void actionPerformed(ActionEvent event) {
		// TODO
	}

	@Override
	public void insertUpdate(DocumentEvent event) {
		refresh();
	}

	@Override
	public void removeUpdate(DocumentEvent event) {
		refresh();
	}

	@Override
	public void changedUpdate(DocumentEvent event) {
		// Plain text components do not fire these events
	}
}