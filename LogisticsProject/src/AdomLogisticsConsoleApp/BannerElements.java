package AdomLogisticsConsoleApp;

import CustomDataStructures.CustomArrayList;

public class BannerElements {
    private CustomArrayList<String> bannerLines = new CustomArrayList<>(); // store elements in arraylist
    private CustomArrayList<String> menuItems = new CustomArrayList<>();
    private int width; // store width between 44 and 156
    public String title;

    BannerElements(int terminalWidth, String title) {
        this.title = title;
        width = Math.max(44, Math.min(terminalWidth, 156));
    }

    public void printBanner() {
        String topBottom = "+" + "=".repeat(width - 2) + "+";
        String middle;
        int totalPadding = width - title.length() - 4;
        int rightPadding = (totalPadding) / 2;
        int leftPadding = totalPadding - rightPadding;
        middle = "||" + " ".repeat(leftPadding) + title + " ".repeat(rightPadding) + "||";

        bannerLines.addElement(topBottom);
        bannerLines.addElement(middle);

        System.out.println(bannerLines.getElement(0)); // print the top part of the banner
        for (int i = 1; i < bannerLines.size(); i++) {
            System.out.println(bannerLines.getElement(i));
        }
        System.out.println(bannerLines.getElement(0));
    }

    public void addToMenu(String menuItem) {
        if (menuItem != null && !menuItem.isEmpty()) {
            menuItems.addElement(menuItem);
        } else {
            System.out.println("Invalid menu item. Please provide a non-empty string.");
        }
    }

    public void printMenu() {
        for (int i = 0; i < menuItems.size(); i++) {
            int index = i + 1;
            System.out.println(index + ". " + menuItems.getElement(i));
        }
    }
    public int size(){
        return menuItems.size();
    }

    public String getElement(int i) {
        if (size()>0) {
            return menuItems.getElement(i);
        }
        return null;
    }
}
