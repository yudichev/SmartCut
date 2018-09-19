package math.linear.basic;

public enum Relation {
    EQUAL,
    GREATER_THEN,
    LESS_THEN,
    GREATER_OR_EQUAL,
    LESS_OR_EQUAL;

    public Relation invert(){
        switch(this){
            case LESS_THEN: return GREATER_THEN;
            case GREATER_THEN: return LESS_THEN;
            case GREATER_OR_EQUAL: return LESS_OR_EQUAL;
            case LESS_OR_EQUAL: return GREATER_OR_EQUAL;
            case EQUAL: return EQUAL;
            default: return EQUAL;
        }
    }
}
