package br.com.pereiraeng.geo.swing.objetos;

import java.awt.Color;
import java.awt.Graphics2D;

import br.com.pereiraeng.geo.objetos.GeoStringMark;
import br.com.pereiraeng.swing.interfaces.WL;

public abstract class GeoStringMarkD extends GeoStringMark implements GeoD {
	private static final long serialVersionUID = -8570152060017674768L;

	// ------------------------------- DRAWER -------------------------------

	protected transient WL wl;

	@Override
	public void setWL(WL wl) {
		this.wl = wl;
	}

	@Override
	public void drawObject(Graphics2D g) {
		drawGeo(g, -1, -1);
	}

	@Override
	public void drawGeo(Graphics2D g, int x, int y) {
		GeoStringD.drawString(g, this, wl, getColor(), isLoop(), toString());
	}

	protected abstract Color getColor();

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
