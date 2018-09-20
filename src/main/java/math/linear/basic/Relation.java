package math.linear.basic;

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
}
