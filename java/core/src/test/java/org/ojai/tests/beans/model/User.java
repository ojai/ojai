/**
 * Copyright (c) 2015 MapR, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.ojai.tests.beans.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class User {
  private String firstName;
  private String lastName;
  private int age;
  private List<String> interets;
  private Address address;
  private double account;
  private boolean customer = false;

  public User() {
  }

  @JsonProperty("first_name")
  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  @JsonProperty("last_name")
  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public int getAge() {
    return age;
  }

  public void setAge(int age) {
    this.age = age;
  }

  public List<String> getInterets() {
    return interets;
  }

  public void setInterets(List<String> interets) {
    this.interets = interets;
  }

  public Address getAddress() {
    return address;
  }

  public void setAddress(Address address) {
    this.address = address;
  }

  public double getAccount() {
    return account;
  }

  public void setAccount(double account) {
    this.account = account;
  }

  public boolean isCustomer() {
    return customer;
  }

  public void setCustomer(boolean customer) {
    this.customer = customer;
  }

  @Override
  public String toString() {
    return "User{" + "firstName='" + firstName + '\'' + ", lastName='" + lastName + '\'' + ", age="
        + age + ", interets=" + interets + ", address=" + address + ", account=" + account
        + ", customer=" + customer + '}';
  }

}
