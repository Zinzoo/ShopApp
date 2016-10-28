import java.io.Serializable;

public class Items implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String item_name;
	private double price;
	private int in_stock;
	
	
	Items(String name){
		item_name=name;
		price=0;
		in_stock=0;
	}
	
	public String getName(){
		return item_name;
	}
	
	public double getPrice(){
		return price;
	}
	
	public int getStock(){
		return in_stock;
	}
	
	public void setPrice(double price) throws Exception{
		if(price<=0) throw new Exception("Cena musi byc wieksza od zera");
		this.price=price;
	}
	
	public void addToStock(int items) throws Exception{
		if(items<=0) throw new Exception("Ilosc przedmiotow ktore chcesz dodac do magazynu musi byc wieksza od zera");
		in_stock=items;
	}
	
	public void sellItem(int number) throws Exception{
		if(number<=0) throw new Exception("Ilo�� alkoholu kt�ra chcesz zakupi� musi by� wi�ksza od zera");
		if(number>in_stock) throw new Exception("Nie ma takiej ilo�ci tego przedmiotu w magazynie");
		in_stock-=number;
	}
	
	public String toString(){
		return String.format("%dx  %s  <%.2f zl>",in_stock, item_name, price);
	}

}
