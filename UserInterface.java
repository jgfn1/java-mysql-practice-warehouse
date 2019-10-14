// package com.warehouse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

import java.sql.*;

public class UserInterface {
	private static UserInterface userInterface;
	static Mysql mysql = new Mysql();
	private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
	static final int EXIT = 0;
	private static final int ADD_CLIENT = 1;
	private static final int ADD_MANUFACTURER = 2;
	private static final int ADD_PRODUCTS = 3;
	private static final int DISPLAY_CLIENTS = 4;
	private static final int SHOW_MANUFACTURERS = 5;
	private static final int SHOW_PRODUCTS = 6;
	private static final int ASSIGN_PRICE = 7;
	private static final int UNASSIGN_PRICE = 8;
	private static final int GET_PRODUCT_BY_SUPPLIER = 9;
	private static final int GET_SUPPLIER_BY_PRODUCT = 10;
	private static final int PLACE_ORDER = 11;
	private static final int MAKE_PAYMENT = 12;
	private static final int GET_OUTSTANDING_CLIENTS = 13;
	private static final int ACCEPT_SHIPMENT = 14;
	private static final int GET_CLIENT_WAITLIST = 15;
	private static final int GET_PRODUCT_WAITLIST = 16;
	private static final int PLACE_ORDER_WITH_MANUFACTURER = 17;
	private static final int DISPLAY_ORDERS_WITH_A_MANUFACTURER = 18;
	private static final int GET_PRODUCTS_WITHOUT_MANUFACTURERS = 19;
	private static final int GET_MANUFACTURERS_WITHOUT_PRODUCTS = 20;
	private static final int HELP = 21;

	public static UserInterface instance() {
		if (userInterface == null) {
			return userInterface = new UserInterface();
		} else {
			return userInterface;
		}
	}

	private boolean yesOrNo(String prompt) {
		String more = getToken(prompt + " (Y|y)[es] or anything else for no");
		if (more.charAt(0) != 'y' && more.charAt(0) != 'Y') {
			return false;
		}
		return true;
	}

	public String getToken(String prompt) {
		do {
			try {
				System.out.println(prompt);
				String line = reader.readLine();
				StringTokenizer tokenizer = new StringTokenizer(line, "\n\r\f");
				if (tokenizer.hasMoreTokens()) {
					return tokenizer.nextToken();
				}
			} catch (IOException ioe) {
				System.exit(0);
			}
		} while (true);
	}

	public int getCommand() {
		do {
			try {
				int value = Integer.parseInt(getToken("Enter command: " + HELP + " for help"));
				if (value >= EXIT && value <= HELP) {
					return value;
				}
			} catch (NumberFormatException nfe) {
				System.out.println("Enter a number: ");
			}
		} while (true);
	}

	public void help() {
		System.out.println(" Enter a choice from 0 through 9 as shown below:");
		System.out.println("|=======================================================|");
		System.out.println("| " + EXIT                    + ".Exit out                                            |");
		System.out.println("| " + ADD_CLIENT              + ".Add a client                                        |");
		System.out.println("| " + ADD_MANUFACTURER        + ".Add a manufacturer                                  |");
		System.out.println("| " + ADD_PRODUCTS            + ".Add products                                        |");
		System.out.println("| " + DISPLAY_CLIENTS         + ".Display list of clients                             |");
		System.out.println("| " + SHOW_MANUFACTURERS      + ".Display list of manufacturers                       |");
		System.out.println("| " + SHOW_PRODUCTS           + ".Display list of products                            |");
		System.out.println("| " + ASSIGN_PRICE            + ".Assign a product to a manufacturer with a price     |");
		System.out.println("| " + UNASSIGN_PRICE          + ".Unassign a product from a manufacturer              |");
		System.out.println("| " + GET_PRODUCT_BY_SUPPLIER + ".Display a list of products for a manufacturer       |");
		System.out.println("| " + GET_SUPPLIER_BY_PRODUCT + ".Display a list of manufacturers for a product      |");
		// System.out.println("| " + PLACE_ORDER             + ".Place an order                                     |");
		// System.out.println("| " + MAKE_PAYMENT            + ".Make payment on behalf of a client                 |");
		// System.out.println("| " + GET_OUTSTANDING_CLIENTS + ".Display a list of clients with outstanding balance |");
		// System.out.println("| " + ACCEPT_SHIPMENT         + ".Accept shipment                                    |");
		// System.out.println("| " + GET_CLIENT_WAITLIST     + ".Display a list of waitlisted orders for a client   |");
		// System.out.println("| " + GET_PRODUCT_WAITLIST    + ".Display a list of waitlisted orders for a product  |");
		// System.out.println("| " + PLACE_ORDER_WITH_MANUFACTURER + ".Order with a manufacturer                          |");
		// System.out.println("| " + DISPLAY_ORDERS_WITH_A_MANUFACTURER    + ".Display a list of orders with a manufacturer       |");
		System.out.println("| " + GET_PRODUCTS_WITHOUT_MANUFACTURERS + ".Display a list of products without a manufacturer  |");
		System.out.println("| " + GET_MANUFACTURERS_WITHOUT_PRODUCTS + ".Display a list of manufacturers without products   |");
		System.out.println("| " + HELP                    + ".Help                                               |");
		System.out.println("|=======================================================|");
	}

