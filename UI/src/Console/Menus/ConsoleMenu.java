package Console.Menus;

import Console.Menus.Exceptions.NoOptionException;
import Utils.MenuSet.MenuSet;

import java.util.Scanner;
import java.util.Set;

public abstract class ConsoleMenu {
    private Set<String> menuOptions;

    ConsoleMenu(Set<String> menuOptions) {
        this.menuOptions = menuOptions;

    }
    ConsoleMenu() {
        menuOptions = new MenuSet();
    }

    public Set<String> getMenuOptions() {
        return menuOptions;
    }

    public void setMenuOptions(Set<String> menuOptions) {
        this.menuOptions = menuOptions;
    }

    public void printMenu() {
        System.out.println(menuOptions.toString());
    }

    public int getOption() throws NoOptionException {
        Scanner inScan = new Scanner(System.in);
        int opt = inScan.nextInt();

        if(opt > menuOptions.size()) {
            throw new NoOptionException();
        }

        return opt;
    }

}
