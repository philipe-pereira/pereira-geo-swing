package br.com.pereiraeng.geo.swing.obj;

import java.awt.Color;

import br.com.pereiraeng.geo.swing.objetos.GeoStringMarkD;

public class Frontier extends GeoStringMarkD {
	private static final long serialVersionUID = 1L;

	@Override
	public boolean isLoop() {
		return true;
	}

	@Override
	protected Color getColor() {
		return Color.DARK_GRAY;
	}
}