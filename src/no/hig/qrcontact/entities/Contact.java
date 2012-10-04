package no.hig.qrcontact.entities;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Comparator;

import android.annotation.SuppressLint;
import android.util.Base64InputStream;
import android.util.Base64OutputStream;

public class Contact implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private int id;

	private String name = null;
	private String surname = null;
	private String contactType = null;
	private String number = null;
	private boolean share = false;
	private String email = null;
	
	private boolean show = true; 

	public Contact(){}
	
	public Contact(String name, String surname, String number) {
		this.name = name;
		this.surname = surname;
		this.number = number;
	}
	
	public Contact(String name, String surname, String number, int share) {
		this.name = name;
		this.surname = surname;
		this.number = number;
		setShare(share);
	}
	
	public Contact(String name, String surname, String number, boolean share) {
		this.name = name;
		this.surname = surname;
		this.number = number;
		setShare(share);
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getContactType() {
		return contactType;
	}

	public void setContactType(String contactType) {
		this.contactType = contactType;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}
	
	public void setShare(int share) {
		this.share = (share == 0) ? false : true;
	}
	
	public void setShare(boolean share) {
		this.share = share;
	}
	
	public boolean getShare(){
		return share;
	}
	
	public void setEmail(String email){
		this.email = email;
	}
	
	public String getEmail(){
		return email;
	}
	
	public void setVisibility(boolean v){
		show = v;
	}
	
	public boolean getVisibility(){
		return show;
	}

	@SuppressLint("NewApi")
	public static String objectToString(Serializable object) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			new ObjectOutputStream(out).writeObject(object);
			byte[] data = out.toByteArray();
			out.close();

			out = new ByteArrayOutputStream();
			Base64OutputStream b64 = new Base64OutputStream(out, 192);
			b64.write(data);
			b64.close();
			out.close();

			return new String(out.toByteArray());
		} catch (IOException e) {
			// e.printStackTrace();
		}
		return null;
	}

	@SuppressLint("NewApi")
	public static Object stringToObject(String encodedObject) {
		try {
			return new ObjectInputStream(new Base64InputStream(new ByteArrayInputStream(encodedObject.getBytes()), 192))
					.readObject();
		} catch (Exception e) {
			// e.printStackTrace();
		}
		return null;
	}
	
	public static class Compare implements Comparator<Contact>{

		@Override
		public int compare(Contact first, Contact second) {
			return first.getName().compareTo(second.getName());
		}
		
	}

}
