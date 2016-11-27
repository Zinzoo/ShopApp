/*
 * Program SimpleShopApplication
 * Autor: Miko³aj Brukiewicz
 * Zajecia: Jezyki Programowania, Sroda TP 11:15 - 13:00
 * Indeks: 225954
 * 	Data: 30 pazdziernika 2016
 */

/**
 * Ta klasa reprezentuje aplikacje sterujaca dzialaniem sklepu oraz zawierajaca w sobie metode main
 * <br>
 * 
 * Przechowuje ona teksty bedace:
 * <ul>
 * 	<li> Wiadomosc powitalna
 * 	<li> Menu glownym
 * 	<li> Menu konta klienta
 *  <li> Menu konta admina
 *  <li> Menu edycji magazynu
 *  <li> Menu listy zyskow
 * </ul>
 * <br>
 * Oraz steruje dzialaniem calej aplikacji poprzez:
 * <ul>
 * 	<li> Zapisywanie i wczytywanie list klientow, przedmiotow oraz admina
 * 	<li> Operowanie na petlach sterujacych poruszaniem sie po menu
 * 	<li> Komunikacje z uzytkownikiem poprzez wyswietlanie komunikatow w zaleznosci od wykonywanej czynnosci
 * </ul>
 */
public class SimpleShopApplication {
	
	public static void main(String[] args){
		new SimpleShopApplication();
	}


	//private UserDialog UI = new ConsoleUserDialog();
	private UserDialog UI = new JOptionPaneUserDialog();
	

	/**
	 * Lancuch znakow bedacy wiadomoscia powitalna
	 */
	private static final String GREETING_MESSAGE =
			"Program Simple Shop\n" +
			"Autor: Miko³aj Brukiewicz\n" +
			"Data: 21 paziernika 2016 r.\n";

	/**
	 * Lancuch znakow reprezentujacy menu glowne
	 */
	private static final String SHOP_MENU = 
			"Sklep Alkoholowy Promil - M E N U   G L O W N E      \n" +
			"1 - Utwórz nowe konto              \n" +
			"2 - Zaloguj siê do konta           \n" + 
			"3 - Zaloguj sie jako sprzedawca	\n" +
			"0 - Zakoncz program                \n";		
	
	/**
	 * Lancuch znakow reprezentujacy menu konta klienta
	 */
	private static final String ACCOUNT_MENU =
			"1 - Wplac na konto             \n" +
			"2 - Informacje o tym koncie    \n" +
			"3 - Zmien haslo                \n" +
			"4 - Zmien nazwisko wlasciciela \n" +
			"5 - Usun konto                 \n" +
			"6 - Przegladaj dostepne alkohole \n" +
			"7 - Zakup wybrany alkohol      \n" +
			"0 - Wyloguj sie z konta        \n";
	
	/**
	 * Lancuch znakow reprezentujacy menu konta admina
	 */
	private static final String OWNER_MENU =
			"Jestes zalogowany jako sprzedawca     \n\n" +
			"1 - Dodaj nowy towar do magazynu        \n" +
			"2 - Wyswietl towary w magazynie         \n" +
			"3 - Edycja towarów w magazynie          \n" +
			"4 - Sprawdz utarg                       \n" +
			"5 - Wyswietl wszystkich klientów        \n" +
			"6 - Zmien haslo                         \n" +
			"0 - Wyloguj sie z konta                 \n";

	/**
	 * Lancuch znakow reprezentujacy menu edycji magazynu
	 */
	private static final String STOCK_MENU =
			"1 - Zmien cene przedmiotu      \n" +
			"2 - Uzupelnij stan magazynu    \n" +
			"3 - Wycofaj towar z oferty     \n" +
			"0 - Wróc do poprzedniego Menu  \n";
	
	/**
	 * Lancuch znakow reprezentujacy menu zyskow
	 */
	private static final String EARNINGS_MENU =
			"1 - Wyczysc historie \n" +
			"0 - Wroc do poprzedniego menu \n";
	
