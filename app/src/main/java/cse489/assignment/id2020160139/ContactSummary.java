package cse489.assignment.id2020160139;

public class ContactSummary {
  String UniqueID;
  String name = "";
  String email = "";
  String homePhone = "";
  String officePhone = "";
  String image;


  public ContactSummary(String UniqueID, String name, String email, String homePhone, String officePhone,String image){
    this.UniqueID = UniqueID;
    this.name = name;
    this.email = email;
    this.homePhone = homePhone;
    this.officePhone = officePhone;
    this.image = image;


  }
}
