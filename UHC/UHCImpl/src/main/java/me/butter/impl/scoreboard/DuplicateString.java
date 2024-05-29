package me.butter.impl.scoreboard;

public class DuplicateString {

    private String string;
    private int number;

    public DuplicateString(String string, int number) {
        this.string = string;
        this.number = number;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof DuplicateString) {
            DuplicateString other = (DuplicateString) obj;
            return other.string.equals(string) && other.number == number;
        }
        return false;
    }

    public String getString() {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < number; i++) {
            sb.append("§r");
        }
        sb.append(string);
        return sb.toString();
    }
}
