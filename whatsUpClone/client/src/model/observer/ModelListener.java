package observer;
/**
 * Cette interfce est implementé par les vues qui doivent écouter le modèle.
 */
public interface ModelListener {
    /**
     * @param source
     * Cette méthode est appelée par les vues à chaque fois que le modèle
     * change. La plupart de temps, on redessine la vue
     */
    public void somethinHasChanged(Object source);
}
