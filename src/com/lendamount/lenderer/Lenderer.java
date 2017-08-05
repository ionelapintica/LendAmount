package com.lendamount.lenderer;

public class Lenderer implements Comparable<Lenderer>{

    private String name;
    private double rate;
    private double amount;

    public Lenderer(String name, double rate, double amount) {
        this.name = name;
        this.rate = rate;
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Override
    public int compareTo(Lenderer o) {
        if (this.rate < o.rate) {
            return -1;
        }else if(this.rate == o.rate){
            return 0;
        }else
            return 1;
    }
}
