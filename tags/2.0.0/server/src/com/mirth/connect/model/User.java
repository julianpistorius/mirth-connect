/*
 * Copyright (c) Mirth Corporation. All rights reserved.
 * http://www.mirthcorp.com
 *
 * The software in this package is published under the terms of the MPL
 * license a copy of which has been included with this distribution in
 * the LICENSE.txt file.
 */

package com.mirth.connect.model;

import java.io.Serializable;
import java.util.Calendar;

import org.apache.commons.lang.ObjectUtils;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("user")
public class User implements Serializable {
	private Integer id;
	private String username;
	private String email;
	private String firstName;
	private String lastName;
	private String organization;
	private String description;
	private String phoneNumber;
	private Calendar lastLogin;
	
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getFirstName() {
		return this.firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public String getLastName() {
		return this.lastName;
	}
	
	public void setOrganization(String organization) {
		this.organization = organization;
	}
	
	public String getOrganization() {
		return this.organization;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getPhoneNumber() {
		return this.phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	public Calendar getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(Calendar lastLogin) {
		this.lastLogin = lastLogin;
	}
	
	public boolean equals(Object that) {
		if (this == that) {
			return true;
		}
		
		if (!(that instanceof User)) {
			return false;
		}
		
		User user = (User) that;
		
		return
			ObjectUtils.equals(this.getId(), user.getId()) &&
			ObjectUtils.equals(this.getUsername(), user.getUsername()) &&
			ObjectUtils.equals(this.getEmail(), user.getEmail()) &&
			ObjectUtils.equals(this.getFirstName(), user.getFirstName()) &&
			ObjectUtils.equals(this.getLastName(), user.getLastName()) &&
			ObjectUtils.equals(this.getOrganization(), user.getOrganization()) &&
			ObjectUtils.equals(this.getDescription(), user.getDescription()) &&
			ObjectUtils.equals(this.getLastLogin(), user.getLastLogin()) &&
			ObjectUtils.equals(this.getPhoneNumber(), user.getPhoneNumber());
	}

	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(this.getClass().getName() + "[");
		builder.append("id=" + getId() + ", ");
		builder.append("username=" + getUsername() + ", ");
		builder.append("email=" + getEmail() + ", ");
		builder.append("firstname=" + getFirstName() + ", ");
		builder.append("lastname=" + getLastName() + ", ");
		builder.append("organization=" + getOrganization() + ", ");
		builder.append("description=" + getDescription() + ", ");
		builder.append("lastLogin=" + getLastLogin() + ", ");
		builder.append("phonenumber=" + getPhoneNumber());
		builder.append("]");
		return builder.toString();
	}
}