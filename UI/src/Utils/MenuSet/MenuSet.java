package Utils.MenuSet;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class MenuSet extends HashSet<String> {

    public static void main(String[] args) {
        Set<String> mainMenu = new MenuSet();

        mainMenu.add("XML");
        mainMenu.add("Loans");
        mainMenu.add("Exit");

        System.out.println(mainMenu.toString());
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

}
