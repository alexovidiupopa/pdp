package ro.alexpopa.domain;

import java.util.List;

public final class Vector {
    private List<Integer> content;
    private int length;

    public Vector(List<Integer> content) {
        this.content = content;
        this.length = content.size();
    }

    public int get(int index){
        return content.get(index);
    }

    public List<Integer> getContent() {
        return content;
    }

    public void setContent(List<Integer> content) {
        this.content = content;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    @Override
    public String toString() {
        return "Vector{" +
                "content=" + content +
                ", length=" + length +
                '}';
    }
}