	/**
	 * Nazwa pliku przechowujacego tablice kont klientow
	 */
	private static final String CLIENT_DATA_FILE_NAME = "SimpleShopClients.BIN";
	
	/**
	 * Nazwa pliku przechowujacego tablice przedmiotow dostepnych w sklepie
	 */
	private static final String ITEMS_DATA_FILE_NAME = "SimpleShopItems.BIN";
	
	/**
	 * Nazwa pliku przechowujacego tablice zawierajaca konto admina
	 */
	private static final String ADMIN_DATA_FILE_NAME = "adminFile.BIN";
	
	/**
	 * Nazwa pliku przechowujacego tablice przedmiotow zakupionych przez klientow
	 */
	private static final String PROFIT_DATA_FILE_NAME = "earnings.BIN";
	
	/**
	 * Wywolanie konstruktora klasu Shop i przypisanie go do zmiennej typu Shop
	 */
	private Shop shop = new Shop();
	
	public SimpleShopApplication(){
		UI.printMessage(GREETING_MESSAGE);
		
		/**
		 * Blok try catch ktorego zadaniem jest wczytanie tablic z kontami klientow, towarami dostepnymi w sklepie, zyskami sklepu oraz z kontem admina
		 * <br>
		 * Blad jest zglaszany kiedy pliki nie moga zostac wczytane	
		 */	
		try {
			shop.loadClientListFromFile(CLIENT_DATA_FILE_NAME);
			UI.printMessage("Klienci zostali wczytani z pliku " + CLIENT_DATA_FILE_NAME);
			shop.loadItemsListFromFile(ITEMS_DATA_FILE_NAME);
			UI.printMessage("Lista przedmiotów w magazynie zostala wczytana z pliku " + ITEMS_DATA_FILE_NAME);
			shop.loadAdminInfoFromFile(ADMIN_DATA_FILE_NAME);
			shop.loadEarningListFromFile(PROFIT_DATA_FILE_NAME);
			
		} catch (Exception e) {
			UI.printErrorMessage(e.getMessage());
		}		
		
		/**
		 * Blok try carch ktorego zadaniem jest:
		 * <ul>
		 *  <li> Sprawdzenie czy istnieje juz konto admina
		 *  <li> Utworzenie go jezeli nie istnieje, oraz przypisanie hasla 'admin1' jako domyslnego
		 * </ul>
		 */
		try{
			if(shop.findAccount("admin") == null)
			{
				Client owner = new Client("admin");
				owner.setPassword("", "admin1");
				shop.manualAddClient(owner);
			}
		}catch (Exception e) {
			UI.printErrorMessage(e.getMessage());
		}
		
		/**
		 * Petla operujaca poruszaniem sie po menu glownym
		 * <br>
		 * <ul>
		 *  <li> 0 - Zapis obecnych tablic oraz zakonczenie dzialania programu
		 *  <li> 1 - Utworzenie nowego konta
		 *  <li> 2 - Logowanie
		 *  <li> 3 - Logowanie jako admin
		 *  <li> 4 - Wypisuje liste klientow (Nie mam pojecia co to tutaj robi)
		 *  </ul>
		 */
		while(true){
			switch(UI.enterInt(SHOP_MENU))	
			{
				case 0:
				{
					try{
						shop.saveClientListToFile(CLIENT_DATA_FILE_NAME);
						shop.saveItemsListToFile(ITEMS_DATA_FILE_NAME);
						shop.saveAdminInfoToFile(ADMIN_DATA_FILE_NAME);
						shop.saveEarningListToFile(PROFIT_DATA_FILE_NAME);
					} catch (Exception e){
						e.getMessage();
					}
					UI.printMessage("Program konczy dzialanie");
					System.exit(0);
					
				}
				
				case 4:
				{
					UI.printMessage(shop.listAccounts());
					break;
				}
				
				case 1:
				{
					newUser();
					break;
				}
				
				case 2:
				{
					logIn();
					break;
				}
				
				case 3:
				{
					adminLogIn();
					break;
				}
			}
			
		}
	}
	
