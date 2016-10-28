/*
 * Program SimpleShopApplication
 * Autor: Miko³aj Brukiewicz
 * 	Data: 20 pazdziernika 2016
 */

import java.io.Serializable;


public class Client implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String login;
	private String name;
	private long password;
	private double money;
	
	Client(String login)
	{
		this.login = login;
		password = 0;
		money = 0;
	}
	
	public String getName()
	{
		return name;
	}
	
	public String getLogin()
	{
		return login;
	}
	
	public double getMoney()
	{
		return money;
	}
	
	public void loseMoney(double how_much) throws Exception{
		if(how_much > money) throw new Exception("Nie posiadasz wystarczaj¹cych œrodków");
		money -= how_much;
	}
	
	public boolean checkPassword(String pass)
	{
		if(pass == null) return false;
		return pass.hashCode() == password;
	}
	
	public void setPassword(String oldPassword, String newPassword) throws Exception {
		if (!checkPassword(oldPassword)) throw new Exception("Bledne haslo");
		password = newPassword.hashCode(); 
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public void addMoney(double new_money) throws Exception{
		if(new_money<=0) throw new Exception("Kwota musi byc wieksza od zera");
		money+=new_money;
	}

	public String toString(){
		return String.format("	%s <%s>", name, login);
	}
	
	public String toStringInfo(){
		return String.format("Konto u¿ytkownika:	%s\nDostêpne œrodki: %.2f z³", name, money);
	}
}

