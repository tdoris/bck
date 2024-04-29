

package BCK.ANN;

public interface BCKObservable{
    public void registerObserver(BCKObserver ob);
    public void removeObserver(BCKObserver ob);
    public void notifyObservers();
}
