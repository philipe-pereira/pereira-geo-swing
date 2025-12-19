package br.com.pereiraeng.geo.swing.objetos;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.List;

import br.com.pereiraeng.geo.GeoCoordinate;
import br.com.pereiraeng.geo.objetos.GeoStrings;
import br.com.pereiraeng.swing.interfaces.WL;

public abstract class GeoStringsD extends GeoStrings implements GeoD {
	private static final long serialVersionUID = 6763945611461175550L;

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
		int loopIndex = 0;
		for (List<GeoCoordinate> cs : this) {
			super.setPos(loopIndex);
			GeoStringD.drawString(g, cs, wl, getColor(), super.isLoop(), toString());
			loopIndex++;
		}
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
