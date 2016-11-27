/*
 * Program SimpleShopApplication
 * Autor: Miko³aj Brukiewicz
 * Zajecia: Jezyki Programowania, Sroda TP 11:15 - 13:00
 * Indeks: 225954
 * 	Data: 30 pazdziernika 2016
 */

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Ta klasa reprezentuje sklep
 * <br>
 * 
 * Umozliwia ona operacje zwiazane z obsluga towarow oraz uzytkownikow:
 * <ul>
 * 	<li> Dodawanie nowego towaru lub klienta
 * 	<li> Usuwanie towaru lub klienta
 * 	<li> Sprzedaz towaru klientowi
 * 	<li> Zapis kont uzytkownikow oraz towarow dostepnych w sklepie do pliku
 * </ul>
 */
public class Shop implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * Jest to tablica przechowujaca konta wszystkich klientow sklepu
	 */
	private ArrayList<Client> listOfClients = new ArrayList<Client>();
	
	/**
	 * Jest to tablica przechowujaca wszystkie przedmioty dostepne w sklepie
	 */
	private ArrayList<Items> listOfItems = new ArrayList<Items>();
	
	/**
	 * Jest to tablica przechowujaca informacje o koncie admina
	 */
	private ArrayList<Client> adminInfo = new ArrayList<Client>();
	
	/**
	 * Jest to tablica przechowujaca informacje o przedmiotach ktore zostaly zakupione przez klientow
	 */
	private ArrayList<Items> earnings = new ArrayList<Items>();
	
	/**
	 * Metoda pozwalajaca na dodanie nowego przedmiotu do oferty sklepu.
	 * Kiedy nowy typ towaru zostaje utworzony, metoda dodaje go do tablicy przedmiotow
	 * 
	 * @param item_name Nazwa reprezentujaca nowy typ towaru
	 * @return Nowo utworzony typ towaru
	 * @throws Exception Wyjatek zwracany kiedy: <ul><li>Nie zostala podana nazwa towaru</li><li>Towar o takiej nazwie juz istnieje</li></ul>
	 */
	public Items addNewItem(String item_name) throws Exception{
		if(item_name == null || item_name.equals("")) throw new Exception("Musisz podac nazwe przedmiotu");
		if(findItem(item_name) != null) throw new Exception("Taki przedmiot juz jest w magazynie");
		
		Items newItem = new Items(item_name);
		listOfItems.add(newItem);
		return newItem;
	}
	
	/**
	 * Metoda dodajaca informacje o zakupionym przez klienta przedmiocie do listy zyskow sklepu
	 * 
	 * @param item_name Nazwa zakupionego przedmiotu
	 * @param number Ilosc zakupionych przedmiotow
	 * @param price Cena zakupionego przedmiotu
	 * @param buyer Login osoby zakupujacej
	 * 
	 * @return Zakupiony przedmiot
	 */
	public Items addToProfitList(String item_name, int number, double price, String buyer) throws Exception
	{
		Items newItem = new Items(item_name);
		newItem.setBuyer(buyer);
		newItem.setPrice(price);
		newItem.addToStock(number);
		earnings.add(newItem);
		return newItem;
	}
	
	/**
	 * Metoda ktora pozwala nie usuniecie przedmiotu z oferty sklepu poprzez usuniecie odpowiedniej pozycji w tablicy
	 * 
	 * @param item Nazwa towaru ktory ma zostac usuniety
	 * @throws Exception Wyjatek zglaszany kiedy wpisana nazwa nie odpowiada zadnemu rodzajowi towaru
	 */
	public void removeItem(Items item) throws Exception{
		if(findItem(item.getName()) == null) throw new Exception("Nie ma takiego przedmiotu");
		listOfItems.remove(item);
	}
	
	/**
	 * Metoda ktorej zadaniem jest sprzedaz konkretnego przedmiotu.
	 * Ma ona wplyw na:
	 * <ul>
	 *  <li> Stan magazynu sklepu
	 *  <li> Saldo klienta
	 * </ul>
	 * 
	 * @param item Przedmiot ktory ma zostac sprzedany
	 * @param buyer Konto kupca
	 * @param number Ilosc przedmiotow ktore kupiec chce nabyc
	 * @throws Exception Wyjatek zglaszany kiedy: <ul><li>Sklep nie posiada danego typu towaru</li><li>Nie ma takiej ilosci przedmiotow w magazynie</li><li>Kupiec nie posiada wystarczajaco duzo pieniedzy</li></ul>
	 */
	public void buyItem(Items item, Client buyer, int number) throws Exception{
		if(findItem(item.getName()) == null) throw new Exception("Nie ma takiego alkoholu w magazynie");
		if(item.getStock() < number) throw new Exception("Nie ma takiej ilosci wybranego alkoholu w magazynie");
		if(buyer.getMoney() < item.getPrice()) throw new Exception("Nie posiadasz wystarczajacych srodkow");
		buyer.loseMoney((item.getPrice() * number));
	}
	
	/**
	 * Metoda tworzaca konto nowego klienta
	 * Kiedy nowe konto zostaje utworzone, metoda dodaje je do tablicy klientow
	 * 
	 * @param account_login Logina konta
	 * @return Nowe konto
	 * @throws Exception Wyjatek sglaszany w przypadku pozostawienia pustego pola na login lub wpisania juz istniejacego loginu
	 */
	public Client makeNewAccount(String account_login) throws Exception{
		if(account_login == null || account_login.equals("")) throw new Exception("Nazwa konta nie moze byc pusta");
		if(findAccount(account_login) != null) throw new Exception("Uzytkownik o takiej nazwie juz istnieje");
		
		Client newUser = new Client(account_login);
		listOfClients.add(newUser);
		return newUser;
	}
	
	/**
	 * Zadaniem tej metody jest dodanie konta admina w przypadku pierwszego uruchomienia programu
	 * 
	 * @param person Konto admina
	 */
	public void manualAddClient(Client person)
	{
		adminInfo.add(person);
	}

	/**
	 * metoda pozwalajaca na usuniecie wybranego konta
	 * 
	 * @param user Konto ktore ma zostac usuniete
	 * @throws Exception Wyjatek zwracany kiedy podane konto nie istnieje
	 */
	public void deleteAccount(Client user) throws Exception{
		if(findAccount(user.getLogin()) == null) throw new Exception("Nie ma takiego uzytkownika");
		listOfClients.remove(user);
	}
	
	/**
	 * Metoda ta szuka za pomoca petli, czy konto o wprowadzonym loginie juz istnieje
	 * Istnieje dodatkowa petla ktora sprawdza czy zostalo juz utworzone konto admina
	 * 
	 * @param name_of_user Login szukanego konta
	 * @return Jezeli konto zostalo znalezione, funkcja je zwraca. Je¿eli nie zostalo znalenione, funkcjia zwraca null
	 */
	public Client findAccount(String name_of_user)
	{
		if(name_of_user.equals("admin"))
		{
			for(Client x : adminInfo)
			{
				if(x.getLogin().equals(name_of_user)) return x;
			}
			return null;
		}else
		{
		for(Client x : listOfClients)
		{
			if(x.getLogin().equals(name_of_user)) return x;
		}
		return null;
		}
	}
	
	/**
	 * Metoda ta szuka za pomoca petli, czy towar o wprowadzonej nazwie juz istnieje
	 * 
	 * @param item_name Nazwa szukanego przedmiotu
	 * @return Jezeli przedmiot zostal znaleziony, funkcja go zwraca. Je¿eli nie zostal znaleniony, funkcjia zwraca null
	 */
	public Items findItem(String item_name)
	{
		for(Items new_item : listOfItems)
		{
			if(new_item.getName().equals(item_name)) return new_item;
		}
		return null;
	}
	
	/**
	 * Metoda wypisuje jako lancuch znakow informacje o wszystkich klientach sklepu
	 * 
	 * @return Utworzony lancuch
	 */
	public String listAccounts(){
		StringBuilder sb = new StringBuilder();
		int n = 0;
		for (Client client : listOfClients){
			if (n++ != 0) sb.append("\n");		
			sb.append(client.toString());
		}
		return sb.toString();
	}
	
	/**
	 * Metoda oblicza laczny profit sklepu ze sprzedazy
	 * 
	 * @return Wynik dodawania
	 */
	public double allEarnings(){
		double amount = 0;
		for (Items item : earnings){
			amount += (item.getPrice()*item.getStock());
		}
		return amount;
	}
	
	/**
	 * Metoda wypisuje jako lancuch znakow informacje o wszystkich towarach dostepnych w sklepie
	 * 
	 * @return Utworzony lancuch
	 */
	public String listItems(){
		StringBuilder sb = new StringBuilder();
		int n = 0;
		for (Items x : listOfItems){
			if (n++ != 0) sb.append("\n");		
			sb.append(x.toString());
		}
		return sb.toString();
	}
	
	/**
	 * Metoda wypisuje jako lancuch znakow informacje o wszystkich przedmiotach zakupionych przez klientow
	 * 
	 * @return Utworzony lancuch
	 */
	public String listEarnings(){
		StringBuilder sb = new StringBuilder();
		int n = 0;
		for (Items x : earnings){
			if (n++ != 0) sb.append("\n");		
			sb.append(x.toString());
			sb.append(" zakupione przez " + x.getBuyer());
		}
		sb.append("\nLacznie sklep zarobil "+ allEarnings()+ " zl");
		return sb.toString();
	}
	
	/**
	 * Metoda ktora czysci cala tablice zyskow
	 */
	public void clearEarningsList(){
		earnings.clear();
	}
	
	/**
	 * Metoda zapisujaca tablice klientow do pliku
	 * 
	 * @param fileName Nazwa pliku
	 */
	void saveClientListToFile(String fileName) throws Exception {
		ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName));
		out.writeObject(listOfClients);
		out.close();
	}
	
	/**
	 * Metoda zapisujaca tablice zyskow do pliku
	 * 
	 * @param fileName Nazwa pliku
	 */
	void saveEarningListToFile(String fileName) throws Exception {
		ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName));
		out.writeObject(earnings);
		out.close();
	}
	
	/**
	 * Metoda wczytujaca tablice klientow z pliku
	 * 
	 * @param fileName Nazwa pliku
	 */
	void loadClientListFromFile(String fileName) throws Exception {
		ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName));
		listOfClients = (ArrayList<Client>)in.readObject();
		in.close();
	}
	
	/**
	 * Metoda wczytujaca tablice zyskow z pliku
	 * 
	 * @param fileName Nazwa pliku
	 */
	void loadEarningListFromFile(String fileName) throws Exception {
		ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName));
		earnings = (ArrayList<Items>)in.readObject();
		in.close();
	}
	
	/**
	 * Metoda zapisujaca tablice z kontem admina do pliku
	 * 
	 * @param adminFileName Nazwa pliku
	 */
	void saveAdminInfoToFile(String adminFileName) throws Exception {
		ObjectOutputStream out2 = new ObjectOutputStream(new FileOutputStream(adminFileName));
		out2.writeObject(adminInfo);
		out2.close();
	}
	
	/**
	 * Metoda wczytujaca tablice z kontem admina z pliku
	 * 
	 * @param adminFileName Nazwa pliku
	 */
	void loadAdminInfoFromFile(String adminFileName) throws Exception {
		ObjectInputStream in2 = new ObjectInputStream(new FileInputStream(adminFileName));
		adminInfo = (ArrayList<Client>)in2.readObject();
		in2.close();
	}
	
	/**
	 * Metoda zapisujaca tablice przedmiotow do pliku
	 * 
	 * @param fileName Nazwa pliku
	 */
	void saveItemsListToFile(String fileName) throws Exception {
		ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName));
		out.writeObject(listOfItems);
		out.close();
	}
	
	/**
	 * Metoda wczytujaca tablice przedmiotow z pliku
	 * 
	 * @param fileName Nazwa pliku
	 */
	void loadItemsListFromFile(String fileName) throws Exception {
		ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName));
		listOfItems = (ArrayList<Items>)in.readObject();
		in.close();
	}

}
