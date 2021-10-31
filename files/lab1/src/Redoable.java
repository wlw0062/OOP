public interface Redoable extends Command,Cloneable{
    void redo();
    void undo();
    Object clone();
}
