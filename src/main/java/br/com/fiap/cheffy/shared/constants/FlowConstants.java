package br.com.fiap.cheffy.shared.constants;

public enum FlowConstants {
    TRIAGE_CREATE_USE_CASE_FOOD_ITEM_FLOW("CreateFoodItemUseCase");

    public  final String name;
    FlowConstants(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
}