	public void addClient() {
		String name = getToken("Enter client name: ");
		String address = getToken("Enter address: ");
		// Verify phoneNumber
		String phoneNumber = getToken("Enter phone: ");
		String balance = getToken("Enter balance: ");
		mysql.update("INSERT INTO clients VALUES('" + name + "', '" + address + "', '" + phoneNumber + "', null," + balance + ");");
	}

	public void addManufacturer() {
		String name = getToken("Enter manufacturer name: ");
		String address = getToken("Enter address: ");
		mysql.update("INSERT INTO manufacturers VALUES('" + name + "', '" + address + "', null);");
	}

	public void addProducts() {
		do {
			String name = getToken("Enter product name: ");
			String category = getToken("Enter category: ");
			mysql.update("INSERT INTO products VALUES('" + name + "', '" + category + "', null);");
			if (!yesOrNo("Add more products?")) {
				break;
			}
		} while (true);
	}

	public void displayClients() {
		ResultSet rs = mysql.query("SELECT * FROM clients");
		try{  
			while(rs.next()){
				System.out.println(
					"Client " + rs.getInt("clientID")+
					":\n"+
					"Name: " + rs.getString("name")+
					"\n"+
					"Address: " + rs.getString("address")+
					"\n"+
					"Phone number: " + rs.getString("phoneNumber")+
					"\n"+
					"Balance: " + rs.getDouble("balance")+
					"\n"
				);
			}
		}catch(Exception e){ 
			System.out.println(e);
		}  
	}

	public void showProducts() {
		ResultSet rs = mysql.query("SELECT * FROM products");
		try{  
			while(rs.next()){
				System.out.println(
					"Product " + rs.getInt("productID")+
					":\n"+
					"Name: " + rs.getString("name")+
					"\n"+
					"Category: " + rs.getString("category")+
					"\n"
				);
			}
		}catch(Exception e){ 
			System.out.println(e);
		}
	}

	public void showManufacturers() {
		ResultSet rs = mysql.query("SELECT * FROM manufacturers");
		try{  
			while(rs.next()){
				System.out.println(
					"Manufacturer " + rs.getInt("manufacturerID")+
					":\n"+
					"Name: " + rs.getString("name")+
					"\n"+
					"Address: " +rs.getString("address")+
					"\n"
				);
			}
		}catch(Exception e){ 
			System.out.println(e);
		}
	}

	public void assignPrice() {
		// Create trigger to ensure ID's existence
		String mid = getToken("Enter the manufacturer's ID");
		String pid = getToken("Enter the product's ID");
		String p = getToken("Enter the price");
		double price = Double.valueOf(p);
		mysql.update("INSERT INTO products_manufacturers VALUES(" + pid + ", " + mid + ", " + p + ");");
	}

	public void unassignPrice() {
		// Create trigger to ensure ID's existence
		String mid = getToken("Enter the manufacturer's ID");
		String pid = getToken("Enter the product's ID");
		mysql.update("DELETE FROM products_manufacturers WHERE products_manufacturers.manufacturerID = " + mid + " AND products_manufacturers.productID = " + pid + ";");
	}

	public void getProductBySupplier() {
		String mid = getToken("Enter the manufacturer's ID");
		ResultSet rs = mysql.query("SELECT p.name 'Product', p.productID 'ID', m.manufacturerID 'mID', m.name 'Manufacturer', pm.price 'Price' FROM products_manufacturers pm INNER JOIN products p ON pm.productID = p.productID INNER JOIN manufacturers m ON m.manufacturerID = " + mid +" AND pm.manufacturerID = " + mid + "; ");
		try{  
			while(rs.next()){
				System.out.println(
					"Product " + rs.getString("ID") + ": " + rs.getString("Product")+
					"\n"+
					"Manufacturer " + rs.getString("mID") + ": " + rs.getString("Manufacturer")+
					"\n"+
					"Price: " +rs.getString("Price")+
					"\n"
				);
			}
		}catch(Exception e){ 
			System.out.println(e);
		}
	}

