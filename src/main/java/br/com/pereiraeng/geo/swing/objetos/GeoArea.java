package br.com.pereiraeng.geo.swing.objetos;

import java.awt.Color;

public class GeoArea extends GeoStringsD {
	private static final long serialVersionUID = 1L;

	private Color color;

	public GeoArea() {
		this(Color.WHITE);
	}

	public GeoArea(Color color) {
		this.setColor(color);
	}

	public void setColor(Color color) {
		this.color = color;
	}

	@Override
	protected Color getColor() {
		return color;
	}
}
