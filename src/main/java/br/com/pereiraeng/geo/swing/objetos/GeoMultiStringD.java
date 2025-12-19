package br.com.pereiraeng.geo.swing.objetos;

import java.awt.Graphics2D;

import br.com.pereiraeng.geo.objetos.GeoMultiString;
import br.com.pereiraeng.swing.interfaces.WL;

public class GeoMultiStringD<K> extends GeoMultiString<GeoStringD> implements GeoD {
	private static final long serialVersionUID = 8540605278013178434L;

	// -------------------------------- DRAWER --------------------------------

	@Override
	public void setWL(WL wl) {
		for (GeoStringD gs : this)
			gs.setWL(wl);
	}

	@Override
	public void drawObject(Graphics2D g) {
		drawGeo(g, -1, -1);
	}

	@Override
	public void drawGeo(Graphics2D g, int x, int y) {
		for (GeoStringD gs : this)
			gs.drawObject(g);
	}

	@Override
	public boolean isDrawable() {
		return true;
	}

	@Override
	public boolean wasDrawn() {
		return true;
	}

	@Override
	public void setDrawable(boolean drawable) {
	}
}