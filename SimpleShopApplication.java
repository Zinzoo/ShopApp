/*
 * Program SimpleShopApplication
 * Autor: Miko�aj Brukiewicz
 * Zajecia: Jezyki Programowania, Sroda TP 11:15 - 13:00
 * Indeks: 225954
 * 	Data: 30 pazdziernika 2016
 */

public class SimpleShopApplication {
	
	public static void main(String[] args){
		new SimpleShopApplication();
	}


	//private UserDialog UI = new ConsoleUserDialog();
	private UserDialog UI = new JOptionPaneUserDialog();
	

	private static final String GREETING_MESSAGE =
			"Program Simple Shop\n" +
			"Autor: Miko�aj Brukiewicz\n" +
			"Data: 21 paziernika 2016 r.\n";

	private static final String SHOP_MENU = 
			"Sklep Alkoholowy Promil - M E N U   G L O W N E      \n" +
			"1 - Utw�rz nowe konto              \n" +
			"2 - Zaloguj si� do konta           \n" + 
			"3 - Zaloguj sie jako sprzedawca	\n" +
			"0 - Zakoncz program                \n";		
		
	private static final String ACCOUNT_MENU =
			"1 - Wplac na konto             \n" +
			"2 - Informacje o tym koncie    \n" +
			"3 - Zmien haslo                \n" +
			"4 - Zmien nazwisko wlasciciela \n" +
			"5 - Usun konto                 \n" +
			"6 - Przegladaj dostepne alkohole \n" +
			"7 - Zakup wybrany alkohol      \n" +
			"0 - Wyloguj sie z konta        \n";
	
	private static final String OWNER_MENU =
			"Jestes zalogowany jako sprzedawca     \n\n" +
			"1 - Dodaj nowy towar do magazynu        \n" +
			"2 - Wyswietl towary w magazynie         \n" +
			"3 - Edycja towar�w w magazynie          \n" +
			"4 - Sprawdz utarg                       \n" +
			"5 - Wyswietl wszystkich klient�w        \n" +
			"6 - Zmien haslo                         \n" +
			"0 - Wyloguj sie z konta                 \n";

	private static final String STOCK_MENU =
			"1 - Zmien cene przedmiotu      \n" +
			"2 - Uzupelnij stan magazynu    \n" +
			"3 - Wycofaj towar z oferty     \n" +
			"0 - Wr�c do poprzedniego Menu  \n";
	
	private static final String EARNINGS_MENU =
			"1 - Wyczysc historie \n" +
			"0 - Wroc do poprzedniego menu \n";
	
	
	private static final String CLIENT_DATA_FILE_NAME = "SimpleShopClients.BIN";
	private static final String ITEMS_DATA_FILE_NAME = "SimpleShopItems.BIN";
	private static final String ADMIN_DATA_FILE_NAME = "adminFile.BIN";
	private static final String PROFIT_DATA_FILE_NAME = "earnings.BIN";
	
	private Shop shop = new Shop();
	
	public SimpleShopApplication(){
		UI.printMessage(GREETING_MESSAGE);
		
			
		try {
			shop.loadClientListFromFile(CLIENT_DATA_FILE_NAME);
			UI.printMessage("Klienci zostali wczytani z pliku " + CLIENT_DATA_FILE_NAME);
			shop.loadItemsListFromFile(ITEMS_DATA_FILE_NAME);
			UI.printMessage("Lista przedmiot�w w magazynie zostala wczytana z pliku " + ITEMS_DATA_FILE_NAME);
			shop.loadAdminInfoFromFile(ADMIN_DATA_FILE_NAME);
			shop.loadEarningListFromFile(PROFIT_DATA_FILE_NAME);
			
		} catch (Exception e) {
			UI.printErrorMessage(e.getMessage());
		}		
		
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
	
	public void newUser()
	{
		String pesel;
		String password;
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
	
	public void logIn()
	{
		String login;
		String password;
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
						item_name = UI.enterString("Podaj nazwe alkoholu kt�ry chcesz kupic");
						item = shop.findItem(item_name);
						number = UI.enterInt("Podaj ilosc sztuk alkoholu kt�ry chcesz kupic");
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
	
	public void adminLogIn()
	{
		String login;
		String password;
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
						int x = UI.enterInt(shop.listEarnings() + "\n" + EARNINGS_MENU + "\n");
						
						switch(x){
						case 0:
						{
							return;
						}
						
						case 1:
						{
							String question;
							question = UI.enterString("Czy chcesz trwale usunac przedmiot z magazynu? Tak/Nie");
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
	
	public String accountInfo(Client account)
	{
		StringBuilder sb = new StringBuilder();
		sb.append(account.toStringInfo());
		return sb.toString();
	}
	
	public void addNewItem()
	{
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
	
	public void changePassword(Client user)
	{
		String new_password, old_password;
		try{
		old_password = UI.enterString("Podaj stare haslo");
		new_password = UI.enterString("Podaj nowe haslo");
		user.setPassword(old_password, new_password);
		}catch (Exception e){UI.printErrorMessage(e.getMessage());}
	}
	
	public void editStock()
	{
		String name;
		Items item;
		double cena;
		int stock;
		
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
