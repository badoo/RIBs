package fixtures;

public class TestAnnotatedInteractor {
    private TestAnnotatedInteractor() {
    }

    public static AnnotatedInteractor create(final Boolean fieldOne, final Integer fieldTwo) {
        AnnotatedInteractor interactor = new AnnotatedInteractor();
        interactor.fieldOne = fieldOne;
        interactor.fieldTwo = fieldTwo;
        return interactor;
    }
}
