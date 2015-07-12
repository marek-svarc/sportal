package com.clubeek.model;


import com.clubeek.dao.CategoryDao;
import com.clubeek.dao.impl.ownframework.CategoryDaoImpl;
import com.clubeek.dao.impl.ownframework.rep.RepCategory;
import com.clubeek.enums.SportType;

public class ClubTeam extends Model {
    // TODO vitfo, created on 11. 6. 2015 
    private CategoryDao categoryDao = new CategoryDaoImpl();

	/* PRIVATE */

	/** nazev tymu */
	private String name = ""; //$NON-NLS-1$
	
	/** Type of sport */
	private SportType sportType;

	/** priznak zda je tym aktivni */
	private Boolean active = true;

	/** identifikator kategorie do ktere je tym zarazen */
	private Integer categoryId;
	
	/** Id of the club the team belongs. It cannot be null. */
	private int clubId;

	/** data kategorie do ktere je tym zarazen */
	private Category category;

	/** cislo urcujici prioritu zobrazeni (vyssi cislo => vetsi priorita) */
	private int sorting = 0;

	/* PUBLIC */

	// Vlastnosti

	/** Vraci Nazev tymu */
	public String getName() {
		return name;
	}

	/** Nastavi Nazev tymu */
	public void setName(String name) {
		this.name = name;
	}
	
	public SportType getSportType() {
        return sportType;
    }

    public void setSportType(SportType sportType) {
        this.sportType = sportType;
    }

    /** Vraci priznak zda je tym aktivni */
	public Boolean getActive() {
		return active;
	}

	/** Nstavi priznak zda je tym aktivni */
	public void setActive(Boolean active) {
		this.active = active;
	}

	/** Vraci identifikator kategore ke ktere tym patri */
	public Integer getCategoryId() {
		return categoryId;
	}

	/** Nastavi identifikator kategore ke ktere tym patri */
	public void setCategoryId(Integer categoryId) {
		if (this.categoryId != categoryId) {
			this.category = null;
			this.categoryId = categoryId;
		}
	}

	/** Vraci kategorii ke ktere tym patri 
	 * 
         */
	public Category getCategory() {
		if (category == null)
//			category = RepCategory.selectById(categoryId, null);
		    // TODO vitfo, created on 11. 6. 2015 - model POJO cannot have such functionality
		    category = categoryDao.getCategory(categoryId);
		return category;
	}

	/** Vraci prioritu zobrazeni */
	public int getSorting() {
		return sorting;
	}

	/** Nastavi prioritu zobrazeni */
	public void setSorting(int sorting) {
		this.sorting = sorting;
	}

	@Override
	public String toString() {
		return name;
	}

    public int getClubId() {
        return clubId;
    }

    public void setClubId(int clubId) {
        this.clubId = clubId;
    }
}
