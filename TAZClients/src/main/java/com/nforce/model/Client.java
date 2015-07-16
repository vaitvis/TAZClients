package com.nforce.model;

import java.util.Date;

public class Client {
	
	/**
	 * Kliento eilės numeris
	 */
	private Integer id;
	
	/**
	 * Adresatas (pvz., buhalteriui, direktoriui, viešųjų pirkimų specialistui ar dar kam nors).
	 */
	private String addressee;
	
	/**
	 * Įmonės pavadinimas
	 */
	private String companyTitle;
	
	/**
	 * Įmonės kodas
	 */
	private String companyCode;
	
	/**
	 * Įmonės PVM kodas
	 */
	private String companyVatCode;
	
	/**
	 * Adresas
	 */
	private String address;
	
	/**
	 * Telefono numeris
	 */
	private String phoneNumber;
	
	/**
	 * El. paštas
	 */
	private String email;
	
	/**
	 * Prenumeratos laikotarpio pradžia
	 */
	private Date validFrom;
	
	/**
	 * Prenumeratos laikotarpio pabaiga
	 */
	private Date validTo;
	
	/**
	 * Kliento požymio laukas (pvz., klientas „gyvas“ – 1, klientas „miręs“, t.y. jam nutrūkusi 
	 * prenumerata – 2)
	 */
	private ClientState clientState;
	
	/**
	 * Pastaba
	 */
	private String comment;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAddressee() {
		return addressee;
	}

	public void setAddressee(String addressee) {
		this.addressee = addressee;
	}

	public String getCompanyTitle() {
		return companyTitle;
	}

	public void setCompanyTitle(String companyTitle) {
		this.companyTitle = companyTitle;
	}

	public String getCompanyCode() {
		return companyCode;
	}

	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}

	public String getCompanyVatCode() {
		return companyVatCode;
	}

	public void setCompanyVatCode(String companyVatCode) {
		this.companyVatCode = companyVatCode;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getValidFrom() {
		return validFrom;
	}

	public void setValidFrom(Date validFrom) {
		this.validFrom = validFrom;
	}

	public Date getValidTo() {
		return validTo;
	}

	public void setValidTo(Date validTo) {
		this.validTo = validTo;
	}

	public ClientState getClientState() {
		return clientState;
	}

	public void setClientState(ClientState clientState) {
		this.clientState = clientState;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	@Override
	public String toString() {
		return "Client [id=" + id + ", addressee=" + addressee
				+ ", companyTitle=" + companyTitle + ", companyCode="
				+ companyCode + ", companyVatCode=" + companyVatCode
				+ ", address=" + address + ", phoneNumber=" + phoneNumber
				+ ", email=" + email + ", validFrom=" + validFrom
				+ ", validTo=" + validTo + ", clientState=" + clientState
				+ ", comment=" + comment + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((address == null) ? 0 : address.hashCode());
		result = prime * result
				+ ((addressee == null) ? 0 : addressee.hashCode());
		result = prime * result
				+ ((clientState == null) ? 0 : clientState.hashCode());
		result = prime * result + ((comment == null) ? 0 : comment.hashCode());
		result = prime * result
				+ ((companyCode == null) ? 0 : companyCode.hashCode());
		result = prime * result
				+ ((companyTitle == null) ? 0 : companyTitle.hashCode());
		result = prime * result
				+ ((companyVatCode == null) ? 0 : companyVatCode.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((phoneNumber == null) ? 0 : phoneNumber.hashCode());
		result = prime * result
				+ ((validFrom == null) ? 0 : validFrom.hashCode());
		result = prime * result + ((validTo == null) ? 0 : validTo.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Client other = (Client) obj;
		if (address == null) {
			if (other.address != null)
				return false;
		} else if (!address.equals(other.address))
			return false;
		if (addressee == null) {
			if (other.addressee != null)
				return false;
		} else if (!addressee.equals(other.addressee))
			return false;
		if (clientState != other.clientState)
			return false;
		if (comment == null) {
			if (other.comment != null)
				return false;
		} else if (!comment.equals(other.comment))
			return false;
		if (companyCode == null) {
			if (other.companyCode != null)
				return false;
		} else if (!companyCode.equals(other.companyCode))
			return false;
		if (companyTitle == null) {
			if (other.companyTitle != null)
				return false;
		} else if (!companyTitle.equals(other.companyTitle))
			return false;
		if (companyVatCode == null) {
			if (other.companyVatCode != null)
				return false;
		} else if (!companyVatCode.equals(other.companyVatCode))
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (phoneNumber == null) {
			if (other.phoneNumber != null)
				return false;
		} else if (!phoneNumber.equals(other.phoneNumber))
			return false;
		if (validFrom == null) {
			if (other.validFrom != null)
				return false;
		} else if (!validFrom.equals(other.validFrom))
			return false;
		if (validTo == null) {
			if (other.validTo != null)
				return false;
		} else if (!validTo.equals(other.validTo))
			return false;
		return true;
	}
}
