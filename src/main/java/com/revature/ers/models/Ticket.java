package com.revature.ers.models;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Ticket {
	
	private int id;
	private TicketStatus status;
	private int userID;
	private TicketType ticketType;
	private BigDecimal amount;
	private String description;
	private Timestamp timestamp;
	
	public Ticket(int id, TicketStatus status, int userID, TicketType ticketType, BigDecimal amount, String description,
			Timestamp timestamp) {
		super();
		this.id = id;
		this.status = status;
		this.userID = userID;
		this.ticketType = ticketType;
		this.amount = amount;
		this.description = description;
		this.timestamp = timestamp;
	}
	public Ticket() {
		super();
		// TODO Auto-generated constructor stub
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public TicketStatus getStatus() {
		return status;
	}
	public void setStatus(TicketStatus status) {
		this.status = status;
	}
	public int getUserID() {
		return userID;
	}
	public void setUserID(int userID) {
		this.userID = userID;
	}
	public TicketType getTicketType() {
		return ticketType;
	}
	public void setTicketType(TicketType ticketType) {
		this.ticketType = ticketType;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Timestamp getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	@Override
	public String toString() {
		return "Ticket [id=" + id + ", status=" + status + ", userID=" + userID + ", ticketType=" + ticketType
				+ ", amount=" + amount + ", description=" + description + ", timestamp=" + timestamp + "]";
	}
	
}
