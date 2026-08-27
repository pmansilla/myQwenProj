package com.czw.smartkit.util;

/* loaded from: classes.dex */
public class ContactInfo {
    private String contactId;
    private boolean isAddContact;
    private boolean isChooseContact;
    private String letter;
    private String name;
    private String phone;

    public String getContactId() {
        return this.contactId;
    }

    public String getLetter() {
        return this.letter;
    }

    public String getName() {
        return this.name;
    }

    public String getPhone() {
        return this.phone;
    }

    public boolean isAddContact() {
        return this.isAddContact;
    }

    public boolean isChooseContact() {
        return this.isChooseContact;
    }

    public void setAddContact(boolean z) {
        this.isAddContact = z;
    }

    public void setChooseContact(boolean z) {
        this.isChooseContact = z;
    }

    public void setContactId(String str) {
        this.contactId = str;
    }

    public void setLetter(String str) {
        this.letter = str;
    }

    public void setName(String str) {
        this.name = str;
    }

    public void setPhone(String str) {
        this.phone = str;
    }

    public String toString() {
        return "ContactInfo{contactId='" + this.contactId + "', letter='" + this.letter + "', name='" + this.name + "', phone='" + this.phone + "', isAddContact=" + this.isAddContact + ", isChooseContact=" + this.isChooseContact + '}';
    }
}