	/**
	 * Metoda komunikujaca sie z uzytkownikiem oraz wywolujaca metode tworzaca nowe konto
	 */
	public void newUser()
	{
		/**
		 * Login nowego konta
		 */
		String pesel;
		
		/**
		 * Haslo nowego konta
		 */
		String password;
		
		/**
		 * Nowe konto
		 */
		Client newClient;
		
		while(true)
		{
			pesel = UI.enterString("Podaj login");
			if(shop.findAccount(pesel)!=null) {
				UI.printErrorMessage("Uzytkownik o takim loginie juz istnieje");
				break;
			}
			password = UI.enterString("Podaj haslo");
			try{
				newClient = shop.makeNewAccount(pesel);
				newClient.setPassword("", password);
			} catch (Exception e){
				e.getMessage();
			}
			UI.printMessage("Konto zostalo utworzone");
			break;
			
		}
		
	}
	
	/**
	 * Metoda komunikujaca sie z uzytkownikiem oraz obslugujaca funkcje logowania sie do konta
	 */
	public void logIn()
	{
		/**
		 * Login konta
		 */
		String login;
		
		/**
		 * Haslo
		 */
		String password;
		
		/**
		 * Zmienna typu Client, ktorej zostaje przypisane konto uzytkownika ktory chce sie zalogowac (jezeli istnieje) 
		 */
		Client client;
		
		UI.printMessage("Logowanie do konta");
		
		login = UI.enterString("Podaj login");
		password = UI.enterString("Podaj haslo");
		
		client = shop.findAccount(login);
		if(client == null){
			UI.printMessage("Wprowadzono bledne dane");
			return;
		}
		
		if(client.checkPassword(password))
		{
			/**
			 * Petla operujaca poruszaniem sie po menu konta klienta
			 * <br>
			 * <ul>
			 *  <li> 0 - Wylogowanie
			 *  <li> 1 - Dodanie nowych srodkow
			 *  <li> 2 - Wyswietlenie informacji o tym koncie
			 *  <li> 3 - Zmiana hasla
			 *  <li> 4 - Zmiana nazwy konta
			 *  <li> 5 - Usuniecie konta
			 *  <li> 6 - Przegladanie ofery sklepu
			 *  <li> 7 - Zakup przedmiotu
			 */
			while(true)
			{
				try{
					switch(UI.enterInt(ACCOUNT_MENU))
					{
					case 0:
					{
						UI.printMessage("Nastapi wylogowanie z konta");
						return;
					}
					case 1:
					{
						double new_money;
						new_money=UI.enterDouble("Ile pieniedzy chcesz dodac do balansu konta?");
						client.addMoney(new_money);
						break;
					}
					case 2:
					{
						UI.printMessage(accountInfo(client));
						break;
					}
					case 3:
					{
						changePassword(client);
						break;
					}
					case 4:
					{
						String user_name;
						user_name=UI.enterString("Podaj nazwe uzytkownika:\n(Zaleca sie aby bylo to imie i nazwisko)");
						client.setName(user_name);
						break;
					}
					case 5:
					{
						String answer;
						answer = UI.enterString("Czy na pewno chcesz usunac konto? Tak/Nie");
						if(answer.equals("Tak")){
							shop.deleteAccount(client);
							return;
						} else if(answer.equals("Nie"))
							break;
						else{
							UI.printErrorMessage("Nieznana komenda");
							break;
						}
						
					}
					case 6:
					{
						UI.printMessage(shop.listItems());
						break;
					}
					case 7:
					{
						String item_name;
						Items item;
						int number;
						item_name = UI.enterString("Podaj nazwe alkoholu który chcesz kupic");
						item = shop.findItem(item_name);
						number = UI.enterInt("Podaj ilosc sztuk alkoholu który chcesz kupic");
						client.loseMoney(item.getPrice()*number);
						item.sellItem(number);
						shop.addToProfitList(item_name, number, item.getPrice(), client.getName());
						UI.printMessage("Zakup zakonczony sukcesem");
						break;
						
					}
					}
				}catch (Exception e){ UI.printErrorMessage(e.getMessage()); }
			}
		}else	UI.printErrorMessage("Bledne haslo");
	}
	
	
	/**
	 * Metoda komunikujaca sie z uzytkownikiem oraz obslugujaca funkcje logowania sie do konta jako admin
	 */
	public void adminLogIn()
	{
		/**
		 * Login konta
		 */
		String login;
		
		/**
		 * Haslo
		 */
		String password;
		
		/**
		 * Zmienna typu Client, ktorej zostaje przypisane konto uzytkownika ktory chce sie zalogowac (jezeli istnieje) 
		 */
		Client client;
		
		UI.printMessage("Logowanie do konta");
		
		login = UI.enterString("Podaj login");
		password = UI.enterString("Podaj haslo");
		
		client = shop.findAccount(login);
		if(client == null){
			UI.printMessage("Wprowadzono bledne dane");
			return;
		}
		
		if(login.equals(client.getLogin()) && client.checkPassword(password))
			{
			
			/**
			 * Petla operujaca poruszaniem sie po menu konta admina
			 * <br>
			 * <ul>
			 *  <li> 0 - Wylogowanie
			 *  <li> 1 - Dodanie nowego towaru do oferty sklepu
			 *  <li> 2 - Wyswietlenie listy wszystkich towarow sklepu
			 *  <li> 3 - Przejscie do menu edycji magazynu
			 *  <li> 4 - Przejscie do menu zyskow oraz wyswietlenie listy zyskow sklepu
			 *  <li> 5 - Wyswietlenie listy wszystkich zarejestrowanych klientow
			 *  <li> 6 - Zmiana hasla
			 * </ul>
			 */
			while(true){
				try{
					switch(UI.enterInt(OWNER_MENU))
					{
					
					case 0:
					{
						UI.printMessage("Zostaniesz wylogowany z konta");
						return;
					}
					
					case 1:
					{
						addNewItem();
						break;
					}
					
					case 2:
					{
						UI.printMessage(shop.listItems());
						break;
					}
					
					case 3:
					{
						editStock();
						break;
					}
					
					case 4:
					{
						/**
						 * Zmienna przechowujaca int odpowiadajacy pod-menu do ktorego uzytkownik chce przejsc
						 */
						int x = UI.enterInt(shop.listEarnings() + "\n" + EARNINGS_MENU + "\n");
						
						/**
						 * Switch case operujacy menu zyskow sklepu
						 * <br>
						 * <ul>
						 *  <li> 0 - Powrot do poprzedniego menu
						 *  <li> 1 - Trwale wyczyszczenie tablicy zyskow sklepu
						 * </ul>
						 */
						switch(x){
						case 0:
						{
							return;
						}
						
						
						case 1:
						{
							String question;
							question = UI.enterString("Czy chcesz trwale wyczyscic liste zyskow? Tak/Nie");
							if(question.equals("Tak")){
								shop.clearEarningsList();
							} else if(question.equals("Nie"))
								break;
							else{ 
								UI.printErrorMessage("Nieznana komenda");
								break;
							}
						}
						
						}
						break;
					}
					
					case 5:
					{
						UI.printMessage(shop.listAccounts());
						break;
					}
					
					case 6:
					{
						changePassword(client);
						break;

					}
					
					}
				} catch (Exception e){
					UI.printErrorMessage(e.getMessage());
				}
			}
		}
		else UI.printMessage("Bledny login lub haslo");
	}
	
