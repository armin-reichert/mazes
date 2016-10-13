package de.amr.easy.grid.api;

/**
 * Interface for accessing content stored in a grid.
 *
 * @param <Content>
 *          grid cell content type
 */
public interface GridDataAccess<Content> {

	/**
	 * Returns the content of the given cell.
	 * 
	 * @param cell
	 *          a cell
	 * @return the content or the default content if no content has been set for this cell
	 */
	public Content get(Integer cell);

	/**
	 * Sets the content of the given cell.
	 * 
	 * @param cell
	 *          a cell
	 * @param data
	 *          the cell content
	 */
	public void set(Integer cell, Content data);

	/**
	 * Clears the complete grid content.
	 */
	public void clear();

	/**
	 * Sets the default content for the cells
	 * 
	 * @param data
	 *          the default content
	 */
	public void setDefault(Content data);
}