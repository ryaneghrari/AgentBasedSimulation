public class Cell
{
	private Macrophage macrophage;
	private Bacteria bacteria;

	private int row;
	private int col;

	
	public Cell(int x, int y)
	{
		macrophage = null;
		bacteria = null;

		row = x;
		col = y;
	}


	public void occupy(Macrophage m)
	{
		macrophage = m;
		m.setRowCol(row, col);
	}

	public void occupy(Bacteria b)
	{
		bacteria = b;
		b.setRowCol(row, col);
	}

	public void removeMacrophage()
	{
		//macrophage.setRowCol(-1,-1);
		macrophage = null;
	}

	public void removeBacteria()
	{
		//bacteria.setRowCol(-1,-1);
		bacteria = null;
	}

	public int getRow()
	{
		return row;
	}

	public int getCol()
	{
		return col;
	}

	public Macrophage getMacrophage()
	{
		return macrophage;
	}

	public Bacteria getBacteria()
	{
		return bacteria;
	}

	public boolean isOccupied()
	{
		if(macrophage != null || bacteria != null)
		{
			return true;
		}
		return false;
	}

	public boolean hasMacrophage()
	{
		if(macrophage != null)
		{
			return true;
		}
		return false;
	}

	public boolean hasBacteria()
	{
		if(bacteria != null)
		{
			return true;
		}
		return false;
	}
}
