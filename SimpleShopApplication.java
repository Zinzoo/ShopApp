
public class SimpleShopApplication {
	
	public static void main(String[] args){
		new SimpleShopApplication();
	}


	//private UserDialog UI = new ConsoleUserDialog();
	private UserDialog UI = new JOptionPaneUserDialog();
	

	private static final String GREETING_MESSAGE =
			"Program Simple Shop\n" +
			"Autor: Miko³aj Brukiewicz\n" +
			"Data: 21 paziernika 2016 r.\n";

	private static final String SHOP_MENU = 
			"Sklep Alkoholowy Promil - M E N U   G £ Ó W N E      \n" +
			"1 - Utwórz nowe konto              \n" +
			"2 - Zaloguj siê do konta           \n" + 
			"3 - Zaloguj siê jako sprzedawca	\n" +
			"0 - Zakoñcz program                \n";		
		
	private static final String ACCOUNT_MENU =
			"1 - Wp³aæ na konto             \n" +
			"2 - Informacje o tym koncie    \n" +
			"3 - Zmieñ has³o                \n" +
			"4 - Zmieñ nazwisko w³aœciciela \n" +
			"5 - Usuñ konto                 \n" +
			"6 - Przegl¹daj dostêpne alkohole \n" +
			"7 - zakup wybrany alkohol      \n" +
			"0 - Wyloguj siê z konta        \n";
	
	private static final String OWNER_MENU =
			"Jesteœ zalogowany jako sprzedawca     \n\n" +
			"1 - Dodaj nowy towar do magazynu        \n" +
			"2 - Wyœwietl towary w magazynie         \n" +
			"3 - Edycja towarów w magazynie          \n" +
			"4 - Sprawdz utarg                       \n" +
			"5 - Wyœwietl wszystkich klientów        \n" +
			"6 - Zmieñ has³o                         \n" +
			"0 - Wyloguj siê z konta                 \n";

	private static final String STOCK_MENU =
			"1 - Zmieñ cenê przedmiotu      \n" +
			"2 - Uzupe³nij stan magazynu    \n" +
			"3 - Wycofaj towar z oferty     \n" +
			"0 - Wróæ do poprzedniego Menu  \n";
	
	
	private static final String CLIENT_DATA_FILE_NAME = "SimpleShopClients.BIN";
	private static final String ITEMS_DATA_FILE_NAME = "SimpleShopItems.BIN";

	private Shop shop = new Shop();
	
	public SimpleShopApplication(){
		UI.printMessage(GREETING_MESSAGE);
		
			
		try {
			shop.loadClientListFromFile(CLIENT_DATA_FILE_NAME);
			UI.printMessage("Klienci zostali wczytani z pliku " + CLIENT_DATA_FILE_NAME);
			shop.loadItemsListFromFile(ITEMS_DATA_FILE_NAME);
			UI.printMessage("Lista przedmiotów w magazynie zosta³a wczytana z pliku " + ITEMS_DATA_FILE_NAME);
			shop.loadAdminInfoFromFile("adminFile.BIN");
			
		} catch (Exception e) {
			UI.printErrorMessage(e.getMessage());
		}		
		
		try{
			if(shop.findAccount("admin") == null)
			{
				Client krzysio = new Client("admin");
				krzysio.setPassword("", "admin1");
				shop.manualAddClient(krzysio);
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
						shop.saveAdminInfoToFile("adminFile.BIN");
					} catch (Exception e){
						e.getMessage();
					}
					UI.printMessage("Program koñczy dzia³anie");
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
				UI.printErrorMessage("U¿ytkownik o takim peselu ju¿ istnieje");
				break;
			}
			password = UI.enterString("Podaj haslo");
			try{
				newClient = shop.makeNewAccount(pesel);
				newClient.setPassword("", password);
			} catch (Exception e){
				e.getMessage();
			}
			UI.printMessage("Uda³o siê");
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
			UI.printMessage("Wprowadzono b³êdne dane");
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
						UI.printMessage("Nast¹pi wylogowanie z konta");
						return;
					}
					case 1:
					{
						double new_money;
						new_money=UI.enterDouble("Ile pieniêdzy chcesz dodaæ do balansu konta?");
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
						user_name=UI.enterString("Podaj nazwê u¿ytkownika:\n(Zaleca siê aby by³o to imiê i zazwisko)");
						client.setName(user_name);
						break;
					}
					case 5:
					{
						String answer;
						answer = UI.enterString("Czy na pewno chcesz usun¹æ konto? Tak/Nie");
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
						item_name = UI.enterString("Podaj nazwê alkoholu który chcesz kupiæ");
						item = shop.findItem(item_name);
						number = UI.enterInt("Podaj iloœæ sztuk alkoholu który chcesz kupiæ");
						client.loseMoney(item.getPrice()*number);
						item.sellItem(number);
						UI.printMessage("Zakup zakoñczony sukcesem");
						
					}
					}
				}catch (Exception e){ UI.printErrorMessage(e.getMessage()); }
			}
		}else	UI.printErrorMessage("B³êdne has³o");
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
			UI.printMessage("Wprowadzono b³êdne dane");
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
		else UI.printMessage("B³êdny login lub has³o");
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
		
		name = UI.enterString("Podaj nazwê przedmiotu");
		if(shop.findItem(name)!=null)
			UI.printMessage("Taki przedmiot ju¿ znajduje siê na liœcie");
		try{
			shop.addNewItem(name);
		} catch (Exception e){
			UI.printErrorMessage(e.getMessage());
		}
		UI.printMessage("Przedmiot zosta³ pomyœlnie dodany");
		
	}
	
	public void changePassword(Client user)
	{
		String new_password, old_password;
		try{
		old_password = UI.enterString("Podaj stare has³o");
		new_password = UI.enterString("Podaj nowe has³o");
		user.setPassword(old_password, new_password);
		}catch (Exception e){UI.printErrorMessage(e.getMessage());}
	}
	
	public void editStock()      //metoda obs³uguj¹ca edycjê magazynu
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
				name = UI.enterString("Podaj nazwê przedmiotu");
				if(shop.findItem(name) == null)
				{
					UI.printErrorMessage("Nie ma takiego przedmiotu");
					break;
				}else
					item = shop.findItem(name);
				
				cena = UI.enterDouble("Podaj now¹ cenê");
				item.setPrice(cena);
				break;
			} 
				
			case 2:
			{
				name = UI.enterString("Podaj nazwê przedmiotu");
				if(shop.findItem(name) == null)
				{
					UI.printErrorMessage("Nie ma takiego przedmiotu");
					break;
				}else
					item = shop.findItem(name);
				
				stock = UI.enterInt("Jak¹ iloœæ towaru chcesz dodaæ do magazynu");
				item.addToStock(stock);
				break;
				
			}
			
			case 3:
			{
				String question;
				
				name = UI.enterString("Podaj nazwê przedmiotu");
				if(shop.findItem(name) == null)
				{
					UI.printErrorMessage("Nie ma takiego przedmiotu");
					break;
				}else
					item = shop.findItem(name);
				question = UI.enterString("Czy chcesz trwale usun¹æ przedmiot z magazynu? Tak/Nie");
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