	public void getSupplierByProduct() {
		String pid = getToken("Enter the product's ID");
		ResultSet rs = mysql.query("SELECT p.name 'Product', p.productID 'ID', m.manufacturerID 'mID', m.name 'Manufacturer', pm.price 'Price' FROM products_manufacturers pm INNER JOIN products p on pm.productID = " + pid + " AND p.productID = " + pid + " INNER JOIN manufacturers m ON m.manufacturerID = pm.manufacturerID; ");
		try{  
			while(rs.next()){
				System.out.println(
					"Product " + rs.getString("ID") + ": " + rs.getString("Product")+
					"\n"+
					"Manufacturer " + rs.getString("mID") + ": " + rs.getString("Manufacturer")+
					"\n"+
					"Price: " +rs.getString("Price")+
					"\n"
				);
			}
		}catch(Exception e){ 
			System.out.println(e);
		}
	}

	public void getProductsWithoutSuppliers() {
		ResultSet rs = mysql.query("SELECT * FROM (SELECT p.name 'Product', p.productID 'ID', p.category 'Category', pm.price FROM  products p LEFT OUTER JOIN products_manufacturers pm ON p.productID = pm.productID) AS t WHERE t.price IS NULL;");
		try{  
			while(rs.next()){
				System.out.println(
					"Product " + rs.getString("ID") + ": " + rs.getString("Product")+
					"\n"+
					"Category: " + rs.getString("Category")+
					"\n"
				);
			}
		}catch(Exception e){ 
			System.out.println(e);
		}
	}

	public void getSuppliersWithoutProducts() {
		ResultSet rs = mysql.query("SELECT * FROM (SELECT m.name 'Manufacturer', m.address 'Address', m.manufacturerID 'ID', pm.productID FROM products_manufacturers pm RIGHT OUTER JOIN manufacturers m ON m.manufacturerID = pm.manufacturerID) AS t WHERE t.productID IS NULL;");
		try{  
			while(rs.next()){
				System.out.println(
					"Manufacturer " + rs.getString("ID") + ": " + rs.getString("Manufacturer")+
					"\n"+
					"Address: " + rs.getString("Address")+
					"\n"
				);
			}
		}catch(Exception e){ 
			System.out.println(e);
		}
	}

	// private void addOrder() {
	// 	Order newOrder;
	// 	boolean isAdded;
	// 	boolean isRunning = true;
	// 	String clientID = getToken("Enter client's ID: ");
	// 	newOrder = warehouse.addOrder(clientID);
	// 	while(newOrder != null && isRunning) {
	// 		String manufacturerID = getToken("Enter Manufacturer's ID: ");
	// 		String productID = getToken("Enter Product's ID: ");
	// 		String q = getToken("Enter quantity: ");
	// 		int quantity = Integer.valueOf(q);
	// 		isAdded = warehouse.addLineItem(newOrder, productID, manufacturerID, quantity);
	// 		if (isAdded == false) {
	// 			System.out.println("Invalid ID");
	// 		} else {
	// 			System.out.println("Added the item to the order");
	// 		}
	// 		String response = getToken("Would you like to add another item? y/n");
	// 		if(response.equals("n")) {
	// 			System.out.println("Adding items completed");
	// 			isRunning = false;
	// 		}
	// 	}
	// 	if (warehouse.checkOrder(newOrder)) {
	// 		System.out.println("Order has been processed.");
	// 	} else {
	// 		System.out.println("Order added to the waitlist");
	// 	}
	// }
	
	// public void displayOutstandingClients() {
	// 	Iterator listOfClients = warehouse.getClients();
	// 	while (listOfClients.hasNext()) {
	// 		Client client = (Client) (listOfClients.next());
	// 		if(client.isOutstanding())
	// 			System.out.println(client);
	// 	}
	// }
	
	// public void makePayment() {
	// 	int result;
	// 	String clientID = getToken("Enter client's ID: ");
	// 	String a = getToken("Enter amount: ");
	// 	int amount = Integer.valueOf(a);
	// 	result = warehouse.makePayment(clientID, amount);
	// 	if (result == 0) {
	// 		System.out.println("Client not found.");
	// 	} else if (result == 1) {
	// 		System.out.println("Amount greater than the balance.");
	// 	} else {
	// 		System.out.println("The payment was received.");
	// 	}
	// }
	
