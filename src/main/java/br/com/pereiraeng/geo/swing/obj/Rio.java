package br.com.pereiraeng.geo.swing.obj;

import br.com.pereiraeng.geo.swing.objetos.GeoMultiStringD;

/**
 * Objeto geográfico obtido pela união de vários {@link Curso cursos
 * hidrográficos}, formando assim um rio.
 * 
 * @author Philipe PEREIRA
 *
 */
public class Rio extends GeoMultiStringD<Curso> {

	private static final long serialVersionUID = 1L;

	public Rio(String name) {
		super.setName(name);
	}
}
