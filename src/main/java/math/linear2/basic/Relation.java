package math.linear2.basic;

public enum Relation {
    EQUAL,
    GREATER_OR_EQUAL,
    LESS_OR_EQUAL;

    public Relation invert(){
        switch(this){
            case GREATER_OR_EQUAL: return LESS_OR_EQUAL;
            case LESS_OR_EQUAL: return GREATER_OR_EQUAL;
            case EQUAL: return EQUAL;
            default: return EQUAL;
        }
    }

    public boolean isEqual(){
        return this.equals(EQUAL);
    }

    public boolean isGreaterOrEqual(){
        return this.equals(GREATER_OR_EQUAL);
    }

    public boolean isLessOrEqual(){
        return this.equals(LESS_OR_EQUAL);
    }
}