	/**
	 * Metoda tworzaca nowy StringBuiler oraz wypisujaca informacje o koncie uzytkownika
	 * 
	 * @param account Konto na temat ktorego maja zostac wypisane informacje
	 */
	public String accountInfo(Client account)
	{
		StringBuilder sb = new StringBuilder();
		sb.append(account.toStringInfo());
		return sb.toString();
	}
	
	/**
	 * Metoda komunikujaca sie z uzytkownikiem oraz wywolujaca metode dodajaca nowy przedmiot do magazynu
	 */
	public void addNewItem()
	{
		/**
		 * Nazwa przedmiotu
		 */
		String name;
		
		name = UI.enterString("Podaj nazwe przedmiotu");
		if(shop.findItem(name)!=null)
			UI.printMessage("Taki przedmiot juz znajduje sie na liscie");
		try{
			shop.addNewItem(name);
		} catch (Exception e){
			UI.printErrorMessage(e.getMessage());
		}
		UI.printMessage("Przedmiot zostal pomyslnie dodany");
		
	}
	
	/**
	 * Metoda komunikujaca sie z uzytkownikiem oraz wywolujaca metode pozwalajaca na zmiane hasla
	 */
	public void changePassword(Client user)
	{
		/**
		 * Nowe oraz stare haslo
		 */
		String new_password, old_password;
		try{
		old_password = UI.enterString("Podaj stare haslo");
		new_password = UI.enterString("Podaj nowe haslo");
		user.setPassword(old_password, new_password);
		}catch (Exception e){UI.printErrorMessage(e.getMessage());}
	}
	
