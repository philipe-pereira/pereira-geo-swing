package br.com.pereiraeng.geo.swing.obj;

import br.com.pereiraeng.geo.swing.objetos.GeoMultiStringD;

/**
 * Objeto geográfico obtido pela união de vários {@link TrechoVia trechos},
 * formando assim uma via.
 * 
 * @author Philipe PEREIRA
 *
 */
public class Via extends GeoMultiStringD<TrechoVia> {
	private static final long serialVersionUID = 1L;

	public Via(String name) {
		super.setName(name);
	}
}
