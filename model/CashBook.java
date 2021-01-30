package kclc.model;

import java.time.LocalDate;

public class CashBook {
    private Integer refId;
    private LocalDate date;
    private String description;
    private Double income;
    private Double expense;
    private Double bankBalance;

    public CashBook(int refId, LocalDate date, String description, Double income, Double expense, Double bankBalance) {
        this.refId = refId;
        this.date = date;
        this.description = description;
        this.income = income;
        this.expense = expense;
        this.bankBalance = bankBalance;
    }

    public CashBook() {

    }

    public int getRefId() {
        return refId;
    }

    public void setRefId(Integer refId) {
        this.refId = refId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getIncome() {
        return income;
    }

    public void setIncome(Double income) {
        this.income = income;
    }

    public Double getExpense() {
        return expense;
    }

    public void setExpense(Double expense) {
        this.expense = expense;
    }

    public Double getBankBalance() {
        return bankBalance;
    }

    public void setBankBalance(Double bankBalance) {
        this.bankBalance = bankBalance;
    }
}
