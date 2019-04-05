package org.opensrp.dto;

public class GenderReportsDTO {
    private String itemName,sn;
    private String male,female;

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getMale() {
        return male;
    }

    public void setMale(String male) {
        this.male = male;
    }

    public String getFemale() {
        return female;
    }

    public void setFemale(String female) {
        this.female = female;
    }
}
