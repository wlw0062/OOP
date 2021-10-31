public interface Revocable extends Command, Cloneable{
    void redo();
    void undo();
    Object clone();
}
