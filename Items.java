/*
 * Program SimpleShopApplication
 * Autor: Miko³aj Brukiewicz
 * Zajecia: Jezyki Programowania, Sroda TP 11:15 - 13:00
 * Indeks: 225954
 * 	Data: 30 pazdziernika 2016
 */

import java.io.Serializable;

/**
 * Ta klasa reprezentuje konkretny rodzaj towaru dostepnego w sklepie
 * <br>
 * 
 * Przechowuje ona informacje o towarze:
 * <ul>
 * 	<li> Nazwa towaru
 * 	<li> Cena towaru
 *  <li> Ilosc towaru w magazynie
 * </ul>
 * <br>
 * Oraz umozliwia podstawowe operacje zwiazane z tym towarem:
 * <ul>
 * 	<li> Ustalanie ceny towaru
 * 	<li> Dodawanie okreslonej ilosci towaru do magazynu
 * 	<li> Sprzedaz towaru klientowi
 * </ul>
 */
public class Items implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * Nazwa towaru
	 */
	private String item_name;
	
	/**
	 * Cena towaru
	 */
	private double price;
	
	/**
	 * Ilosc towaru w magazynie
	 */
	private int in_stock;
	
	/**
	 * Zmienna przechowujaca login uzytkownika ktory zakupil towar
	 */
	private String who_bought;
	
	/**
	 * Konstruktor klasy Items.
	 * 
	 * @param name Przypisuje nazwe unikatowa dla tego rodzaju przedmiotow
	 */
	Items(String name){
		item_name=name;
		price=0;
		in_stock=0;
	}
	
	/**
	 * Metoda zwraca nazwe towaru
	 * 
	 * @return Nazwa
	 */
	public String getName(){
		return item_name;
	}
	
	/**
	 * Metoda zrwaca login osoby ktora kupila dany towar
	 * 
	 * @return Kupiec
	 */
	public String getBuyer(){
		return who_bought;
	}
	
	/**
	 * Metoda zrwaca cene towaru
	 * 
	 * @return Cena
	 */
	public double getPrice(){
		return price;
	}
	
	/**
	 * Metoda zwraca dostepna ilosc tego rodzaju przedmiotow w magazynie
	 * 
	 * @return Ilosc przedmiotow
	 */
	public int getStock(){
		return in_stock;
	}
	
	/**
	 * Metoda pozwalajaca na zmiane lub ustalanie ceny towaru
	 * 
	 * @param price Nowa cena
	 * @throws Exception Wyjate zglaszany kiedy podana cena jest mniejsza od zera
	 */
	public void setPrice(double price) throws Exception{
		if(price<=0) throw new Exception("Cena musi byc wieksza od zera");
		this.price=price;
	}
	
	/**
	 * Metoda przypusuje zmiennej who_bought login osoby ktora dokonala zakupu
	 * 
	 * @param buyer Kupiec
	 */
	public void setBuyer(String buyer)
	{
		this.who_bought = buyer;
	}
	
	/**
	 * Metoda pozwalajaca na dodawanie do magazynu okreslonej ilosci przedmiotow
	 * 
	 * @param items Ilosc przedmiotow ktore maja zostac dodane do magazynu
	 * @throws Exception Wyjatek zglaszany kiedy podana liczba jest mniejsza od zera
	 */
	public void addToStock(int items) throws Exception{
		if(items<=0) throw new Exception("Ilosc przedmiotow ktore chcesz dodac do magazynu musi byc wieksza od zera");
		in_stock=items;
	}
	
	/**
	 * Metoda umozliwiajaca sprzedaz okreslonej ilosci tego rodzaju przedmiotow
	 * 
	 * @param number Ilosc przedmiotow ktora ma zostac sprzedana
	 * @throws Exception Wyjatki sa zglaszane kiedy: <ul><li>Podana ilosc jest mniejsza od zera</li><li>Nie ma takiej ilosci przedmiotow w magazynie</li></ul>
	 */
	public void sellItem(int number) throws Exception{
		if(number<=0) throw new Exception("Ilosc alkoholu która chcesz zakupic musi byc wieksza od zera");
		if(number>in_stock) throw new Exception("Nie ma takiej ilosci tego przedmiotu w magazynie");
		in_stock-=number;
	}
	
	/**
	 * Metoda wyswietla informacje dotyczace tego przedmiotu:
	 * <ul>
	 *  <li> Ilosc przedmiotow w magazynie
	 *  <li> Nazwa przedmiotu
	 *  <li> Cena przedmiotu
	 * </ul>
	 * 
	 * @return Informaje o towarze
	 */
	public String toString(){
		return String.format("%dx  %s  <%.2f zl>",in_stock, item_name, price);
	}

}