	/**
	 * Metoda wyswietlajaca oraz operujaca menu edycji magazynu
	 */
	public void editStock()
	{
		
		/**
		 * Nazwa nowego przedmiotu
		 */
		String name;
		
		/**
		 * Nowy przedmiot
		 */
		Items item;
		
		/**
		 * Cena nowego przedmiotu
		 */
		double cena;
		
		/**
		 * Ilosc przedmiotow danego typu ktora ma zostac dodana do magazynu
		 */
		int stock;
		
		/**
		 * Petla operujaca poruszaniem sie po menu konta admina
		 * <br>
		 * <ul>
		 *  <li> 0 - Powrot do poprzedniego menu
		 *  <li> 1 - Zmiana ceny przedmiotu
		 *  <li> 2 - Dodanie okreslonej ilosci przedmiotow danego typu do magazynu
		 *  <li> 3 - Usuniecie przedmiotu z oferty sklepu
		 */
		while(true)
		{
			try{
			switch(UI.enterInt("\tEdycja magazynu\n" + STOCK_MENU)){
			
			case 0:
			{
				return;
			}
			
			case 1:
			{
				name = UI.enterString("Podaj nazwe przedmiotu");
				if(shop.findItem(name) == null)
				{
					UI.printErrorMessage("Nie ma takiego przedmiotu");
					break;
				}else
					item = shop.findItem(name);
				
				cena = UI.enterDouble("Podaj nowa cene");
				item.setPrice(cena);
				break;
			} 
				
			case 2:
			{
				name = UI.enterString("Podaj nazwe przedmiotu");
				if(shop.findItem(name) == null)
				{
					UI.printErrorMessage("Nie ma takiego przedmiotu");
					break;
				}else
					item = shop.findItem(name);
				
				stock = UI.enterInt("Jaka ilosc towaru chcesz dodac do magazynu");
				item.addToStock(stock);
				break;
				
			}
			
			case 3:
			{
				/**
				 * Zmienna przechowujaca odpowiedz uzytkownika
				 */
				String question;
				
				name = UI.enterString("Podaj nazwe przedmiotu");
				if(shop.findItem(name) == null)
				{
					UI.printErrorMessage("Nie ma takiego przedmiotu");
					break;
				}else
					item = shop.findItem(name);
				question = UI.enterString("Czy chcesz trwale usunac przedmiot z magazynu? Tak/Nie");
				if(question.equals("Tak")){
					shop.removeItem(item);
					break;
				}
					else if(question.equals("Nie"))
						break;
					else{ 
						UI.printErrorMessage("Nieznana komenda");
						break;
					}
			}
			
			}	
						
			}catch (Exception e){
				UI.printErrorMessage(e.getMessage());
			}
		}
	}
}
