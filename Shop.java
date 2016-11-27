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

public class Shop implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private ArrayList<Client> listOfClients = new ArrayList<Client>();
	private ArrayList<Items> listOfItems = new ArrayList<Items>();
	private ArrayList<Client> adminInfo = new ArrayList<Client>();
	private ArrayList<Items> earnings = new ArrayList<Items>();
	
	public Items addNewItem(String item_name) throws Exception{
		if(item_name == null || item_name.equals("")) throw new Exception("Musisz podac nazwe przedmiotu");
		if(findItem(item_name) != null) throw new Exception("Taki przedmiot juz jest w magazynie");
		
		Items newItem = new Items(item_name);
		listOfItems.add(newItem);
		return newItem;
	}
	
	public Items addToProfitList(String item_name, int number, double price, String buyer) throws Exception
	{
		Items newItem = new Items(item_name);
		newItem.setBuyer(buyer);
		newItem.setPrice(price);
		newItem.addToStock(number);
		earnings.add(newItem);
		return newItem;
	}
	
	public void removeItem(Items item) throws Exception{
		if(findItem(item.getName()) == null) throw new Exception("Nie ma takiego przedmiotu");
		listOfItems.remove(item);
	}
	
	public void buyItem(Items item, Client buyer, int number) throws Exception{
		if(findItem(item.getName()) == null) throw new Exception("Nie ma takiego alkoholu w magazynie");
		if(item.getStock() < number) throw new Exception("Nie ma takiej ilosci wybranego alkoholu w magazynie");
		if(buyer.getMoney() < item.getPrice()) throw new Exception("Nie posiadasz wystarczajacych srodkow");
		buyer.loseMoney((item.getPrice() * number));
	}
	
	public Client makeNewAccount(String account_login) throws Exception{
		if(account_login == null || account_login.equals("")) throw new Exception("Nazwa konta nie moze byc pusta");
		if(findAccount(account_login) != null) throw new Exception("Uzytkownik o takiej nazwie juz istnieje");
		
		Client newUser = new Client(account_login);
		listOfClients.add(newUser);
		return newUser;
	}
	
	public void manualAddClient(Client person)
	{
		adminInfo.add(person);
	}

	public void deleteAccount(Client user) throws Exception{
		if(findAccount(user.getLogin()) == null) throw new Exception("Nie ma takiego uzytkownika");
		listOfClients.remove(user);
	}
	
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
	
	public Items findItem(String item_name)
	{
		for(Items new_item : listOfItems)
		{
			if(new_item.getName().equals(item_name)) return new_item;
		}
		return null;
	}
	
	public String listAccounts(){
		StringBuilder sb = new StringBuilder();
		int n = 0;
		for (Client client : listOfClients){
			if (n++ != 0) sb.append("\n");		
			sb.append(client.toString());
		}
		return sb.toString();
	}
	
	public double allEarnings(){
		double amount = 0;
		for (Items item : earnings){
			amount += (item.getPrice()*item.getStock());
		}
		return amount;
	}
	
	public String listItems(){
		StringBuilder sb = new StringBuilder();
		int n = 0;
		for (Items x : listOfItems){
			if (n++ != 0) sb.append("\n");		
			sb.append(x.toString());
		}
		return sb.toString();
	}
	
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
	
	public void clearEarningsList(){
		earnings.clear();
	}
	
	void saveClientListToFile(String fileName) throws Exception {
		ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName));
		out.writeObject(listOfClients);
		out.close();
	}
	
	void saveEarningListToFile(String fileName) throws Exception {
		ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName));
		out.writeObject(earnings);
		out.close();
	}
	
	void loadClientListFromFile(String fileName) throws Exception {
		ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName));
		listOfClients = (ArrayList<Client>)in.readObject();
		in.close();
	}
	
	void loadEarningListFromFile(String fileName) throws Exception {
		ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName));
		earnings = (ArrayList<Items>)in.readObject();
		in.close();
	}
	
	void saveAdminInfoToFile(String adminFileName) throws Exception {
		ObjectOutputStream out2 = new ObjectOutputStream(new FileOutputStream(adminFileName));
		out2.writeObject(adminInfo);
		out2.close();
	}
	
	void loadAdminInfoFromFile(String adminFileName) throws Exception {
		ObjectInputStream in2 = new ObjectInputStream(new FileInputStream(adminFileName));
		adminInfo = (ArrayList<Client>)in2.readObject();
		in2.close();
	}
	
	void saveItemsListToFile(String fileName) throws Exception {
		ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName));
		out.writeObject(listOfItems);
		out.close();
	}
	
	void loadItemsListFromFile(String fileName) throws Exception {
		ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName));
		listOfItems = (ArrayList<Items>)in.readObject();
		in.close();
	}

}
