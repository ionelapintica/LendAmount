package com.lendamount.quotation;

import com.lendamount.lenderer.Lenderer;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Quotation {

    public static void main(String [] args){

        if (args.length !=2){
           throw new IllegalArgumentException("Inccorect number of arguments! Please provide a correct input!");
        }
        int amountToBorrow = 0;
        try {
            amountToBorrow = Integer.parseInt(args[1]);
        }catch(NumberFormatException e) {
            throw new IllegalArgumentException("The amount argument must be an integer value!");
        }
        if(amountToBorrow % 100 != 0){
            throw new IllegalArgumentException("The amount must be multiple of 100");
        }
        if ((amountToBorrow < 1000) || (amountToBorrow > 15000)){
            throw new IllegalArgumentException("The amount should a value between 1000 and 15000!");
        }

        List<Lenderer> lenderers = processCsvFile(args[0]);

        Collections.sort(lenderers);

        double sumAmounts = lenderers.stream().mapToDouble(a -> a.getAmount()).sum();
        if (sumAmounts < amountToBorrow){
            System.out.println("The requested amount is greater than the available amount! Please provide a smaller amount!");
            System.exit(0);
        }
        List<Lenderer> availableLenderers = listOfLenderers(amountToBorrow, lenderers);

        double totalRate = (availableLenderers.stream().mapToDouble( a -> a.getRate()*a.getAmount()).sum()) / amountToBorrow*100;

        double totalAmountToPay = availableLenderers.stream().mapToDouble( a -> a.getAmount() * Math.pow(1+ (a.getRate()/12), 36)).sum();

        System.out.println("Requested Amount = " + amountToBorrow);
        System.out.printf("Rate = %.1f%%\n" + totalRate);
        System.out.printf("Monthly repayment = %.2f \n" + (totalAmountToPay/36));
        System.out.printf("Totally payment = %.2f \n" + totalAmountToPay);
    }

    private static List<Lenderer> processCsvFile(String fileName){
        List<Lenderer> lenderers = new ArrayList<>();

        Path filePath = Paths.get(fileName);
        try(BufferedReader br = new BufferedReader(new FileReader(filePath.toAbsolutePath().toString()))){
            String line = "";
            while ((line = br.readLine()) != null ){
                String [] lendererInfo = line.split(",");
                Lenderer lenderer = new Lenderer(lendererInfo[0], Double.parseDouble(lendererInfo[1]), Double.parseDouble(lendererInfo[2]));
                lenderers.add(lenderer);
            }
        } catch (FileNotFoundException e) {
            System.out.println("The file was not found! Please provide a correct path!");
            System.exit(0);
        } catch (IOException e) {
            System.out.println("The file coulnd not be processed! Please try again!");
            System.exit(0);
        } catch (NumberFormatException e) {
            System.out.println("The file contains wrong data in rate or amount column! Please check data and correct accordingly!");
            System.exit(0);
        }
        return lenderers;
    }

    private static List<Lenderer> listOfLenderers(double amountToBarrow, List<Lenderer> lenderers){
        List<Lenderer> availableLenderers = new ArrayList<>();

        for(Lenderer lenderer : lenderers){
            if (lenderer.getAmount() < amountToBarrow){
                amountToBarrow = amountToBarrow - lenderer.getAmount();
                availableLenderers.add(lenderer);
            }else {
                lenderer.setAmount(amountToBarrow);
                availableLenderers.add(lenderer);
                break;
            }

        }
        return availableLenderers;
    }





}