	// public void acceptShipment() {
	// 	ProductManufacturer result;
	// 	String manufacturerID = getToken("Enter manufacturer's ID: ");
	// 	String productID = getToken("Enter product's ID: ");
	// 	String q = getToken("Enter quantity: ");
	// 	int quantity = Integer.valueOf(q);
	// 	result = warehouse.acceptShipment(productID, manufacturerID, quantity);
	// 	if (result != null) {
	// 		System.out.println(result);
	// 	} else {
	// 		System.out.println("Wrong product's ID or manufacturer's ID.");
	// 	}
	// }
	
	// public void displayClientWaitList() {
	// 	String clientID = getToken("Enter client's ID: ");
	// 	Client client = warehouse.searchForClient(clientID);
	// 	if (client == null)
    // 		System.out.println("Client not found");
	// 	else {
	// 		Iterator itr = client.getWaitListOrderIDs();
	// 		while(itr.hasNext()) {
	// 			String orderID = (String) (itr.next());
	// 			Order order = warehouse.searchForOrder(orderID);
    //             System.out.println(order.toString());
	// 		}
	// 	}
	// }
	
	// public void displayProductWaitList() {
	// 	String productID = getToken("Enter product's ID: ");
	// 	Product product = warehouse.searchForProduct(productID);
	// 	if (product == null)
    // 		System.out.println("Product not found");
	// 	else {
	// 		Iterator itr = product.getWaitListOrderIDs();
	// 		while(itr.hasNext()) {
	// 			String orderID = (String) (itr.next());
    //             System.out.println(orderID);
	// 		}
	// 	}
	// }
	
	// private void addOrderWithManufacturer() {
	// 	OrderWithManufacturer result;
	// 	String manufacturerID = getToken("Enter Manufacturer's ID: ");
	// 	String productID = getToken("Enter Product's ID: ");
	// 	String orderID = getToken("Enter order's ID: ");
	// 	String q = getToken("Enter quantity: ");
	// 	int quantity = Integer.valueOf(q);
	// 	result = warehouse.addManufacturerOrder(orderID, productID, manufacturerID, quantity);
	// 	if (result != null) {
	// 		System.out.println(result);
	// 	} else {
	// 		System.out.println("Invalid ID.");
	// 	}
	// }
	
	// public void displayOrdersWithAManufacturer() {
	// 	String manufacturerID = getToken("Enter manufacturer's ID: ");
	// 	Iterator listOfOrders = warehouse.OrdersWithAManufacturer(manufacturerID);
	// 	while (listOfOrders.hasNext()) {
	// 		OrderWithManufacturer orderWithManufacturer = (OrderWithManufacturer) (listOfOrders.next());
	// 	    System.out.println(orderWithManufacturer); 
	// 	}
	// }

	public void process() {
		int command;
		help();
		while ((command = getCommand()) != EXIT) {
			switch (command) {
			case ADD_CLIENT:
				addClient();
				break;
			case DISPLAY_CLIENTS:
				displayClients();
				break;
			case ADD_MANUFACTURER:
				addManufacturer();
				break;
			case ADD_PRODUCTS:
				addProducts();
				break;
			case SHOW_MANUFACTURERS:
				showManufacturers();
				break;
			case SHOW_PRODUCTS:
				showProducts();
				break;
			case ASSIGN_PRICE:
				assignPrice();
				break;
			case UNASSIGN_PRICE:
				unassignPrice();
				break;
			case GET_PRODUCT_BY_SUPPLIER:
				getProductBySupplier();
				break;
			case GET_SUPPLIER_BY_PRODUCT:
				getSupplierByProduct();
				break;
			case PLACE_ORDER:
				// addOrder();
				break;
			case MAKE_PAYMENT:
				// makePayment();
				break;
			case GET_OUTSTANDING_CLIENTS:
				// displayOutstandingClients();
				break;
			case ACCEPT_SHIPMENT:
				// acceptShipment();
				break;
			case GET_CLIENT_WAITLIST:
				// displayClientWaitList();
				break;
			case GET_PRODUCT_WAITLIST:
				// displayProductWaitList();
				break;
			case PLACE_ORDER_WITH_MANUFACTURER:
				// addOrderWithManufacturer();
				break;
			case DISPLAY_ORDERS_WITH_A_MANUFACTURER:
				// displayOrdersWithAManufacturer();
				break;
			case GET_PRODUCTS_WITHOUT_MANUFACTURERS:
				getProductsWithoutSuppliers();
				break;
				case GET_MANUFACTURERS_WITHOUT_PRODUCTS:
				getSuppliersWithoutProducts();
				break;
			case HELP:
				help();
				break;
			}
		}
	}

	public static void main(String[] args) {
		UserInterface.instance().process();
	}

}
