/*
 * Program SimpleShopApplication
 * Autor: Miko³aj Brukiewicz
 * Zajecia: Jezyki Programowania, Sroda TP 11:15 - 13:00
 * Indeks: 225954
 * 	Data: 30 pazdziernika 2016
 */

import java.io.Serializable;

/**
 * Ta klasa reprezentuje konto klienta w sklepie
 * <br>
 * 
 * Przechowuje ona najwazniejsze informacje o uzytkowniku bedacym klientem:
 * <ul>
 * 	<li> Login
 * 	<li> Haslo
 *  <li> Nazwe uzytkownika
 *  <li> Ilosc pieniedzy na koncie
 * </ul>
 * <br>
 * Oraz umozliwia podstawowe operacje zwiazane z kontem uzytkownika:
 * <ul>
 * 	<li> Zmiane nazwy uzytkownika
 * 	<li> Zmiane hasla
 * 	<li> Operacje zwiazane z saldem
 * </ul>
 */
public class Client implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * Login uzytkownika
	 * <p><b>Uwaga:</b> Login nie moze byc zmieniany! </p>
	 */
	private String login;
	
	/**
	 * Nazwa uzytkownika
	 */
	private String name;
	
	/**
	 * Haslo do konta
	 */
	private long password;
	
	/**
	 * Dostepne saldo na koncie
	 */
	private double money;
	
	/**
	 * 
	 * Konstruktor klasy Client.
	 * 
	 * @param login Przypisuje login unikatowy dla konkretnego klienta
	 */
	Client(String login)
	{
		this.login = login;
		password = 0;
		money = 0;
		name = "anonim";
	}
	
	/**
	 * Metoda zwracajaca nazwe uzytkownika
	 * 
	 * @return Nazwa
	 */
	public String getName()
	{
		return name;
	}
	
	/**
	 * Metoda zwracajaca login uzytkownika
	 * 
	 * @return Login
	 */
	public String getLogin()
	{
		return login;
	}
	
	/**
	 * Metoda zwracajaca stan konta uzytkownika
	 * 
	 * @return Ile pieniedzy posiada uzytkownik
	 */
	public double getMoney()
	{
		return money;
	}
	
	/**
	 * Metoda ktorej zadaniem jest zmniejszanie ilosci pieniedzy na koncie w przypadku dokonania zakupu
	 * 
	 * @param how_much Cena kupionego przedmiotu
	 * @throws Exception Zwraca wyjatek jezeli uzytkownik nie posiada wystarczajacych srodkow na zakup
	 */
	public void loseMoney(double how_much) throws Exception{
		if(how_much > money) throw new Exception("Nie posiadasz wystarczaj¹cych œrodków");
		money -= how_much;
	}
	
	/**
	 * Metoda sprawdzajaca zgodnosc hasla podczas logowania lub zmiany hasla
	 * 
	 * @param pass Haslo wpisane przez uzytkownika
	 * @return Sprawdzenie czy hasla sie zgadzaja
	 */
	public boolean checkPassword(String pass)
	{
		if(pass == null) return false;
		return pass.hashCode() == password;
	}
	
	/**
	 * Metoda zmieniajaca haslo uzytkownika
	 * 
	 * @param oldPassword Stare haslo
	 * @param newPassword Nowe haslo
	 * @throws Exception Zwraca wyjatek jezeli uzytkownik wprowadzil bledne haslo
	 */
	public void setPassword(String oldPassword, String newPassword) throws Exception {
		if (!checkPassword(oldPassword)) throw new Exception("Bledne haslo");
		password = newPassword.hashCode(); 
	}
	
	/**
	 * Metoda pozwalajaca na przypisanie lub zmiane nazwy uzytkownika
	 * @param name Nowa nazwa
	 */
	public void setName(String name)
	{
		this.name = name;
	}
	
	/**
	 * Metoda pozwalajaca na dodanie pieniedzy do konta uzytkownika
	 * 
	 * @param new_money Ilosc pieniedzy jaka chcemy dodac
	 * @throws Exception Wyjatek zglaszany kiedy podana kwota jest ujemna
	 */
	public void addMoney(double new_money) throws Exception{
		if(new_money<=0) throw new Exception("Kwota musi byc wieksza od zera");
		money+=new_money;
	}

	/**
	 * Metoda ktora wypisuje podstawowe informacje na temat konta klienta do lancucha znakow
	 * @return Informacje o koncie
	 */
	public String toString(){
		return String.format("	%s <%s>", name, login);
	}
	
	/**
	 * Metoda wyswietla uzytkownikowi informacje na temat:
	 * <ul>
	 *  <li> Dostepnych srodkow na koncie
	 *  <li> Nazwy uzytkownika
	 * </ul>
	 * 
	 * @return Informaje o koncie
	 */
	public String toStringInfo(){
		return String.format("Konto u¿ytkownika:	%s\nDostêpne œrodki: %.2f z³", name, money);
	}
}
