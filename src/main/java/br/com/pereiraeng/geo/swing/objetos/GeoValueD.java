package br.com.pereiraeng.geo.swing.objetos;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

import br.com.pereiraeng.geo.objetos.GeoValue;
import br.com.pereiraeng.swing.LeafOM;
import br.com.pereiraeng.swing.interfaces.WL;

public class GeoValueD extends GeoValue implements GeoD {
	private static final long serialVersionUID = 6698166510393024801L;

	// ------------------------------- DRAWER -------------------------------

	protected transient WL wl;

	@Override
	public void setWL(WL wl) {
		this.wl = wl;
	}

	@Override
	public boolean isDrawable() {
		return true;
	}

	@Override
	public void setDrawable(boolean drawable) {
	}

	@Override
	public boolean wasDrawn() {
		return true;
	}

	@Override
	public void drawObject(Graphics2D g) {
		Point p = LeafOM.getTranformedPoint(x, y, wl);
		drawGeo(g, p.x, p.y);
	}

	@Override
	public void drawGeo(Graphics2D g, int x, int y) {
		g.setColor(Color.GREEN);
		g.drawLine(x - 2, y, x + 2, y);
		g.drawLine(x, y - 2, x, y + 2);

		String s = super.getValueString();
		if (s == null || priorName) {
			if (super.getName() != null)
				s = super.getName();
			else
				s = String.valueOf(super.getId());
		}
		g.drawString(s, x, y);
	}

	private transient boolean priorName = false;

	/**
	 * Função que dá preferência para o {@link #getName() nome} do ponto ao invés do
	 * seu {@link #getValueString() valor} ao ser mostrado no mapa
	 * 
	 * @param priorName <code>true</code> para o nome, <code>false</code> para o
	 *                  valor
	 */
	public void setPriorName(boolean priorName) {
		this.priorName = priorName;
	}

}
