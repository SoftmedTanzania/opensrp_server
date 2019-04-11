package org.opensrp.dto;

public class MalariaReportDTO {
    private String itemName,sn;
    private String greaterThan5Male,greaterThan5Female,lessThanFiveMale,lessThanFiveFemale,totalMale,totalFemale;


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

    public String getGreaterThan5Male() {
        return greaterThan5Male;
    }

    public void setGreaterThan5Male(String greaterThan5Male) {
        this.greaterThan5Male = greaterThan5Male;
    }

    public String getGreaterThan5Female() {
        return greaterThan5Female;
    }

    public void setGreaterThan5Female(String greaterThan5Female) {
        this.greaterThan5Female = greaterThan5Female;
    }

    public String getLessThanFiveMale() {
        return lessThanFiveMale;
    }

    public void setLessThanFiveMale(String lessThanFiveMale) {
        this.lessThanFiveMale = lessThanFiveMale;
    }

    public String getLessThanFiveFemale() {
        return lessThanFiveFemale;
    }

    public void setLessThanFiveFemale(String lessThanFiveFemale) {
        this.lessThanFiveFemale = lessThanFiveFemale;
    }

    public String getTotalMale() {
        return totalMale;
    }

    public void setTotalMale(String totalMale) {
        this.totalMale = totalMale;
    }

    public String getTotalFemale() {
        return totalFemale;
    }

    public void setTotalFemale(String totalFemale) {
        this.totalFemale = totalFemale;
    }
}
