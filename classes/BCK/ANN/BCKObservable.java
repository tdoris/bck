package BCK.ANN;

public interface BCKObservable {
   void registerObserver(BCKObserver var1);

   void removeObserver(BCKObserver var1);

   void notifyObservers();
}
