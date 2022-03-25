package utils;

import console.menus.exceptions.NoOptionException;

import java.util.HashSet;
import java.util.Scanner;

public class MenuSet extends HashSet<String> {

    public static void main(String[] args) {
        MenuSet mainMenu = new MenuSet();

        mainMenu.add("XML");
        mainMenu.add("Loans");
        mainMenu.add("Exit");

        mainMenu.printMenu();
        while(true) {
            try {
                System.out.println(mainMenu.getOption());
                break;
            } catch (NoOptionException e) {
                System.out.println(e.getMessage());
            }
        }

    }
    @Override
    public String toString() {
        StringBuilder menuStr = new StringBuilder();
        int i = 1;

        for(String currStr : this) {
            menuStr.append(i);
            menuStr.append(". ");
            menuStr.append(currStr);
            menuStr.append('\n');
            i++;
        }

        menuStr.deleteCharAt(menuStr.length() - 1); // remove last \n
        return menuStr.toString();
    }

    public int getOption() throws NoOptionException {
        Scanner inScan = new Scanner(System.in);
        int opt = inScan.nextInt();

        if(opt > this.size() || opt <= 0) {
            throw new NoOptionException();
        }

        return opt;
    }

    public void printMenu() {
        System.out.println(this.toString());
    }

}
